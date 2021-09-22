package com.jhpark.websupport.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@ToString
public class ZipCodeResponse {
  private long zipCode;
  private Map<String, Object> address;
  private Map<String, Object> yomi;

}
