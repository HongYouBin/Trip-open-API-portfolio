package com.sch.sch_elasticsearch.domain.accommodation.controller;

import com.sch.sch_elasticsearch.aop.SaveLogging;
import com.sch.sch_elasticsearch.domain.accommodation.dto.AccommodationDTO;
import com.sch.sch_elasticsearch.domain.accommodation.service.AccommodationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;


    //Fuzzy 제목 검색
    @SaveLogging
    @GetMapping("/title/fuzzy")
    public List<AccommodationDTO> searchTitleFuzzy(@RequestParam("title") String title,
                                                        @RequestParam("maxResults") int maxResults,
                                                        @RequestParam("fuzziness") int fuzziness)
    {
        return accommodationService.searchTitleUseFuzzyDto(title, maxResults, fuzziness);
    }

    //ngram 제목 검색
    @SaveLogging
    @GetMapping("/title/ngram")
    public List<AccommodationDTO> searchTitleNgram(@RequestParam("title") String title,
                                                        @RequestParam("maxResults") int maxResults)
    {
        return accommodationService.searchTitleUseNgramDto(title, maxResults);
    }

    //통합 제목 검색 : 제목 일치 or (Fuzzy + ngram)
    @SaveLogging
    @GetMapping("/title/aggregate-search")
    public List<AccommodationDTO> searchTitleComprehensive(@RequestParam("title") String title,
                                                                @RequestParam("maxResults") int maxResults,
                                                                @RequestParam("fuzziness") int fuzziness,
                                                                @RequestParam("fuzzyPrimary") boolean fuzzyPrimary)
    {
        return accommodationService.searchFuzzyAndNgram(title, maxResults, fuzziness, fuzzyPrimary); //2. 결과가 없다면 두 검색 진행 후 유사도별로 정렬
    }

}
