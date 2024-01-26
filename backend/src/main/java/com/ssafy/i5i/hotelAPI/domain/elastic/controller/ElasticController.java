package com.ssafy.i5i.hotelAPI.domain.elastic.controller;

import com.ssafy.i5i.hotelAPI.common.exception.CommonException;
import com.ssafy.i5i.hotelAPI.common.exception.ExceptionType;
import com.ssafy.i5i.hotelAPI.common.response.DataResponse;
import com.ssafy.i5i.hotelAPI.domain.elastic.dto.ResponseWikiDto;
import com.ssafy.i5i.hotelAPI.domain.elastic.dto.SearchAllDto;
import com.ssafy.i5i.hotelAPI.domain.elastic.dto.WikiDto;
import com.ssafy.i5i.hotelAPI.domain.elastic.service.ElasticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/elastic")
public class ElasticController {
    private final ElasticService elasticService;

    //테스트용
    @GetMapping("/test")
    public Mono<String> test(){
        return elasticService.test();
    }

    //특정 컬럼의 Keyword 매칭 검색 ->
    //사용자에게 제공하지 않는 서비스
    @GetMapping("/keyword")
    public List<ResponseWikiDto> searchExact(@RequestParam("typeNum") int typeNum,
                                             @RequestParam("inputString") String inputString,
                                             @RequestParam("reliable") boolean reliable,
                                             @RequestParam("maxResults") int maxResults) {
        List<ResponseWikiDto> data = elasticService.searchExact(typeNum, inputString, reliable, maxResults);
        return data;
    }

    //특정 컬럼의 match 검색
    //사용자에게 제공하지 않는 서비스
    @GetMapping("/partial")
    public List<ResponseWikiDto> searchPartial(@RequestParam("typeNum") int typeNum,
                                               @RequestParam("inputString") String inputString,
                                               @RequestParam("reliable") boolean reliable,
                                               @RequestParam("maxResults") int maxResults) {
        List<ResponseWikiDto> data = elasticService.searchPartial(typeNum, inputString, reliable, maxResults);
        return data;
    }

    //특정 칼럼의 fuzzy 검색
    //사용자에게 제공하지 않는 서비스
    @GetMapping("/fuzzy")
    public List<ResponseWikiDto> searchFuzzy(@RequestParam("typeNum") int typeNum, @RequestParam("inputString") String inputString,
                                             @RequestParam("reliable") boolean reliable, @RequestParam("maxResults") int maxResults ,@RequestParam("fuzziness") int fuzziness) {
        List<ResponseWikiDto> data = elasticService.fuzzinessSearch(typeNum, inputString, reliable, maxResults, fuzziness);
        return data;
    }

    //통합 내용 검색(전문 검색)
    //사용자에게 제공되는 검색어 기반의 전문 검색 서비스
    @GetMapping("/search")
    public List<ResponseWikiDto> searchAll(@RequestParam("content") String content, @RequestParam("maxResults") int maxResults) {
        List<ResponseWikiDto> data = elasticService.searchAll(content, maxResults);
        return data;
    }

    //통합 제목 검색 : 제목 일치 or (Fuzzy + ngram)
    //사용자에게 제공되는 제목 기반의 검색 기능
    @GetMapping("/title/aggregate-search")
    public List<ResponseWikiDto> searchTitleComprehensive(@RequestParam("title") String title,
                                                          @RequestParam("maxResults") int maxResults,
                                                          @RequestParam("fuzziness") int fuzziness,
                                                          @RequestParam("reliable") boolean reliable,
                                                          @RequestParam("fuzzyPrimary") boolean fuzzyPrimary){
        List<ResponseWikiDto> data = elasticService.searchFuzzyAndNgram(title, maxResults, fuzziness, reliable, fuzzyPrimary);
        return data;
    }

    //제목 일치 여부 검색
    //사용자에게 제공하지 않는 서비스
    @GetMapping("/title/correct")
    public ResponseWikiDto searchTitleCorrect(@RequestParam("title") String title, @RequestParam("reliable") boolean reliable) {
        ResponseWikiDto data = elasticService.searchTitleCorrect(title, reliable);
        return data;
    }

    //Fuzzy 제목 검색
    //사용자에게 제공하지 않는 서비스
    @GetMapping("/title/fuzzy")
    public List<ResponseWikiDto> searchTitleFuzzy(@RequestParam("title") String title, @RequestParam("maxResults") int maxResults,
                                                  @RequestParam("fuzziness") int fuzziness, @RequestParam("reliable") boolean reliable)
    {
        List<ResponseWikiDto> data =  elasticService.searchTitleUseFuzzyDto(title, maxResults, fuzziness, reliable);
        return data;
    }
    //ngram 제목 검색
    //사용자에게 제공하지 않는 서비스
    @GetMapping("/title/ngram")
    public List<ResponseWikiDto> searchTitleNgram(@RequestParam("title") String title, @RequestParam("maxResults") int maxResults,
                                                  @RequestParam("reliable") boolean reliable)
    {
        List<ResponseWikiDto> data =  elasticService.searchTitleUseNgramDto(title, maxResults, reliable);
        return data;
    }

}
