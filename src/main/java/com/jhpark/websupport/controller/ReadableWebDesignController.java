package com.jhpark.websupport.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ch 15. 읽기가능한 웹서비스 설계
 *
 * data 는 일반 현실에서 사용하는 data
 * resource : 웹상에 존재하는 이름이 부여된 정보
 * 링크 관계 : 해당 페이지에서 다른 리소스 요청하는 관계 
 *
 * 리소스 설계의 지침 : `RESTful 웹 서비스` - `리소스 지향 아키텍쳐` (ROA)
 * - 전체 -
 * 1. 웹 서비스에서 제공할 `data` 특정
 * 2. `data` 를 `resource`로 특정
 * - per resource -
 * 3. `resource` 에 URI로 이름 부여
 * 4. 클라에 제공할 `resource`의 표현을 설계 : 표현 방식 - ex) xhtml, json, csv, mpeg, pdf  등 
 * (5.FE) 링크와 폼을 이용해 리소스와 리소스를 연결
 * 6. event 의 표준적인 코스를 검토 ()
 * 7. 에러 검토
 *
 *
 * RESTful 의 성질
 * 1. * Addressability : 어드레스 가능성
 *  - URI만 있으면 리소스를 한결같이 가리킬 수 있다는 성질
 * 2. Connectedness : 접속성
 *  - 리소스를 링크로 접속하여 하나의 App으로 이룬다.
 * 3. Uniform Interface
 *  - ??
 * 4. Statelessness
 *  - * 사용 편의성이 손상된다면 최악 : 필요에 따라서 Stateful 하게 가는게 좋을 수도 있다.
 *  - coockie로 세션관리를 하면 Stateful 하다.
 */
@RestController
@RequestMapping(path = "/read")
public class ReadableWebDesignController {
  /**
   * 예시. 우편번호 검색 서비스의 설계
   * - only read : only GET
   * - 기능
   *  - 주소, 주소읽기로 우편정보를 full text searching 할 수 있다.
   */

  /**
   * 1. 제공할 데이터 특정
   *  |어떤 데이터를 제공할 것인지 이해하지 못하면 리소스를 설계할 수 없다.
   * - 예시
   *  - 7자리 우편번호
   *  - 그 우편번호가 표현하는 주소
   *  - 주소의 발음정보
   */

  /**
   * 2. data 를 resource로 특정
   * - 데이터를 리소스로 분할 : resource란? 웹상에 존재하는 이름이 부여된 정보
   * : 제공할 데이터를 가지고 최적의 `resource 분할` 과정을 진행한다.
   * 
   * - 예시
   *  - 우편번호 리소스
   *  - 검색결과 리소스
   *  - 지역 리소스
   *  - 톱 레벨 리소스 : web 의 entry point
   */

  /**
   * 3. 각 리소스에 URI 부여
   * - 예시
   *  - 우편번호 리소스 : 우편번호로 유일하게 식별이 가능
   *   - /{addressId} : /1112222 or /111-2222
   *   - % `정규 URI`는 프로그램에서 다루기 쉬운 쪽을 채택하는게 좋다.
   *    - `-`를 붙이는 경우를 제공하면 `대리 URI` 로  `정규 URI`로 301 redirect 하자
   *    
   *  - 검색결과 리소스
   *    - /search?q={query} : 검색할 대상을 query param 으로 받자
   *    
   *  - 지역 리소스
   *    - /{도도부현 명}/{시구읍면 명}/{정역 명}
   *    - 계층구조로 path 로 분리
   *    
   *  - 톱레벨 리소스
   *   - /
   */

  /**
   * 4. 클라에 제공할 resource의 표현 설계
   * - xml
   *  - xhtml : hypermedia에서 필요한 기능을 모두 갖추고 있다. 
   *  - 브라우저로 표시 가능
   * - 경량포맷
   *  - json
   *  - yaml
   * - % 리소스의 표현은 :  request 의 Accept Header 로 지정할 수 있다. : 혹은 uri 의 끝에 .json 을 붙이는 식으로 설계도 가능
   */

  /**
   * 6. event의 표준적인 코스를 검토 : user 입장 usecase 작성
   */

  /**
   * 7. 에러 검토
   *  - 특정 상황별로 어떤 status code 를 반환할지 검토
   *  - 존재하지 않는 URI 지정
   *   - 404 not found
   *  - 필수 parameter를 지정하지 않은 경우
   *   - 400 Bad Request
   *  - 지원하지 않는 method
   *   - 405 method not allowed
   */

  /**
   * 마무리 :
   *  - 리소스 설계는 스킬이다.
   *   - 몸에 익힐 수가 있다. : 체화시키자
   */
}
