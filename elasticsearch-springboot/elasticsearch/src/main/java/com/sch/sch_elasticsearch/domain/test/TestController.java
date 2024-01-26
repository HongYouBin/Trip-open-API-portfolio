package com.sch.sch_elasticsearch.domain.test;

import com.sch.sch_elasticsearch.aop.SaveLogging;
import com.sch.sch_elasticsearch.domain.wiki.entity.Wiki;
import com.sch.sch_elasticsearch.domain.wiki.service.*;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.springframework.web.bind.annotation.*;
import org.elasticsearch.client.RequestOptions;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RestHighLevelClient client;
    private final WikiServiceTermSimilary wikiServiceTermSimilary;

    @RequestMapping("boot")
    public String bootCheck() {
        try {
            MainResponse response = client.info(RequestOptions.DEFAULT);
            return "Well Done : " + response.getVersion().toString();
        } catch (Exception e) {
            return "fail to boot : " + e.getMessage();
        }
    }

    @SaveLogging
    @RequestMapping("boot-logger")
    public String bootCheck2() {
        try {
            MainResponse response = client.info(RequestOptions.DEFAULT);
            return "Well Done : " + response.getVersion().toString();
        } catch (Exception e) {
            return "fail to boot : " + e.getMessage();
        }
    }


    @GetMapping("term")
    public String setTerm(@RequestParam("searchCount") int searchCount) {
        while(true) {
            //1. searchCount만큼 데이터를 긁어온다.
            List<Wiki> result = wikiServiceTermSimilary.findWikiMatchTermIsNull(searchCount);
            if(result.isEmpty()) break;

            //2. 반복 시행 : null인 경우 -1, 그 외에 분석 후 데이터 삽입
            for(int i = 0; i < result.size(); i++) {
                Wiki wiki = result.get(i);
                if(wikiServiceTermSimilary.checkWikiContentIsNull(wiki)) continue; //null일경우 -1 값 업데이트
                wikiServiceTermSimilary.updateNewTerms(wiki); //아닐 경우 계산 후 새로운 값 업데이트
            }
        }
        return "Finish Set Terms";
    }

}
