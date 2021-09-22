package com.jhpark.websupport.controller;

import com.jhpark.websupport.payload.request.ActiveTransactionRequest;
import com.jhpark.websupport.payload.request.ZipCodeRequest;
import com.jhpark.websupport.payload.response.PerspectiveLockResponse;
import com.jhpark.websupport.payload.response.ZipCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Random;

/**
 * Ch 16. 쓰기 가능한 웹 설계
 * % readable < writeable 어려운점 (고려할점):
 *  - 동시에 쓰기를 하는 경우
 *  - 동시에 갱신을 하는 경우
 *  - 복수의 처리과정을 반드시 실행하는 상황
 *
 */

@RestController
@RequestMapping(path = "/write",
    consumes = "application/json",
    produces = "application/json")
public class WriteableWebDesignController {
  Logger LOG = LoggerFactory.getLogger(WriteableWebDesignController.class);
  private static final String BASE_DOMAIN = "http://{mydomain}.com";
  /**
   * Ch15 에 이어서 기능 추가
   */

  /**
   * #1. 리소스의 작성#
   *  - 새로운 우편번호 추가기능
   * 방법 2가지 존재
   *  1. Factory resource(생성을 위한 uri) 에 `POST` : response로 어떤 uri 가 생성되었는지 response
   *  2. 직접 작성 (특정 uri로 ) : `PUT`
   *
   *  PUT의 장단점
   *    - 장점 : 멱등성 보장
   *      - 서버측 구현이 간단 : [%] 비슷한듯
   *      - 클라쪽에서 `작성`과 `변경`을 구별할 필요가 없어도 되므로 클라쪽 구현이 간단해진다. : 보통 PUT 은 갱신에 잘 사용
   *    - 단점 :
   *      - 클라가 URI 구조를 미리 알아야 한다. : POST 는 Location Header 로 파악
   *      - request 만 봐서는 신규작성인지 갱신인지 구별이 어렵다.
   */
  @RequestMapping(path = "/add", method = RequestMethod.POST)
  public ZipCodeRequest addPost(@RequestBody ZipCodeRequest body,
                                 HttpServletResponse response) { // 1. Factory resource
    LOG.info("request body: {}", body);
    addZipCode(body);

    response.setStatus(HttpStatus.CREATED.value());
    response.setHeader("Location", BASE_DOMAIN + "/" + body.getZipCode()); //클라는 Location header 를 보고 생성된 uri 확인
    //POST 는 Location header가 필요하다. POLo포로..

    return body;
  }

  @RequestMapping(path = "/add/{zipCode}", method = RequestMethod.PUT)
  public ZipCodeRequest addPut(@RequestBody ZipCodeRequest body,
                     HttpServletResponse response) { // 2. 직접 작성
    addZipCode(body);

    response.setStatus(HttpStatus.CREATED.value());
    return body;
  }
  private int addZipCode(ZipCodeRequest body) {
    //TODO : dao 로 들어온 body 를 db에 insert : PK 는 gen seq 혹은 zip code
    return 1;
  }

