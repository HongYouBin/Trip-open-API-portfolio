package com.sch.sch_elasticsearch.domain.wiki.service;

import com.sch.sch_elasticsearch.domain.shared_query.ToolsForQuery;
import com.sch.sch_elasticsearch.domain.wiki.dto.ResponseWikiDto;
import com.sch.sch_elasticsearch.domain.wiki.dto.SearchAllDTO;
import com.sch.sch_elasticsearch.domain.wiki.dto.TermDTO;
import com.sch.sch_elasticsearch.domain.wiki.entity.Wiki;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;

import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Script 등 2차 가공 이상 복잡한 쿼리 로직의 집합입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WikiServiceExtend {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ToolsForWikiService toolsForWikiService;
    private final ToolsForQuery toolsForQuery;

    /**
     * Attraction_name과 Wiki_Title이 입력 검색어와 같은 쿼리 반환
     * @return List<ResponseWikiDto>
     */
    public List<ResponseWikiDto> getSameAttNameAndWikiTitle(String inputString, boolean reliable) {
        try {

            // Script 문자열을 정의한다.
            String scriptStr = "if (doc['attraction_name.keyword'].size() != 0 && doc['wiki_title.keyword'].size() != 0) { " +
                    "String attName = doc['attraction_name.keyword'].value.replace(' ', ''); " +
                    "String wikiTitle = doc['wiki_title.keyword'].value.replace(' ', ''); " +
                    "return attName.equals(wikiTitle) && attName.equals(params.inputString); " +
                    "} return false;";

            // 파라미터 맵을 생성하고 inputString을 추가한다. painless에서는 이렇게 param을 통해 값을 넣어야한다. (pstmt를 지원하지 않는다..)
            Map<String, Object> params = new HashMap<>();
            params.put("inputString", inputString);

            // Script 객체를 생성한다. (params를 넘겨줘야 해서 인자를 변경해줄 것)
            Script script = new Script(ScriptType.INLINE, "painless", scriptStr, params);

            // 스크립트 쿼리 빌더를 생성한다.
            QueryBuilder scriptQueryBuilder = QueryBuilders.scriptQuery(script);

            // Elasticsearch의 bool 쿼리를 생성한다.
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(scriptQueryBuilder);

            // NativeSearchQuery를 생성한다.
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQueryBuilder)
                    .withSourceFilter(new FetchSourceFilter(new String[]{"pk_id","attraction_name", "content_id", "wiki_title", "wiki_content"}, null)) // 필요한 필드만 가져오기 위한 설정
                    .build();

            // Elasticsearch에서 쿼리 실행 후 결과값 가져오기
            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            return toolsForWikiService.getListBySearchHits(searchHits, reliable)
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CommonException(ExceptionType.WIKI_GET_SAME_ATTNAME_AND_WIKITITLE_FAIL);
        }
    }

    /**
     * fuzziness 유사도 검색
     * @param inputString
     * @param typeNum
     * @param fuzziness
     * @return List<ResponseWikiDto>
     */
    public List<ResponseWikiDto> fuzzinessSearch(int typeNum, String inputString, boolean reliable, int maxResults, int fuzziness) {
        try {
            String type = toolsForWikiService.getType(typeNum);
            NativeSearchQuery searchQuery = toolsForQuery.fuzzyQuery(type, inputString, fuzziness, maxResults);

            // Elasticsearch에서 쿼리 실행 후 결과값 가져오기
            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            return toolsForWikiService.getListBySearchHits(searchHits, reliable)
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e.getMessage());
            throw new CommonException(ExceptionType.WIKI_FUZZINESS_SEARCH_FAIL);
        }

    }

    /**
     * DTO 입력을 토대로 가장 유사도가 높은 내용을 검색하여 반환한다.
     * 입력 파라미터로 검색 문장, 가중치, 반환 개수를 가진다.
     * @param searchAllDTO
     * @return List<ResponseWikiDto>
     */
    public List<ResponseWikiDto> searchAll(SearchAllDTO searchAllDTO) {
        try {
            QueryBuilder multiyMatchQuery = new MultiMatchQueryBuilder(
                    searchAllDTO.getSearchContent(), // 검색어
                    "attraction_name", "overview", "wiki_title", "wiki_content"
            )
                    .field("attraction_name", searchAllDTO.getAttractionNameCorrectionFactor())
                    .field("overview", searchAllDTO.getOverviewCorrectionFactor())
                    .field("wiki_title", searchAllDTO.getWikiTitleCorrectionFactor())
                    .field("wiki_content", searchAllDTO.getWikiContentCorrectionFactor());
            //NativeQuery 생성
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(multiyMatchQuery)
                    .withCollapseField("content_id")
                    .withPageable(PageRequest.of(0, searchAllDTO.getMaxResults())) // 결과 개수 제한
                    .build();

            // Elasticsearch에서 쿼리 실행 후 결과값 가져오기
            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            return toolsForWikiService.getListBySearchHits(searchHits, searchAllDTO.isUseReliableSearch())
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("[ERR LOG]{}", e.getMessage());
            throw new CommonException(ExceptionType.WIKI_SEARCH_ALL_BY_DTO_FAIL);
        }
    }

    /**
     * 동일 메서드 Overload, 텍스트와 반환 개수만 지정
     * @param inputString, maxResults, useReliableSearch
     * @return searchAll
     */
    public List<ResponseWikiDto> searchAll(String inputString, int maxResults, boolean reliable) {
        try {
            QueryBuilder multiyMatchQuery = new MultiMatchQueryBuilder(
                    inputString, // 검색어
                    "attraction_name", "overview", "wiki_title", "wiki_content"
            )
                    .field("attraction_name", 4)
                    .field("overview", 3)
                    .field("wiki_title", 2)
                    .field("wiki_content", 1);
            //NativeQuery 생성
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(multiyMatchQuery)
                    .withCollapseField("content_id")
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .build();

            // Elasticsearch에서 쿼리 실행 후 결과값 가져오기
            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            return toolsForWikiService.getListBySearchHits(searchHits, reliable)
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e.getMessage());
            throw new CommonException(ExceptionType.WIKI_SEARCH_ALL_BY_PARAM_FAIL);
        }
    }

}
