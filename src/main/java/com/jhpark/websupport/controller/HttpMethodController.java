package com.jhpark.websupport.controller;

import com.jhpark.websupport.payload.request.SampleRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 주의 : 본 예시에서는 공부 목적으로 만들어진 api 이므로 기본적으로 api apth의 시작을 method 의 명으로 작성했다.
 *        실제 설계시에 method 의 명은 path 에 추가하지 말도록 하자.
 *
 *
 * Ch. 7 HTTP 메서드
 * HTTP Method 가 왜 단지 8개로만 운영이 가능한지.
 * 각 Method 의 존재 이유 & 용도 & 설계
 */
@RestController
@RequestMapping(value = "/method")
public class HttpMethodController {
  Logger LOG = LoggerFactory.getLogger(HttpMethodController.class);

  /**
   * 배경지식
   * Method 는 총 8개 but, 6개만 취급 (TRACE, CONNECT 는 거의 사용X)
   * URI 에는 동사가 들어가지 않아야 한다. : method 로 동작하는 action을 내포
   *
   * 멱등성 : 계속 반복해서 요청해도 똑같은 상황 보장 : for 통신 불통 - 보통 timeout 이면 재전송을 한다.
   * 안정성 : 리소스의 상태(정보)를 변화시키지 않음
   *
   * % 메서드 오용 %
   * - http 의 스펙과 다르게 api 를 설계하면 모든 개념이 무너진다. -> GET 으로 삭제 정책을 실행하면, 안정성이 깨지기 때문에 클라쪽에서 혼동이 올 수가 있다.
   * - POST 의 오용
   *   -- POST는 만능 method 이기는 하지만 적절한 method가 있음에도 지나친 POST 의 사용은 `멱등성`과 `안정성`을 보장하지 못한다.
   *   -- ex) POST의 오용 예시 : XML-RPC, SOAP RPC를 구현하기 위한 프로토콜 : but, 모든 function call 을 POST로 날리게 되어있다.. : 함수의 기능이  get이나 delete 상황이어도..
   *        body 에 xml 형식으로 (content-type : application/xml)
   *        
   * - PUT 의 오용 (멱등성 날라감)
   *   -- 갱신을 하는 case 에서 상대적인 차분을 전송을 하면 PUT 은 멱등성을 잃어버린다. : ex) 가격을 갱신하는데 +50 으로 request : 복수번 요청을 할 때마다 결과가 달라진다.
   *   -- 갱신하는 경우에는 항상 `절대적인 데이터 값 : (희망하는)변경 후의 값`을 요청해야 `멱등성`이 보장된다.
   * 
   * - DELETE 의 오용 (멱등성 날라감)
   *   -- alias(별칭):의미를 가지는 의 resource 의 경우 DELETE /latest -> 계속 호출하면 삭제한 후의 가장 최신 데이터를 다시 삭제 한다. : 1.2 삭제 -> 1.1 삭제 -> 1.0 삭제
   *   -- 즉, 이런 특수한 리소스(동일 uri 에서 리소스가 영구적으로 나타나는게 아닌 상황, 시기에 따라서 변화하는 리소스)의 경우는 UPDATE 와 DELETE 를 조작 할 수 없도록 설계하자.
   *   
   *   
   * % Chapter 정리 %
   * - HTTP 메서드는 심플한 만큼 REST의 한정하여 고정시켰다. 
   * - 이렇기에 각 메서드 별로 독창적인 특징을 가지게 되고 HTTP 를 파워풀하게 만들어줬다.
   *  -- GET에 숨겨진 안정성
   *  -- PUT, DELETE 의 멱등성
   *  -- POST 의 다체로운 활용도
   * - 다시 주의를 하자면 : 이런 설계 의도를 맞춰서 app, api 를 구성해야 HTTP가 제시, 보장하는 특징들을 잘 살릴수가 있다.
   */

  /**
   * GET : 리소스 취득 -
   * 멱등성, 안정성 모두 보장
   * @param value
   * @return
   */
  @GetMapping(value = "/get/{value}")
  public String get(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                 @PathVariable("value") String value) {
    response.setStatus(200);
//    LOG.info("session : {}", session);
    makeLog(request, response);
    return value;
  }

  /**
   * POST : 서브 리소스 작성 (add item in list), 서브 리소스 추가,
   * 등등.. 잡다.. (ex uri 의 최대 제한 char 은 2000자. : body 를 기반으로 데이터를 가져올때는 post가 필요하다)
   * 멱등성 보장x, 안정성 보장x : POST를 여러번 보내는 것에 대해서는 굉장히 신중해야 한다.
   * ex) 쇼핑몰에서 뒤로 버튼을 조작할 떄 "다시 전송 하겠습니까?" 라는 문구가 나온다.
   * @return
   */
  // sub resource 작성 : ex) 게시글에 글 추가
  @PostMapping(value = "/post/list", consumes = "application/json")
  public HttpStatus post(@RequestBody SampleRequest request, HttpServletResponse response) {
    LOG.info("5번째 게시글 추가됨 -> item5");
    response.setHeader("Location", "http://domain:port/list/item5"); // 생성된 리소스의 uri 를 Location 에 넣어서 반환
    return HttpStatus.CREATED; //리소스를 새로 생성했으니 201로 반환
  }

