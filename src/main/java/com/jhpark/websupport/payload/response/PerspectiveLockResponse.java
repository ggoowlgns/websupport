package com.jhpark.websupport.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@RequiredArgsConstructor
@ToString
public class PerspectiveLockResponse {
  String lockType;
  Date timeout;
  String owner;

  public PerspectiveLockResponse(String lockType) {
    this.lockType = lockType;
  }
}
