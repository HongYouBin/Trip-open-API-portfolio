package com.sch.sch_elasticsearch.domain.wiki.service;

import com.sch.sch_elasticsearch.domain.wiki.dto.ResponseWikiDto;
import com.sch.sch_elasticsearch.domain.wiki.entity.Wiki;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 제목 검색과 관련된 서비스 로직
 */
@Service
@Slf4j
public class WikiServiceTitle {
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    private ToolsForWikiService toolsForWikiService;

    public WikiServiceTitle(@Qualifier("ElasticsearchTemplateBean") ElasticsearchRestTemplate elasticsearchRestTemplate, ToolsForWikiService toolsForWikiService) {
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
        this.toolsForWikiService = toolsForWikiService;
    }

    @Value("${info.analyzer.nori-ngram}")
    String ngramAnalyzer;

    /**
     * 입력값에 존재하는 제목이 있다면 리턴,아니라면 null 값 리턴
     * @param title
     * @param reliable
     * @return
     */
    public ResponseWikiDto searchTitleCorrect(String title, boolean reliable) {
        try {
            QueryBuilder queryBuilder = new MatchQueryBuilder("attraction_name.keyword", title);
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(queryBuilder)
                    .build();

            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            List<Wiki> result = toolsForWikiService.getListBySearchHits(searchHits, reliable);
            if(result.size() == 0) return null;
            return result.get(0).toDto();
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e);
            throw new CommonException(ExceptionType.WIKI_SEARCH_CORRECT_TITLE_FAIL);
        }
    }

    /**
     * Fuzzy를 사용한 제목 유사도 검색
     * @param title
     * @param maxResults
     * @param fuzziness
     * @param reliable
     * @return
     */
    public List<ResponseWikiDto> searchTitleUseFuzzyDto(String title, int maxResults, int fuzziness, boolean reliable) {
        try {
            // fuzziness 설정
            Fuzziness fuzzinessLevel;
            if (fuzziness > 0) {
                fuzzinessLevel = Fuzziness.build(fuzziness);
            } else {
                fuzzinessLevel = Fuzziness.AUTO;
            }

            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(
                            new FuzzyQueryBuilder("attraction_name.keyword", title)
                                    .fuzziness(fuzzinessLevel)
                    )
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .withCollapseField("content_id")
                    .build();

            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            return toolsForWikiService.getListBySearchHits(searchHits, reliable)
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e);
            throw new CommonException(ExceptionType.WIKI_SEARCH_FUZZY_TITLE_FAIL);
        }
    }

    /** Ngram 방식을 사용한 제목 유사도 검색
     *
     * @param title
     * @param maxResults
     * @param reliable
     * @return
     */
    public List<ResponseWikiDto> searchTitleUseNgramDto(String title, int maxResults, boolean reliable) {
        try {
            QueryBuilder queryBuilder = new QueryStringQueryBuilder(title)
                    .defaultField("attraction_name")
                    .analyzer(ngramAnalyzer);
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(queryBuilder)
                    .withPageable(PageRequest.of(0, maxResults)) // 결과 개수 제한
                    .withCollapseField("content_id")
                    .build();

            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            return toolsForWikiService.getListBySearchHits(searchHits, reliable)
                    .stream()
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e.getMessage());
            throw new CommonException(ExceptionType.WIKI_SEARCH_NGRAM_TITLE_FAIL);
        }
    }


    /**
     * Fuzzy와 Ngram 둘 다 사용하여 집계. 가장 스코어가 높은 기준대로 리턴
     * @param title
     * @param maxResults
     * @param fuzziness
     * @param reliable
     * @return
     */
    public List<ResponseWikiDto> searchFuzzyAndNgram(String title, int maxResults, int fuzziness, boolean reliable, boolean fuzzyPrimary) {
        {
            //maxResult가 적으면 적은 List셋에 고만고만한 값만 나와 유의미한 검색 결과가 나오지 않음. 가장 큰 이슈였다.
            int searchCount = Math.max(maxResults, 40); //최소 40개로 검색 결과를 보장 : 10개 정도라면 비슷한 값만 나올 수도 있음. (최종 결과는 maxResults로 리턴)
            int fuzzyWeight, ngramWeight; //가중치 여부 : fuzzyPrimary에 따라 다름
            if(fuzzyPrimary) { //가중치 부여
                fuzzyWeight = 7;
                ngramWeight = 3;
            } else {
                fuzzyWeight = 3;
                ngramWeight = 7;
            }

            List<ResponseWikiDto> fuzzyList = searchTitleUseFuzzyDto(title, searchCount, fuzziness, reliable);
            List<ResponseWikiDto> ngramList = searchTitleUseNgramDto(title, searchCount, reliable);

            //두개 리스트를 새로 합치고, 스코어가 있다면 가중. < 지명 : 스코어 > 로 집계
            HashMap<String, Float> alladdHashMap = new HashMap<>();
            //1. Fuzzy 삽입
            for (ResponseWikiDto dto : fuzzyList) {
                alladdHashMap.put(dto.getAttractionName(), (dto.getScore() * fuzzyWeight));
            }
            //2. ngram 삽입, fuzzy와 동일한 지명이 있다면 스코어 가중
            for (ResponseWikiDto dto : ngramList) {
                if(alladdHashMap.containsKey(dto.getAttractionName())) { //동일 지명
                    float existingScore = alladdHashMap.get(dto.getAttractionName());
                    float newScore = dto.getScore() * ngramWeight;
                    alladdHashMap.put(dto.getAttractionName(), existingScore + newScore);;
                } else {
                    alladdHashMap.put(dto.getAttractionName(), dto.getScore() * ngramWeight);
                }
            }

            //스트림을 통해 해시맵 내림차순 정렬 후 리스트화
            List<String> nameList = alladdHashMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Float>comparingByValue().reversed())
                    .limit(maxResults)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());


            //추려진 값들을 should를 사용하여 동시적으로 조회
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String name : nameList) {
                boolQueryBuilder.should(QueryBuilders.termQuery("attraction_name.keyword", name)); //here
            }
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(boolQueryBuilder)
                    .withCollapseField("content_id") //해당 컬럼으로 묶기(중복 제거)
                    .withPageable(PageRequest.of(0, maxResults)) // maxResults만큼 결과
                    .build();

            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);
            List<Wiki> searchResult =  toolsForWikiService.getListBySearchHits(searchHits, reliable);

            /**
             * AttractionName List대로 재정렬 로직 구현
             * 1. resultAttractionName 리스트를 순회 (소스)
             * 2. 중간 연산 : 각 name에 대해 searchResults 스트림을 열고 attraction Name이 일치하는 객체 필터링
             * 3. 종단 연산 : 결과를 List<responseWikiDto>로 수집
             */
            return nameList.stream()
                    .flatMap(name -> searchResult.stream()
                            .filter(wiki -> wiki.getAttractionName().equals(name)))
                    .map(wiki -> wiki.toDto())
                    .collect(Collectors.toList());
        }
    }
}
