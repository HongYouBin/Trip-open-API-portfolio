package com.sch.sch_elasticsearch.domain.wiki.service;

import com.sch.sch_elasticsearch.domain.wiki.entity.Wiki;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 용어 분석 및 overview - wikiContent의 유사도 비교 관련 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WikiServiceTermSimilary {
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    private final ToolsForWikiService toolsForWikiService;
    private final RestHighLevelClient restHighLevelClient;
    private final WikiServiceBasic wikiServiceBasic;

    //2단어 길이 이상 필터링된 인덱스
    @Value("${info.index.wiki}")
    String wikiIndex;

    @Value("${info.analyzer.nori-with-length}")
    String noriAnalyzerWithLength;


    /**
     * <용어, 개수>의 해시맵 두개를 받아, 각각의 용어간 유사성 빈도 비교
     * @param hashMapA
     * @param hashMapB
     * @return
     */
    public int[] calculateTermOverviewAndContent(HashMap<String, Integer> hashMapA, HashMap<String, Integer> hashMapB) {
        try {
            int totalTermCount = 0; //전체 용어 수
            int matchTermCount = 0; //맞는 용어 수

            //1. 전체 토큰 세기 :  HashMap의 values() 메서드를 사용하여 값들을 순회하고 합산
            for (Integer value : hashMapA.values()) {
                totalTermCount += value;
            }
            for (Integer value : hashMapB.values()) {
                totalTermCount += value;
            }

            //2. 맞는 용어 수를 세기
            for(String key : hashMapA.keySet()) {
                if(hashMapB.containsKey(key)) {
                    matchTermCount += hashMapA.get(key);
                    matchTermCount += hashMapB.get(key);
                }
            }

            log.info("[calculateSimilarityByTerm] 전체 용어 : {} , 매치된 용어 : {}", totalTermCount, matchTermCount);
            return new int[] {totalTermCount, matchTermCount};
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e);
            throw new CommonException(ExceptionType.WIKI_CALCULATE_SIMILARY_OVERVIEW_AND_CONTENT);
        }
    }

    /**
     * 문자열을 입력받아 분석기를 사용해 토큰화된 값을 HashMap<토큰, 개수> 결과로 리턴
     * @param inputString
     * @return HashMap<String, Integer>
     */
    public HashMap<String, Integer> useAnalyzerAndGetTokens(String inputString) {
        try {
            HashMap<String, Integer> hashMap = new HashMap<>();
            AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(wikiIndex, noriAnalyzerWithLength, inputString);
            AnalyzeResponse response = restHighLevelClient.indices().analyze(request, RequestOptions.DEFAULT);
            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens(); // 결과에서 토큰화된 텍스트 가져오기

            // tokens 리스트를 반복하여 HashMap에 저장 후 출력
            tokens.forEach(token -> {
                String term = token.getTerm();
                if(hashMap.containsKey(term)) {
                    hashMap.put(term, hashMap.get(term) + 1);
                }
                else hashMap.put(term, 1);
            });
            return hashMap;
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e);
            throw new CommonException(ExceptionType.WIKI_USE_ANALYZER_AND_GET_TERMS_FAIL);
        }
    }

    /**
     * searchCount에 맞는 현재 Term이 존재하지 않는 값들을 List로 반환
     * @param searchCount
     * @return
     */
    public List<Wiki> findWikiMatchTermIsNull(int searchCount) {
        try {
            // 존재하는 wiki_content 필드를 가진 문서 검색을 위한 쿼리 빌더 생성
            BoolQueryBuilder mustNotQueryBuilder = QueryBuilders.boolQuery()
                    .mustNot(QueryBuilders.existsQuery("match_term"));

            // 위에서 생성한 쿼리를 사용하여 검색 쿼리 객체를 생성
            NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(mustNotQueryBuilder)
                    .withFields("wiki_content", "overview", "pk_id") // 필요한 필드를 명시
                    .withPageable(PageRequest.of(0, searchCount)) // 검색할 문서의 수를 지정
                    .build();

            // Elasticsearch 템플릿을 사용하여 검색 실행
            SearchHits<Wiki> searchHits = elasticsearchRestTemplate.search(searchQuery, Wiki.class);

            return toolsForWikiService.getListBySearchHits(searchHits, false);
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e);
            throw new CommonException(ExceptionType.WIKI_FIND_WIKI_NOT_EXIST_TERMS);
        }
    }

    /**
     * 해당 도큐먼트의 content 값이 Null인지 판정 후, true라면 term 값을 update하는 메서드
     * @param wiki
     * @return boolean
     */
    public boolean checkWikiContentIsNull(Wiki wiki) {
        try {
            String wikiContent = wiki.getWiki_content();
            String wikiOverview = wiki.getOverview();
            if(wikiContent == null || wikiContent.equals("null") || wikiOverview == null) {
                wiki.setMatchTerm(-1); //null을 뜻하는 -1 삽입
                wiki.setTotalTerm(1);
                wikiServiceBasic.insertWiki(wiki); //변경된 값으로 갱신
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e);
            throw new CommonException(ExceptionType.WIKI_CHECK_CONTENT_IS_NULL);
        }
    }

    /**
     * 도큐먼트를 입력받아 문장 간 유사도를 추출하고, 나온 결과값(match_term / total_term)을 보정하여 갱신
     * @param wiki
     */
    public void updateNewTerms(Wiki wiki) {
        try {
            //1차 비교 : overview : wiki_content, 리턴값은 {totalTermCount, matchTermCount}
            int termsOne[] = calculateTermOverviewAndContent(
                    useAnalyzerAndGetTokens(wiki.getOverview()),
                    useAnalyzerAndGetTokens(wiki.getWiki_content())
            );
            //2차 비교 : attraction_name과(원문 타이틀) wiki_content
            int termsTwo[] = calculateTermOverviewAndContent(
                    useAnalyzerAndGetTokens(wiki.getAttractionName()),
                    useAnalyzerAndGetTokens(wiki.getWiki_content())
            );

            //예외 처리 : divideByZero, 혹은 유의미한 토큰이 없을 경우 Null과 동일 처리
            if(termsOne[0] == 0 || termsTwo[0] == 0) {
                wiki.setMatchTerm(-1); //null을 뜻하는 -1 삽입
                wiki.setTotalTerm(1);
                wikiServiceBasic.insertWiki(wiki); //변경된 값으로 갱신
                return;
            }

            //2차가 전체 용어가 적을 수밖에 없다. 전체 용어 개수만큼 비율을 맞추고, matchTerm만 추가해준다.
            int correctionFactor = termsOne[0] / termsTwo[0]; //보정 비율
            int totalMatchTerm = termsOne[1] + (correctionFactor * termsTwo[1]); //matechTerm 보정값

            wiki.setMatchTerm(totalMatchTerm);
            wiki.setTotalTerm(termsOne[0]);
            wikiServiceBasic.insertWiki(wiki);
        } catch (Exception e) {
            log.error("[ERR LOG]{}", e);
            throw new CommonException(ExceptionType.WIKI_UPDATE_NULL_TERMS_FAIL);
        }
    }
}
