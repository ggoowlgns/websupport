package com.jhpark.websupport.payload.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class PerspectiveLockRequest {
  String scope;
  long timeout;
}
