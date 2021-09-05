package com.jhpark.websupport.domain.rdesign;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Data
public class Prefecture {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private @NonNull long id;
  private String name;
  private String readingInfo;

  @OneToMany
  @JoinColumn(name = "cityId", nullable = false)
  private List<City> cities;

}
