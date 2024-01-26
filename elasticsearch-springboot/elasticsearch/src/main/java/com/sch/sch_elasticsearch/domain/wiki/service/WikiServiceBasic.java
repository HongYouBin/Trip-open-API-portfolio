package com.sch.sch_elasticsearch.domain.wiki.service;

import com.sch.sch_elasticsearch.domain.shared_query.ToolsForQuery;
import com.sch.sch_elasticsearch.domain.wiki.dto.ResponseWikiDto;
import com.sch.sch_elasticsearch.domain.wiki.entity.Wiki;
import com.sch.sch_elasticsearch.domain.wiki.repository.WikiRepository;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 간단한 일치 쿼리 등을 조회합니다.
 */
@Service
@Slf4j
public class WikiServiceBasic {
    private final ElasticsearchOperations elasticsearchOperations;
    private final ToolsForWikiService toolsForWikiService;
    private final WikiRepository wikiRepository;
    private final ToolsForQuery toolsForQuery;

    public WikiServiceBasic(@Qualifier("ElasticsearchOperationsBean") ElasticsearchOperations elasticsearchOperations,
                            ToolsForWikiService toolsForWikiService, WikiRepository wikiRepository, ToolsForQuery toolsForQuery) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.toolsForWikiService = toolsForWikiService;
        this.wikiRepository = wikiRepository;
        this.toolsForQuery = toolsForQuery;
    }

    /**
     * 정확한 keyword 검색용 쿼리문 : attraction_name, wiki_title, content_id
     * @param typeNum (타입 넘버)
     * @param inputString (찾을 검색어)
     * @param reliable 신뢰성 검색 사용 유무
     * @return List<ResponseWikiDto> 결과값
     */
    public List<ResponseWikiDto> searchExact(int typeNum, String inputString, boolean reliable, int maxResults) {
        try {
            String type = toolsForWikiService.getType(typeNum);
            NativeSearchQuery searchQuery = toolsForQuery.termQuery(type, inputString, maxResults);

            return toolsForWikiService.getListBySearchHits(elasticsearchOperations.search(searchQuery, Wiki.class), reliable)
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.info("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.WIKI_SEARCH_EXACT_FAIL);
        }
    }


    /**
     * 입력 파라미터의 부분 검색(전문 검색) 수행 (attraction_name), (wiki_content)
     * @param typeNum (타입 넘버)
     * @param inputString (찾을 검색어)
     * @param reliable 신뢰성 검색 사용 유무
     * @return List<ResponseWikiDto> 결과값
     */
    public List<ResponseWikiDto> searchPartial(int typeNum, String inputString, boolean reliable, int maxResults) {
        try {
            String type = toolsForWikiService.getType(typeNum);
            NativeSearchQuery searchQuery = toolsForQuery.matchQuery(type, inputString, maxResults);

            // Elasticsearch에서 쿼리 실행 후 결과값 가져오기
            return toolsForWikiService.getListBySearchHits(elasticsearchOperations.search(searchQuery, Wiki.class), reliable)
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.info("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.WIKI_SEARCH_PARTIAL_FAIL);
        }
    }


    public void insertWiki(Wiki wiki) {
        try {
            wikiRepository.save(wiki);
        } catch (Exception e) {
            log.info("[ERR LOG] {}", e.getMessage());
            throw new CommonException(ExceptionType.WIKI_INSERT_FAIL);
        }
    }


}
