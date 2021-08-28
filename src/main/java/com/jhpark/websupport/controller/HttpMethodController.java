package com.jhpark.websupport.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.xpath.XPath;

/**
 * Ch. 7 HTTP 메서드
 * HTTP Method 가 왜 단지 8개로만 운영이 가능한지.
 * 각 Method 의 존재 이유 & 용도 & 설계
 */
@Controller
@RequestMapping(value = "/method")
public class HttpMethodController {
  Logger LOG = LoggerFactory.getLogger(HttpMethodController.class);

  @GetMapping(value = "/get/{value}")
  public HttpServletResponse get(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                 @PathVariable("value") String value) {
    response.setStatus(200);
    LOG.info("session : {}", session);
    makeLog(request, response);
    return response;
  }


  private void makeLog(HttpServletRequest request, HttpServletResponse response) {
    LOG.info("request : {}", request);
    LOG.info("response : {}", response);
  }
}
