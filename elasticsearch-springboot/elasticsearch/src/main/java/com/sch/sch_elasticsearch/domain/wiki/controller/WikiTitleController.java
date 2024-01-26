package com.sch.sch_elasticsearch.domain.wiki.controller;

import com.sch.sch_elasticsearch.aop.SaveLogging;
import com.sch.sch_elasticsearch.domain.wiki.dto.ResponseWikiDto;
import com.sch.sch_elasticsearch.domain.wiki.service.WikiServiceBasic;
import com.sch.sch_elasticsearch.domain.wiki.service.WikiServiceExtend;
import com.sch.sch_elasticsearch.domain.wiki.service.WikiServiceTitle;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 제목 검색과 관련된 기능 제공 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/wiki")
public class WikiTitleController {
    private final WikiServiceBasic wikiServiceBasic;
    private final WikiServiceExtend wikiServiceExtend;
    private final WikiServiceTitle wikiServiceTitle;

    //통합 제목 검색 : 제목 일치 or (Fuzzy + ngram)
    @GetMapping("/title/aggregate-search")
    @SaveLogging
    public List<ResponseWikiDto> searchTitleComprehensive(@RequestParam("title") String title,
                                                          @RequestParam("maxResults") int maxResults,
                                                          @RequestParam("fuzziness") int fuzziness,
                                                          @RequestParam("reliable") boolean reliable,
                                                          @RequestParam("fuzzyPrimary") boolean fuzzyPrimary)
    {
        try {
            ResponseWikiDto wiki = wikiServiceTitle.searchTitleCorrect(title, reliable);
            if (wiki != null) {
                List<ResponseWikiDto> wikiList = new ArrayList<>();
                wikiList.add(wiki);
                return wikiList;
            } //1. 일치 제목 검색이 있다면 이를 리스트에 추가 후 리턴
            return wikiServiceTitle.searchFuzzyAndNgram(title, maxResults, fuzziness, reliable, fuzzyPrimary); //2. 아니라면 두개 검색 비교후 리턴
        } catch (Exception e) {
            log.error("[ERR LOG] {}", e);
            throw new CommonException(ExceptionType.WIKI_AGGREGATE_TITLE_SEARCH_FAIL);
        }
    }

    //제목 일치 여부 검색
    @GetMapping("/title/correct")
    @SaveLogging
    public ResponseWikiDto searchTitleCorrect(@RequestParam("title") String title, @RequestParam("reliable") boolean reliable) {
        return wikiServiceTitle.searchTitleCorrect(title, reliable);
    }

    //Fuzzy 제목 검색
    @GetMapping("/title/fuzzy")
    @SaveLogging
    public List<ResponseWikiDto> searchTitleFuzzy(@RequestParam("title") String title, @RequestParam("maxResults") int maxResults,
                                                  @RequestParam("fuzziness") int fuzziness, @RequestParam("reliable") boolean reliable)
    {
        return wikiServiceTitle.searchTitleUseFuzzyDto(title, maxResults, fuzziness, reliable);
    }
    //ngram 제목 검색
    @GetMapping("/title/ngram")
    @SaveLogging
    public List<ResponseWikiDto> searchTitleNgram(@RequestParam("title") String title, @RequestParam("maxResults") int maxResults,
                                                  @RequestParam("reliable") boolean reliable)
    {
        return wikiServiceTitle.searchTitleUseNgramDto(title, maxResults, reliable);
    }
}
