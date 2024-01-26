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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 다수 서비스에서 사용하는 유용한 공통 메서드들의 모음입니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ToolsForWikiService {




    /**
     * SearchHits의 값을 전처리를 거쳐 List<Wiki> 값으로 출력
     * @param SearchHits<Wiki> result
     * @return List<Wiki>
     */
    public List<Wiki> getListBySearchHits(SearchHits<Wiki> result, boolean reliable) {
        // SearchHits 내부 Content를 List<Wiki>로 변환
        List<Wiki> wikiList = new ArrayList<>();
        for (SearchHit<Wiki> hit : result) {
            Wiki wiki = hit.getContent();
            //신뢰성 판단 로직 사용
            wiki.setScore(hit.getScore());
            wiki = setReliableWiki(wiki, reliable);
            wikiList.add(wiki);
        }
        return wikiList;
    }

    /**
     * Wiki에 Reliable 로직과 score 적용
     * @param wiki
     * @return wiki
     */
    public Wiki setReliableWiki(Wiki wiki, boolean reliable) {
        //신뢰성 판단 로직 사용
        if(reliable && (wiki.getMatchTerm() / (float)wiki.getTotalTerm()) <= 0.2f) {
            wiki.setWikiContentAndWikiTitleNull();
        }
     return wiki;
    }

    /**
     * TypeNum : Keyword change
     * @param typeNum (input num)
     * @return type (String keyword)
     * @throws CommonException 유효하지 않은 typeNum이 입력될 경우
     */
    public String getType(int typeNum) {
        String type = "";
        switch (typeNum) {
            case 0:
                type = "attraction_name.keyword";
                break;
            case 1:
                type = "wiki_title.keyword";
                break;
            case 2:
                type = "attraction_name";
                break;
            case 3:
                type = "wiki_content";
                break;
            case 4:
                type = "overview";
                break;
            default:
                throw new CommonException(ExceptionType.WIKI_TYPENUM_IS_INVALID);
        }
        return type;
    }
}
