package com.sch.sch_elasticsearch.domain.shared_query;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * 여러 도메인에서 공통으로 사용하는 NativeSearchQuery를 관리합니다.
 */
@Service
public class ToolsForQuery {

    /**
     * match 쿼리 생성
     * @param fieldType
     * @param inputString
     * @param maxResults
     * @return
     */
    public NativeSearchQuery matchQuery(String fieldType, String inputString, int maxResults) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(fieldType, inputString))
                .withPageable(PageRequest.of(0, maxResults))
                .build();
        return searchQuery;
    }

    /**
     * term 쿼리 생성
     * @param fieldType
     * @param inputString
     * @param maxResults
     * @return
     */
    public NativeSearchQuery termQuery(String fieldType, String inputString, int maxResults) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery(fieldType, inputString))
                .withPageable(PageRequest.of(0, maxResults))
                .build();
        return searchQuery;
    }

    /**
     * Fuzzy 네이티브 쿼리 생성
     * @param fieldType
     * @param inputString
     * @param fuzziness
     * @param maxResults
     * @return
     */
    public NativeSearchQuery fuzzyQuery(String fieldType, String inputString, int fuzziness, int maxResults) {
        Fuzziness fuzzinessLevel;
        if (fuzziness > 0) {
            fuzzinessLevel = Fuzziness.build(fuzziness);
        } else {
            fuzzinessLevel = Fuzziness.AUTO;
        }
        QueryBuilder fuzzyBuilder = new FuzzyQueryBuilder(fieldType, inputString)
                .fuzziness(fuzzinessLevel);
        //NativeQuery 생성
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(fuzzyBuilder)
                .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                .build();

        return searchQuery;
    }

}
