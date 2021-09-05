package com.jhpark.websupport.domain.rdesign;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Data
public class ZipCode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private @NonNull long id;
  private String zipCode;

  @ManyToOne
  @JoinColumn(name = "preId", nullable = false)
  private Prefecture prefecture;

  @ManyToOne
  @JoinColumn(name = "cityId", nullable = false)
  private City city;

  private String realName;
  private String realNameInfo;

}
