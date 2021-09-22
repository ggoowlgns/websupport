package com.jhpark.websupport.payload.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@ToString
public class ZipCodeRequest {
//  @NotEmpty
//  private String type; // String must be non-null and contain at least one character
//  @NotNull
//  private String payload; // fails on null but not on ""
  private long zipCode; // allows null or "" or any value
  private Map<String, Object> address;
  private Map<String, Object> yomi;

}
