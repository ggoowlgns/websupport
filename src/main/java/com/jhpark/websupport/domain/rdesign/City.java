package com.jhpark.websupport.domain.rdesign;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.List;

@Data
public class City {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private @NonNull long id;
  private String name;
  private String readInfo;

  @ManyToOne
  @JoinColumn(name = "preId", nullable = false)
  private @NonNull Prefecture prefecture;

  @OneToMany
  @JoinColumn(name = "townId", nullable = false)
  private List<Town> towns;
}
