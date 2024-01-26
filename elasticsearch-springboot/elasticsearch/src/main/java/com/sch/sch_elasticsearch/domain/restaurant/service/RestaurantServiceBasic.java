package com.sch.sch_elasticsearch.domain.restaurant.service;

import com.sch.sch_elasticsearch.domain.restaurant.dto.ResponseRestaurantDto;
import com.sch.sch_elasticsearch.domain.restaurant.entity.Restaurant;
import com.sch.sch_elasticsearch.domain.shared_query.ToolsForQuery;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RestaurantServiceBasic {

    private ElasticsearchOperations elasticsearchOperations;
    private ToolsForRestauantService toolsForRestauantService;
    private ToolsForQuery toolsForQuery;

    public RestaurantServiceBasic(@Qualifier("ElasticsearchOperationsBean") ElasticsearchOperations elasticsearchOperations, ToolsForRestauantService toolsForRestauantService, ToolsForQuery toolsForQuery) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.toolsForRestauantService = toolsForRestauantService;
        this.toolsForQuery = toolsForQuery;
    }

    /**
     * 정확한 음식점명을 입력받고, 정보를 조회한다.
     * @param restaurantName
     * @param maxResults
     * @return
     */
    public List<ResponseRestaurantDto> searchExactRestaurantName(String restaurantName, int maxResults) {
        try {
            NativeSearchQuery searchQuery = toolsForQuery.termQuery("food_name.keyword", restaurantName ,maxResults);
            return toolsForRestauantService.getListBySearchHits(elasticsearchOperations.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.info("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_EXACT_RESTAURANT_NAME_FAIL);
        }
    }

    /**
     * 입력 파라미터의 부분 검색(match) 수행
     * @param typeNum
     * @param inputString
     * @param maxResults
     * @return
     */
    public List<ResponseRestaurantDto> searchPartial(int typeNum, String inputString, int maxResults) {
        try {
            String type = toolsForRestauantService.getType(typeNum);
            NativeSearchQuery searchQuery = toolsForQuery.matchQuery(type, inputString, maxResults);
            return toolsForRestauantService.getListBySearchHits(elasticsearchOperations.search(searchQuery, Restaurant.class));
        } catch (Exception e) {
            log.info("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.RESTAURANT_SEARCH_PARTIAL_FAIL);
        }
    }

}
