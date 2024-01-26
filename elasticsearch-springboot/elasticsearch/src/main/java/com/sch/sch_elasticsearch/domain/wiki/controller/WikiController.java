package com.sch.sch_elasticsearch.domain.wiki.controller;

import com.sch.sch_elasticsearch.aop.SaveLogging;
import com.sch.sch_elasticsearch.domain.wiki.dto.ResponseWikiDto;
import com.sch.sch_elasticsearch.domain.wiki.dto.SearchAllDTO;
import com.sch.sch_elasticsearch.domain.wiki.service.WikiServiceBasic;
import com.sch.sch_elasticsearch.domain.wiki.service.WikiServiceExtend;
import com.sch.sch_elasticsearch.domain.wiki.service.WikiServiceTitle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 특정 컬럼이나 필드 검색 결과를 조회하는 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/wiki")
public class WikiController {
    private final WikiServiceBasic wikiServiceBasic;
    private final WikiServiceExtend wikiServiceExtend;
    private final WikiServiceTitle wikiServiceTitle;

    //특정 컬럼의 Keyword 매칭 검색 ->
    @GetMapping("/keyword")
    @SaveLogging
    public List<ResponseWikiDto> searchExact(@RequestParam("typeNum") int typeNum,
                                             @RequestParam("inputString") String inputString,
                                             @RequestParam("reliable") boolean reliable,
                                             @RequestParam("maxResults") int maxResults) {
        return wikiServiceBasic.searchExact(typeNum, inputString, reliable, maxResults);
    }

    //특정 컬럼의 match 검색
    @GetMapping("/partial")
    @SaveLogging
    public List<ResponseWikiDto> searchPartial(@RequestParam("typeNum") int typeNum,
                                    @RequestParam("inputString") String inputString,
                                    @RequestParam("reliable") boolean reliable,
                                    @RequestParam("maxResults") int maxResults) {
        return wikiServiceBasic.searchPartial(typeNum, inputString, reliable, maxResults);
    }

    //특정 칼럼의 fuzzy 검색
    @GetMapping("/fuzzy")
    @SaveLogging
    public List<ResponseWikiDto> searchFuzzy(@RequestParam("typeNum") int typeNum,
                                             @RequestParam("inputString") String inputString,
                                             @RequestParam("reliable") boolean reliable,
                                             @RequestParam("maxResults") int maxResults,
                                             @RequestParam("fuzziness") int fuzziness) {
        return wikiServiceExtend.fuzzinessSearch(typeNum, inputString, reliable, maxResults, fuzziness);
    }

    //통합 내용 검색(전문 검색)
    @PostMapping("/search")
    @SaveLogging
    public List<ResponseWikiDto> searchAll(@RequestBody SearchAllDTO searchAllDTO) {
        return wikiServiceExtend.searchAll(searchAllDTO);
    }

    //통합 내용 검색(전문 검색) : 간단한 버전
    @GetMapping("/search")
    @SaveLogging
    public List<ResponseWikiDto> searchAll(@RequestParam("inputString") String inputString,
                                           @RequestParam("maxResults") int maxResults,
                                           @RequestParam("reliable") boolean reliable) {
        return wikiServiceExtend.searchAll(inputString, maxResults, reliable);
    }

}
