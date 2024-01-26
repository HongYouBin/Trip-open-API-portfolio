package com.sch.sch_elasticsearch.domain.restaurant.service;

import com.sch.sch_elasticsearch.domain.restaurant.dto.ResponseRestaurantDto;
import com.sch.sch_elasticsearch.domain.restaurant.entity.Restaurant;
import com.sch.sch_elasticsearch.domain.shared_query.ToolsForQuery;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestaurantServiceExtend {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ToolsForRestauantService toolsForRestauantService;
    private final ToolsForQuery toolsForQuery;

    public List<ResponseRestaurantDto> fuzzinessSearch(int typeNum, String inputString, int maxResults, int fuzziness) {
        try {
            String type = toolsForRestauantService.getType(typeNum);
            NativeSearchQuery searchQuery = toolsForQuery.fuzzyQuery(type, inputString, fuzziness, maxResults);
            return toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.info("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_FUZZINESS_SEARCH_FAIL);
        }
    }

    public List<ResponseRestaurantDto> searchAll(String inputString, int maxResults) {
        try {
            QueryBuilder multiMatchQuery = getMultiMatchQuery(inputString);
            //NativeQuery 생성
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(multiMatchQuery)
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .build();
            return toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_ALL_BY_PARAM_FAIL);
        }
    }

    /**
     * 가장 연관관계가 있으며, 일정 기준점 스코어 이상인 값들을 리턴. 0 포함 여부도 조회
     * @param inputString
     * @param maxResults
     * @param includeZero
     * @param starScore
     * @param foodScore
     * @return
     */
    public List<ResponseRestaurantDto> searchAllExtendScore(String inputString, int maxResults, boolean includeZero, float starScore, int foodScore) {
        try {
            //multiMatch
            QueryBuilder multiMatchQuery = getMultiMatchQuery(inputString);

            // Bool 쿼리 생성
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.should(multiMatchQuery); // 기본 MultiMatchQuery

            BoolQueryBuilder finalBoolQueryBuilder = addBoolQueryBuilderScoreConditions(boolQueryBuilder, includeZero, starScore,foodScore);

            // NativeQuery 생성
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(finalBoolQueryBuilder)
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .build();
            return toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_ALL_EXTEND_SCORE_FAIL);
        }

    }

    /**
     * 가장 연관관계가 있으며, 일정 거리 이내인 값들을 리턴.
     * @param inputString
     * @param maxResults
     * @param lat
     * @param lng
     * @param kilo
     * @return
     */
    public List<ResponseRestaurantDto> searchAllExtendDistance(String inputString, int maxResults, float lat, float lng, int kilo) {
        try {

            QueryBuilder multiMatchQuery = getMultiMatchQuery(inputString); //MultiMatch로직 모듈화
            BoolQueryBuilder boolQueryBuilder = addBoolQueryBuilderDistanceConditions(lat, lng, kilo); //거리 계산 boolQuery must 로직
            boolQueryBuilder.should(multiMatchQuery); // 기본 MultiMatchQuery

            // NativeQuery 생성
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQueryBuilder)
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .build();

            return toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));

        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_ALL_EXTEND_DISTANCE_FAIL);
        }
   }


    /**
     * 가장 연관관계가 있으며, 일정 기준점 스코어 이상인 값, 일정 거리 이내인 값들을 리턴. 0 포함 여부도 조회
     * @param inputString
     * @param maxResults
     * @param includeZero
     * @param starScore
     * @param foodScore
     * @param lat
     * @param lng
     * @param kilo
     * @return
     */
    public List<ResponseRestaurantDto> searchAllExtendScoreAndDistance(String inputString, int maxResults, boolean includeZero, float starScore, int foodScore, float lat, float lng, int kilo) {
        try {
            QueryBuilder multiMatchQuery = getMultiMatchQuery(inputString); //MultiMatch로직 모듈화
            BoolQueryBuilder boolQueryBuilder = addBoolQueryBuilderDistanceConditions(lat, lng, kilo); //거리 계산 boolQuery must 로직
            boolQueryBuilder.should(multiMatchQuery); // 기본 MultiMatchQuery

            BoolQueryBuilder finalBoolQueryBuilder = addBoolQueryBuilderScoreConditions(boolQueryBuilder, includeZero, starScore,foodScore); //스코어 제한 로직 모듈화 적용

            // NativeQuery 생성
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(finalBoolQueryBuilder)
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .build();
            return toolsForRestauantService.getListBySearchHits(elasticsearchRestTemplate.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_ALL_EXTEND_SCORE_AND_DISTANCE_FAIL);
        }
    }

    //MultiMatch 내부 모듈화
    private QueryBuilder getMultiMatchQuery(String inputString) {
        QueryBuilder multiMatchQuery = new MultiMatchQueryBuilder(
                inputString, // 검색어
                "food_type_main", "food_type_sub", "food_name"
        )
                .field("food_type_main", 3)
                .field("food_type_sub", 2)
                .field("food_name", 4);
        return multiMatchQuery;
    }

    //score 관련 must-should 구문 내부 모듈화
    private BoolQueryBuilder addBoolQueryBuilderScoreConditions(BoolQueryBuilder boolQueryBuilder, boolean includeZero, float starScore, int foodScore) {
        // star_score와 food_score 조건
        QueryBuilder starScoreQuery = QueryBuilders.rangeQuery("food_star").gte(starScore);
        QueryBuilder foodScoreQuery = QueryBuilders.rangeQuery("food_score").gte(foodScore);

        if(includeZero) { //0.0인 값 포함
            // food_star가 지정값 이상이고 food_score가 0인 경우
            BoolQueryBuilder condition1 = QueryBuilders.boolQuery()
                    .must(QueryBuilders.rangeQuery("food_star").gte(starScore))
                    .must(QueryBuilders.termQuery("food_score", 0));

            // food_score가 지정값 이상이고 food_star가 0인 경우
            BoolQueryBuilder condition2 = QueryBuilders.boolQuery()
                    .must(QueryBuilders.rangeQuery("food_score").gte(foodScore))
                    .must(QueryBuilders.termQuery("food_star", 0));

            boolQueryBuilder.should(condition1);
            boolQueryBuilder.should(condition2);
            boolQueryBuilder.minimumShouldMatch(1); // 최소 한 개의 should 조건이 매치되도록 설정
        } else {
            boolQueryBuilder.must(starScoreQuery); // star_score 조건
            boolQueryBuilder.must(foodScoreQuery); // food_score 조건
        }

        return boolQueryBuilder;
    }

    //distance 계산 관련 bool쿼리 내부 모듈화
    private BoolQueryBuilder addBoolQueryBuilderDistanceConditions(float lat, float lng, int kilo) {
        double limitDist[][] = ToolsForRestauantService.calculateLatLonRange(lat, lng, kilo); //최저 최고 거리 계산

        QueryBuilder lngRangeQuery = QueryBuilders.rangeQuery("food_longitude").gte(limitDist[0][1]).lte(limitDist[1][1]);
        QueryBuilder latRangeQuery = QueryBuilders.rangeQuery("food_latitude").gte(limitDist[0][0]).lte(limitDist[1][0]);

        // Bool 쿼리 생성
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(lngRangeQuery);
        boolQueryBuilder.must(latRangeQuery);
        return boolQueryBuilder;
    }
}
