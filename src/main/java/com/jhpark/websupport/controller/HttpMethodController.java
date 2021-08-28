package com.jhpark.websupport.controller;

import com.jhpark.websupport.request.SampleRequest;
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
   *
   * 멱등성 : 계속 반복해서 요청해도 똑같은 상황 보장
   * 안정성 :
   */

  /**
   * GET : 리소스 취득 -
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
   * @return
   */
  @PutMapping(value = "/put")
  public String put(@RequestBody SampleRequest request) {
    return "";
  }

  /**
   * DELETE : 리소스 삭제
   * @return
   */
  @DeleteMapping(value = "/delete")
  public String delete() {
    return "";
  }

  /**
   * 리소스의 헤더 획득
   * @return
   */
  @RequestMapping(value = "/head", method = RequestMethod.HEAD)
  public String head() {
    return "";
  }

  /**
   * 리소스가 지원하는 Method들 획득
   * @return
   */
  @RequestMapping(value = "/options", method = RequestMethod.OPTIONS)
  public String options() {
    return "";
  }


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
