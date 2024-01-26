package com.sch.sch_elasticsearch.domain.restaurant.contorller;

import com.sch.sch_elasticsearch.aop.SaveLogging;
import com.sch.sch_elasticsearch.domain.restaurant.dto.ResponseRestaurantDto;
import com.sch.sch_elasticsearch.domain.restaurant.service.RestaurantServiceBasic;
import com.sch.sch_elasticsearch.domain.restaurant.service.RestaurantServiceExtend;
import com.sch.sch_elasticsearch.domain.wiki.dto.ResponseWikiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 특정 컬럼이나 필드 검색 결과를 조회하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantServiceExtend restaurantServiceExtend;
    private final RestaurantServiceBasic restaurantServiceBasic;


    //특정 필드의 match 쿼리 조회
    @SaveLogging
    @GetMapping("/partial")
    public List<ResponseRestaurantDto> searchPartial(@RequestParam("typeNum") int typeNum,
                                                     @RequestParam("inputString") String inputString,
                                                     @RequestParam("maxResults") int maxResults) {
        return restaurantServiceBasic.searchPartial(typeNum, inputString, maxResults);
    }

    //특정 필드의 Fuzzy 쿼리 조회
    @SaveLogging
    @GetMapping("/fuzzy")
    public List<ResponseRestaurantDto> searchFuzzy(@RequestParam("typeNum") int typeNum,
                                             @RequestParam("inputString") String inputString,
                                             @RequestParam("maxResults") int maxResults,
                                             @RequestParam("fuzziness") int fuzziness) {
        return restaurantServiceExtend.fuzzinessSearch(typeNum, inputString, maxResults, fuzziness);
    }

    //통합 내용 검색(전문 검색) - 간단 버전
    @SaveLogging
    @GetMapping("/search")
    public List<ResponseRestaurantDto> searchAll(@RequestParam("inputString") String inputString,
                                                 @RequestParam("maxResults") int maxResults) {
        return restaurantServiceExtend.searchAll(inputString, maxResults);
    }

    //통합 내용 검색(전문 검색) - 복잡한 버전 : 별점, 점수의 정량 지표 추가, includeZero의 경우 둘 중 하나의 파라미터가 0일 경우 포함
    @SaveLogging
    @GetMapping("/search/score")
    public List<ResponseRestaurantDto> searchAllExtendScore(@RequestParam("inputString") String inputString,
                                                 @RequestParam("maxResults") int maxResults,
                                                       @RequestParam("includeZero") boolean includeZero,
                                                       @RequestParam("starScore") float starScore,
                                                       @RequestParam("foodScore") int foodScore) {
        return restaurantServiceExtend.searchAllExtendScore(inputString, maxResults, includeZero, starScore, foodScore);
    }

    //현재 위치에서 Nkm 이내의 통합 검색
    @SaveLogging
    @GetMapping("/search/distance")
    public List<ResponseRestaurantDto> searchAllExtendDistance(@RequestParam("inputString") String inputString,
                                                                       @RequestParam("maxResults") int maxResults,
                                                                       @RequestParam("lat") float lat,
                                                                       @RequestParam("lng") float lng,
                                                                       @RequestParam("kilo") int kilo) {
        return restaurantServiceExtend.searchAllExtendDistance(inputString, maxResults, lat, lng, kilo);
    }

    //현재 위치에서 Nkm 이내, 별점의 통합 내용 검색
    @SaveLogging
    @GetMapping("/search/score/distance")
    public List<ResponseRestaurantDto> searchAllExtendScoreAndDistance(@RequestParam("inputString") String inputString,
                                                            @RequestParam("maxResults") int maxResults,
                                                            @RequestParam("includeZero") boolean includeZero,
                                                            @RequestParam("starScore") float starScore,
                                                            @RequestParam("foodScore") int foodScore,
                                                            @RequestParam("lat") float lat,
                                                            @RequestParam("lng") float lng,
                                                            @RequestParam("kilo") int km) {
        return restaurantServiceExtend.searchAllExtendScoreAndDistance(inputString, maxResults, includeZero, starScore, foodScore, lat, lng, km);
    }
}
