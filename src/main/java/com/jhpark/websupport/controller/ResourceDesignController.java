package com.jhpark.websupport.controller;

import com.jhpark.websupport.dao.rdesign.RDesignDao;
import com.jhpark.websupport.domain.rdesign.ZipCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Ch.17 리소스의 설계
 * % RESTful 에서 권장하는 아키텍쳐의 설계 방법은 모순이 존재한다.
 *  - 1.웹서비스에서 제공할 데이터를 특정, 2. 데이터를 리소스로 나누는 방법 : 이 두가지를 알 수 없기 때문
 *  
 * % 리소스를 설계하는 정해진 방법은 없지만 `어느정도 확립되어 있는 기존의 설계방법` 에서 얻어진 결과를 바탕으로 리소스를 설계하는 방법을 제시한다.
 *  a. ERD
 *  b. OOP Model의 Class Diagram
 *  c. 정보 아키텍쳐
 *
 *
 * 정리 : ERD 설계부터 하고 -> 객체를 어떻게 Mapping 할지 고민 & 설계 ->
 */

@RestController
@RequestMapping(path = "/rdesign")
public class ResourceDesignController {

  private final RDesignDao rDesignDao;

  public ResourceDesignController(@Autowired RDesignDao rDesignDao) {
    this.rDesignDao = rDesignDao;
  }

  /**
   * ERD 를 활용한 리소스 설계
   *  - 중심이 되는 table 가져오는 리소스
   * @param id
   * @return
   */
  @RequestMapping(path = "/zipCode/{id}", method = RequestMethod.GET, produces = "application/json")
  public ZipCode getZipCode(@PathVariable("id")long id) {
    // GET ZipCode by id
    return new ZipCode(id); // Dodo, Sigu 다 들어가 있는 entity
  }

  /**
   * ERD 를 활용한 리소스 설계
   *  - 검색 결과 리소스 도출
   * @param query
   * @return
   */
  @RequestMapping(path = "/search", method = RequestMethod.GET, produces = "application/json")
  public List<ZipCode> search(@RequestParam("query")String query) {
    // 주소 전부 혹은 일부에 의한 검색
    // 우편번호 전부 혹은 일부에 의한 검색
    return rDesignDao.searchZipCode(query); // Dodo, Sigu 다 들어가 있는 entity
  }

  /**
   *
   * ERD | RDB 로 표현하기 힘든 데이터 구조
   *  - 계층 구조 (객체로 보면 보기가 쉽다. : 아래 설명)
   *  - 톱레벨 리소스 : 다른 리소스로 링크를 연결하는 바탕이 되는 리소스 .. ? :
   *
   * 리소스 간의 링크 관계에는 ERD 를 활용하자
   *
   * -ERD 마무리-
   */



  /**
   * OO Model : 성과물 : Class D. , Sequence D.
   * : OOM 은 ER 보다 더 많은 정보를 포함하고 있다. : 개별 클래스로의 조작을 나타내는 메서드도 정의 가능
   *
   * ERD 의 결과를 가지고 Class 설계를 할텐데
   *  -> 계층 구조를 잡을수 있다.
   *  Prefecture -> City -> Town -> ZipCode
   *
   *  -> but 아직 톱 레벨 리소스는 도출 할 수 없다.
   */


  /**
   * 정보 아키텍처로부터의 도출 : 정보 분류
   * `정보 아키텍처`란? : 웹은 더 복잡한 디자인이 필요해졌다.
   *    -> 이 복잡한 디자인을 `도서정보학`등의 정보 분류의 관점에서 정리하여
   *      받는 이가 정보를 쉽게 찾거나 이해하기 쉽도록 전달하기 위한 기술이다.
   *
   * 검색 방법 예시
   *  - 전국지도에서 검색
   *  - 주소로 검색
   *  - 우편번호로 검색
   *  +a : 빵 부스러기 리스트 (Home > Blog > Category ) 이렇게 타고 들어간 상황에서 Bread Crumb List 를 제공해서 User 가 언제든 다시 뒤로 돌아갈수 있게끔 설계
   *  정보 아키텍쳐는 리소스 설계와 상성이 좋다.
   *
   */


  /*******************************
   * 정리 : 리소스 설계에서 가장 중요한 것
   * - 리소스를 설계할 때는 `Web Service` 와 `Web API` 를 분리시켜서 생각하지 말아라
   ********************************/

}