  //리소스에 데이터 추가 : ex) 로그데이터에 추가
  @PostMapping(value = "/post/log")
  public HttpStatus post2(@RequestBody SampleRequest request) {
    LOG.info("기존 LOG Data에 data 추가");
    return HttpStatus.OK;
  }

  //다른 method 를 대응 안되는 케이스 : ex) data 를 조회하는데 query 가 너무 길떄 (get 의 uri 제한 2000자가 넘어서 body 로 query 를 날려야함) : body 는 글자 제한이 없음
  @PostMapping(value = "/post/search", consumes = "application/x-www-form-urlencoded")
  public String post3(@RequestBody SampleRequest request) {
    // body : "q=very+long+keyword+foo+bar ...."
    return "search 로 검색된 결과 ㄱㄱ";
  }

  /**
   * PUT : 리소스 갱신, 리소스 작성
   * 멱등성 보장, 안정성 보장x
   * @return
   */
  //리소스 갱신
  @PutMapping(value = "/put/list/item5")
  public HttpStatus put(@RequestBody SampleRequest request) {
    LOG.info("item5 의 정보를 Update");
    return HttpStatus.NO_CONTENT; //바디에 아무것도 없으므로 no_content : ok로 해도 상관없다
  }
  //리소스 작성
  @PutMapping(value = "/put/{newItem}", consumes = "text/plain; charset=utf-8", produces = "text/plain; charset=utf-8")
  public HttpStatus put1(@RequestBody SampleRequest request,
                         @PathVariable("newItem") String newItem) {
    LOG.info("newItem 리소스 추가/갱신");
    boolean isDataExist = true;
    if (isDataExist) { //기존에 리소스가 존재하면
      return HttpStatus.OK; // 리소스 갱신
    } else {
      return HttpStatus.CREATED; // 없으면 리소스 추가 : POST 의 `서브 리소스 추가`와 다르게 Location header 로 추가할 필요는 없다.
    }
  }

  /**
   * 리소스 작성시 PUT, POST 구별
   * 보통 POST로 한다.
   * POST 로 리소스 추가 : uri 의 결정권이 서버에 있을때 (item 추가했는데 item5 롤 결정)
   * PUT 로 리소스 추가 : uri 를 클라에서 결정 (newItem이라는 uri 를 client 에서 만들었다.)
   *  - 특별 : wiki처럼 유저가 게시판 명을 작성하고 그걸 바탕으로 uri를 생성 : 이때는 중복 체크를 위해 put 을 쏘기 전에 get을 요청할 필요가 있다.
   */



  /**
   * DELETE : 리소스 삭제
   * 멱등성 보장, 안정성 보장x
   * @return
   */
  @DeleteMapping(value = "/delete/{itemNum}")
  public HttpStatus delete(@PathVariable("itemNum") long itemNum) {
    LOG.info("{}의 item 삭제", itemNum);
    boolean isItemExist = false;
    if (isItemExist) {
      return HttpStatus.OK; //or No_CONTENT
    } else {
      return HttpStatus.NOT_FOUND; // for 멱등성 보장
    }

  }

  /**
   * 리소스의 헤더 획득
   * 멱등성 보장, 안정성 보장
   * @return
   */
  //get 이랑 유사하나 header 만 가져오는 method : 네트워크 대역을 절약
  @RequestMapping(value = "/head", method = RequestMethod.HEAD)
  public HttpStatus head() {
    return HttpStatus.OK;
  }

  /**
   * 리소스가 지원하는 Method들 획득
   * @return
   */
  @RequestMapping(value = "/options", method = RequestMethod.OPTIONS)
  public HttpStatus options() {
    LOG.info("response Header 에 Allow 가 추가되어 해당 api 에서 수신이 가능한 method 들이 들어온다."); //주의 : OPTION 자체는 Allow 헤더에 포함되지 않는다.
    return HttpStatus.OK;
  }


  /**
   * POST 로 DELETE, PUT 하기
   * request header 에
   * X-HTTP-Method-Override : PUT
   * 를 붙이면 PUT method 로 override 된다.
   *
   * 조건부 요청
   * 이 시간 이후 갱신되어 있으면 좋겠다
   * GET / If-Modified-Since : ~
   * 이시간 이후로 갱신되어 있지 않으면 갱신한다.
   * POST / If-Unmodified-Since : ~
   */



  /**
   * WIP : request, response Logging method : 미완
   * @param request
   * @param response
   */
  private void makeLog(HttpServletRequest request, HttpServletResponse response) {
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
    
    LOG.info("request : {}", requestWrapper.toString());
    LOG.info("response : {}", responseWrapper.toString());
  }
}
