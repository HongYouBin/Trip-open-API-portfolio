package com.sch.sch_elasticsearch.domain.restaurant.service;

import com.fasterxml.jackson.databind.ser.std.ToEmptyObjectSerializer;
import com.sch.sch_elasticsearch.domain.restaurant.dto.ResponseRestaurantDto;
import com.sch.sch_elasticsearch.domain.restaurant.entity.Restaurant;
import com.sch.sch_elasticsearch.domain.shared_query.ToolsForQuery;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sch.sch_elasticsearch.exception.ExceptionType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantTitleService {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ToolsForQuery toolsForQuery;
    private final ToolsForRestauantService toolsForRestauantService;

    @Value("${info.analyzer.nori-ngram}")
    String ngramAnalyzer;

    /**
     * Fuzzy를 사용한 유사도 검색
     * @param title
     * @param maxResults
     * @param fuzziness
     * @return
     */
    public List<ResponseRestaurantDto> searchTitleUseFuzzyDto(String title, int maxResults, int fuzziness)
    {
        try {
            NativeSearchQuery searchQuery = toolsForQuery.fuzzyQuery("food_name.keyword", title, fuzziness, maxResults);
            return toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_FUZZY_TITLE_FAIL);
        }
    }

    /** Ngram 방식을 사용한 제목 유사도 검색
     *
     * @param title
     * @param maxResults
     * @return
     */
    public List<ResponseRestaurantDto> searchTitleUseNgramDto(String title, int maxResults) {
        try {
            QueryBuilder queryBuilder = new QueryStringQueryBuilder(title)
                    .defaultField("food_name")
                    .analyzer(ngramAnalyzer);
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(queryBuilder)
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .build();

            return toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_NGRAM_TITLE_FAIL);
        }
    }

    /**
     * fuzzy와 ngram을 동시에 사용하여 가장 유사한 결과를 찾아내는 로직. 가중치를 설정 가능하다.
     * @param title
     * @param maxResults
     * @param fuzziness
     * @param fuzzyPrimary fuzzy 우선 가중치 사용 여부
     * @return
     */
    public List<ResponseRestaurantDto> searchFuzzyAndNgram(String title, int maxResults, int fuzziness, boolean fuzzyPrimary) {
        try {
            //maxResult가 적으면 적은 List셋에 고만고만한 값만 나와 유의미한 검색 결과가 나오지 않음. 가장 큰 이슈였다.
            int searchCount = Math.max(maxResults, 40); //최소 40개로 검색 결과를 보장 : 10개 정도라면 비슷한 값만 나올 수도 있음.
            int fuzzyWeight, ngramWeight; //가중치 여부 : fuzzyPrimary에 따라 다름
            if(fuzzyPrimary) { //가중치 부여
                fuzzyWeight = 7;
                ngramWeight = 3;
            } else {
                fuzzyWeight = 3;
                ngramWeight = 7;
            }

            List<ResponseRestaurantDto> fuzzyList = searchTitleUseFuzzyDto(title, searchCount, fuzziness);
            List<ResponseRestaurantDto> ngramList = searchTitleUseNgramDto(title, searchCount);


            //두 리스트를 합치고 스코어가 있다면 가중
            HashMap<String, Float> alladdHashMap = new HashMap<>();
            //1. fuzzy 삽입
            for (ResponseRestaurantDto dto : fuzzyList) {
                alladdHashMap.put(dto.getRestaurantName(), (dto.getScore() * fuzzyWeight));
            }
            //2. ngram 삽입, fuzzy와 동일한 지명이 있다면 스코어 가중
            for (ResponseRestaurantDto dto : ngramList) {
                if(alladdHashMap.containsKey(dto.getRestaurantName())) {
                    alladdHashMap.put(dto.getRestaurantName(),
                            alladdHashMap.get(dto.getRestaurantName()) + (dto.getScore() * ngramWeight));
                } else alladdHashMap.put(dto.getRestaurantName(), (dto.getScore() * ngramWeight));
            }

            //스트림을 통해 해시맵 내림차순 정렬 후 리스트화
            List<String> nameList = alladdHashMap.entrySet()
                    .stream() //HashMap을 스트림으로 변환
                    .sorted(Map.Entry.<String, Float>comparingByValue().reversed()) //value를 기준으로 내림차순 정렬
                    .limit(maxResults)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            //추려진 값들을 should를 사용하여 동시적으로 조회 (리스트 내부에 있는 제목의 검색 결과는 리턴됨)
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String name : nameList) {
                boolQueryBuilder.should(QueryBuilders.termQuery("food_name.keyword", name));
            }
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQueryBuilder)
                    .withPageable(PageRequest.of(0, maxResults)) // maxResults만큼 결과
                    .build();

            List<ResponseRestaurantDto> queryResult = toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));

            //최종 합산 결과 저장, 이전 스코어 순서대로 재정렬
            List<ResponseRestaurantDto> results = new ArrayList<>();
            for (String name : nameList) {
                queryResult.stream()  // queryResult 리스트를 스트림으로 변환
                        .filter(dto -> dto.getRestaurantName().equals(name)) // 현재 이름과 일치하는 ResponseRestaurantDto 객체 필터링
                        .findFirst() // 필터링된 스트림에서 첫 번째 요소 찾기 (일치하는 첫 번째 객체)
                        .ifPresent(results::add); // 일치하는 객체가 존재하면 sortedResults 리스트에 추가
            }

            return results;
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_FUZZY_AND_NGRAM_TITLE_FAIL);
        }
    }
}
