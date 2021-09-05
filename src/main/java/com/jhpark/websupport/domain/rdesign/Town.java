package com.jhpark.websupport.domain.rdesign;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.List;

@Data
public class Town {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private @NonNull long id;
  private String name;
  private String readInfo;
  
  @ManyToOne
  @JoinColumn(name = "cityId", nullable = false)
  private @NonNull City city;


  /**
   * Town 안에는 수많은 ZipCode가 존재할테니까
   */
  @OneToMany
  @JoinColumn(name = "zipCodeId", nullable = false)
  private List<ZipCode> zipCodes;
}
