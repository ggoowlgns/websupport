package com.jhpark.websupport.dao.rdesign;

import com.jhpark.websupport.domain.rdesign.Prefecture;
import com.jhpark.websupport.domain.rdesign.ZipCode;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RDesignDao {

  public List<ZipCode> searchZipCode(String query) {
    return new ArrayList<>();
  }

  public List<ZipCode> findByAddress(String query) {
    return new ArrayList<>();
  }
  public List<ZipCode> findByZipCode(String query) {
    return new ArrayList<>();
  }

  public List<Prefecture> getPrefectureList() {
    return new ArrayList<>();
  }
}