  /**
   * #2. 리소스의 갱신 #
   *  - 갱신은 기본적으로 PUT : but, 뒤에 설명할 일괄갱신은 POST
   *
   * 1. 벌크 업데이트 : 갱신하고자 하는 리소스 전체를 그대로 집어 넣음
   *  - 가장 기본적인 방법
   *  - 단점 : 네트워크대역 많이 사용
   * 2. Partial 업데이트 : 갱신 하고자 하는 부분만 전송
   *  - 단점 : GET한 리소스의 일부를 수정해서 그대로 PUT하는 방식은 불가능하다.
   *  - 보통 Partail 을 지원하면 Bulk 도 지원하는게 바람직하다.
   *
   *  % 에러처리
   *   - `우편번호`를 갱신하고자 하는 경우는 400 Bad Request를 return 하는게 맞다.
   *   - `작성일시` 같은 서버에서 자동으로 생성하는 값을 갱신하는 요청을 한다고 해도 그냥 무시하고 200을 주는게 맞다.
   */
  @RequestMapping(path = "/update/{zipCode}", method = RequestMethod.PUT)
  public ZipCodeRequest updateBulk(@RequestBody ZipCodeRequest body, //Request의 전체를 무조건 받음
                               HttpServletResponse response) {

    if (updateDao(body)) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      return null;
    }
    return body;
  }

  @RequestMapping(path = "/update/{zipCode}", method = RequestMethod.PUT)
  public ZipCodeRequest updatePartial(@Valid @RequestBody ZipCodeRequest body, //Request의 부분만 받음 - 필수적인 요소들은 Request Class 에서
                                                                                //@NotNull, @NotEmpty 로 구분
                               HttpServletResponse response) {
    if (updateDao(body)) {
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      return null;
    }
    return body;
  }

  private boolean updateDao(ZipCodeRequest body) {
    //TODO : body 정보대로 upodate
    // `우편번호`를 갱신하고자 하는 경우 return false
    return true;
  }


  /**
   * # 3. 리소스의 삭제 #
   * DELETE Method 로 삭제 : 갱신보다 간단하다.
   * 주의 :
   *  - 삭제 대상 리소스 아래에 자식 리소스가 존재하는 경우 : 일반적으로는 부모 리소스가 삭제되면 자식 리소스는 삭제되는게 맞다.
   *
   */

  /**
   * #4. 일괄처리 #
   * batch Insert or Update
   * 1. 일괄갱신 :
   *  - 기존에 Request~ 로 주던걸 Request List (JSON List 가 되겠지?)로 준다. :
   *   - 여기서 일괄 갱신은 PUT이 불가능하다. : URI 를 특정할수가 없으니까 - 복수개니까
   *
   *  - %문제는 `에러`가 발생한 경우 대처방법 :
   *   |아무런 고려를 하지 않으면 보통 성공한놈은 성공하고 실패한놈은 실패한상태로 될것이다.
   *   - 1. Request를 Transaction 화
   *    - 도중에 실패하면 아무 처리도 하지 않음
   *   - 2. 어느 리소스가 성공했고 어느 리소스가 실패했는지 클라에 전달
   *    - a. 207 Muli-Status : WebDAV 에서 정의하고 있다. - PASS
   *    - b. json 배열로 각 리소스에 대한 결과값을 return
   */

  /**
   * #5. 트랜잭션 #
   * 예시
   *  - 해결해야만 하는 문제
   *   - 리소스의 일괄 삭제
   *    - /1, /2, /3 : but 이렇게 따로 삭제를 하면 2 삭제 도중에 에러가 발생하면 1만 삭제되는 꼴이 된다..
   *     -> 트랜잭션 처리를 하기 위해서는 어떻게 해야할까?
   *
   *  - 트랜잭션 리소스 (URI) 를 만들자
   *   - 1. transaction factory 로 transactionNum 을 만들고
   *   - 2. 그 num으로 삭제할 address 들의 zipCode를 추가
   *   - % 지금 예시는 삭제라 body가 없지만 갱신을 하는 경우는 POST에 갱신할 값들을 넣어줘라
   */
  //약간 독특 : 사실 Spring 의 @Transaction 을 사용하면 딱히 필요가 없다..
  @RequestMapping(path = "/transactions", method = RequestMethod.POST) //factory resource
  public void transactions(HttpServletResponse response) {
    long transactionNum = 999; // 임의의 번호를 생성하고 client에 알려줌
    response.setStatus(HttpStatus.CREATED.value());
    response.setHeader("Location", BASE_DOMAIN + "/transactions" + "/" + transactionNum);
  }
  // 1. /transaction 으로 요청하고 받은 transactionNum으로 삭제하고자 하는 address 의 zipCode로
  //  /{zipCode}로 (PUT) 리소스를 생성한다.
  @RequestMapping(path = "/transactions/{transactionNum}/{zipCode}", method = RequestMethod.PUT)
  public void addDeletingAddress(HttpServletResponse response) {
    response.setStatus(HttpStatus.CREATED.value());
  }

  // 2. 여기까지 하면 transactionNum을 기준으로 자식 URI 3개가 생긴다.

  //3. 이제 만든 트랜잭션을 실행
  @RequestMapping(path = "/transactions/{transactionNum}", method = RequestMethod.PUT,
  consumes = "application/x-www-form-urlencoded")
  public void activeTransaction(@RequestBody ActiveTransactionRequest request,
                                @PathVariable("transactionNum") long transactionNum,
                                HttpServletResponse response) {
    LOG.info("request : {}", request);
    //TODO : transactionNum 에 들어있는 리소스들 모두 처리
    response.setStatus(HttpStatus.OK.value());
  }

  //4. 생성한 트랜잭션 리소스들 모두 Delete
  @RequestMapping(path = "/transactions/{transactionNum}", method = RequestMethod.DELETE)
  public void deleteTransaction(@PathVariable("transactionNum") long transactionNum,
                                HttpServletResponse response) {
    //TODO : transactionNum 과 에 들어있는 리소스들 모두 삭제
    response.setStatus(HttpStatus.OK.value());
  }

  /**
   * #6. 배타제어 : Mutual Exclusion
   * - 하나의 리소스를 편잽해 경합이 일어나지 않도록 하나의 클라이언트만 편집하도록 제어
   * 두가지로 처리 가능 : 예시 : 클라 A,B에서 동시에 갱신하는 경우
   * 1. 비관적 잠금
   *  - 사용자를 신용 못하는 경우
   *  - 구현 방법
   *    - 잠금을 표현하는 자식 리소스를 새로 추가
   *    - lock 리소스가 존재하면  - 400 Bad Request 응답
   *    - update 후에는 lock 리소스 제거
   *  - 잠금의 권한을 가진 사람 이외에는 편집할 수 없도록 제한
   *  - %스케일이 커질수록 문제 발생 (wiki 같은곳은 여러명이 같이 수정을 한다 보통..)
   * 2. 낙관적 잠금
   *  - 같은 문서를 여러 사람이 계속 편집하는 경우는 거의 없다는 전제
   *  - 구현방법
   *    - 조건부 PUT, 조건부 DELETE : Header에 Last-Modified나 ETag 를 사용
   *      - ETag의 If-Match, Last-Modified의 If-Unmodified-Since 한다면 PUT 을 강행
   *            (이전 GET 할 떄는 ETag 의 If-Not-Match, Last-Modified의 If-modified-Since 를 사용했었음.. 그냥 그렇다고..)
   *      - 갱신요청을 할 때 자신이 갱신하고자 하는 리소스의 변경 여부를 먼저 확인
   *    - A가 수정하는 중에 B가 수정후에 PUT 발생한 상황
   *
   *   - 리소스가 변경되어 있는 경우는 412 Precondition Failed를 반환
   *    - 412에 대한 대처 방법은 세가지 존재
   *      1. 경합을 일으킨 사용자에게 확인시키고 갱신 또는 삭제
   *      2. 경합을 일으킨 사용자에게 경합 리소스를 별도 리소스로 보존 : 일단 보존, 클라에 확인 요청
   *      3. 경합을 일으킨 사용자에게  변경점을 전하고 병합 촉구 : user에게 경합을 확인시킴
   */
  @RequestMapping(path = "/lock/{zipCode}", consumes = "application/x-www-form-urlencoded")
  public PerspectiveLockResponse perspectiveLock(@PathVariable("zipCode") long zipCode,
                                                 HttpServletResponse response) {
    response.setHeader("Location", BASE_DOMAIN+"/"+zipCode+"/lock");
    return new PerspectiveLockResponse("exclusive");
  }


  /**
   * # 마무리 : 설계의 밸런스 #
   *
   * 당면한 문제에 대해서 복수의 해결방법이 존재할 경우 고민에 빠질것이다.
   *
   * 고민 사항들
   * - 서비스의 규모
   * - 대상 범위
   * - 상정하는 사용자
   * - 납기
   * - 필요한 품질
   * - 서비스 레벨
   *
   * 설계는 밸런스를 잡는 작업이다. : trade off 가 항상 존재한다.
   *
   *
   * 필자가 중요하게 생각하는 지침을 정리하고 마무리한다.
   * 1. 될 수 있는 한 심플하게 유지
   *  - 전체를 심플하게 유지하는게 제일 좋다.
   * 2. 막히면 리소스로 돌아가 생각한다.
   *  - 구현할 수 없는 기능이 있다고 느껴지면, 독립된 별도의 리소스로 대처할 수 없을지 생각
   *   - 검색기능을 SEARCH하는 메서를 HTTP 에 추가하는 식이 아니라, 검색 결과를 GET한다는 느낌으로 맞춰서 생각
   * 3. POST는 만능이다.
   *  - 일괄갱신처럼 PUT을 포기하고 POST로 해결을 하는것 처럼 복수의 리소스가 대상이 된 시점에서 PUT를 포기하고 POST로 하자..
   *  - [%] 그냥 멱등성을 포기하라는 말인듯 하다.
   */
}
