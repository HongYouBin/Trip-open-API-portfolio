package com.sch.sch_elasticsearch.domain.restaurant.contorller;

import com.sch.sch_elasticsearch.aop.SaveLogging;
import com.sch.sch_elasticsearch.domain.restaurant.dto.ResponseRestaurantDto;
import com.sch.sch_elasticsearch.domain.restaurant.service.RestaurantServiceBasic;
import com.sch.sch_elasticsearch.domain.restaurant.service.RestaurantTitleService;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 제목 검색과 관련된 기능 제공 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/restaurant")
public class RestaurantTitleController {
    private final RestaurantTitleService restaurantTitleService;
    private final RestaurantServiceBasic restaurantServiceBasic;

    //정확한 음식점명의 결과 조회
    @GetMapping("/exact-title")
    @SaveLogging
    public List<ResponseRestaurantDto> searchExactRestaurantName(
            @RequestParam("restaurantName") String restaurantName,
            @RequestParam("maxResults") int maxResults) {
        return restaurantServiceBasic.searchExactRestaurantName(restaurantName, maxResults);
    }

    //Fuzzy 제목 검색
    @SaveLogging
    @GetMapping("/title/fuzzy")
    public List<ResponseRestaurantDto> searchTitleFuzzy(@RequestParam("title") String title,
                                                        @RequestParam("maxResults") int maxResults,
                                                        @RequestParam("fuzziness") int fuzziness)
    {
        return restaurantTitleService.searchTitleUseFuzzyDto(title, maxResults, fuzziness);
    }

    //ngram 제목 검색
    @SaveLogging
    @GetMapping("/title/ngram")
    public List<ResponseRestaurantDto> searchTitleNgram(@RequestParam("title") String title,
                                                        @RequestParam("maxResults") int maxResults)
    {
        return restaurantTitleService.searchTitleUseNgramDto(title, maxResults);
    }

    //통합 제목 검색 : 제목 일치 or (Fuzzy + ngram)
    @SaveLogging
    @GetMapping("/title/aggregate-search")
    public List<ResponseRestaurantDto> searchTitleComprehensive(@RequestParam("title") String title,
                                                                @RequestParam("maxResults") int maxResults,
                                                                @RequestParam("fuzziness") int fuzziness,
                                                                @RequestParam("fuzzyPrimary") boolean fuzzyPrimary)
    {
        try {
            List<ResponseRestaurantDto> responseRestaurantDtoList = restaurantServiceBasic.searchExactRestaurantName(title, maxResults);
            if (responseRestaurantDtoList.size() != 0) {
                return responseRestaurantDtoList;
            } //1. 일치 제목 검색이 있다면 이를 리스트에 추가 후 리턴
            return restaurantTitleService.searchFuzzyAndNgram(title, maxResults, fuzziness, fuzzyPrimary); //2. 결과가 없다면 두 검색 진행 후 유사도별로 정렬
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_AGGREGATE_TITLE_SEARCH_FAIL);
        }
    }


}
