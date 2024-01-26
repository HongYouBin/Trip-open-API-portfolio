package com.ssafy.i5i.hotelAPI.domain.elastic.service;

import com.ssafy.i5i.hotelAPI.common.exception.CommonException;
import com.ssafy.i5i.hotelAPI.common.exception.ExceptionType;
import com.ssafy.i5i.hotelAPI.domain.elastic.dto.ResponseWikiDto;
import com.ssafy.i5i.hotelAPI.domain.elastic.dto.SearchAllDto;
import com.ssafy.i5i.hotelAPI.domain.elastic.dto.WikiDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class ElasticService {
    private final WebClient webClient;

    public ElasticService(@Value("${url.host}") String baseUrl){
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl+"/elastic/api/wiki")
                .build();
    }

    public List<ResponseWikiDto> searchExact(int typeNum, String inputString, boolean reliable, int maxResults) {
        if(maxResults <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);

        List<ResponseWikiDto> result = webClient.get().uri(uriBuilder -> {
            uriBuilder
                    .path("/keyword")
                    .queryParam("typeNum",typeNum)
                    .queryParam("inputString",inputString)
                    .queryParam("reliable",reliable)
                    .queryParam("maxResults",maxResults);
                return uriBuilder.build();
            })
                .retrieve()
                .bodyToFlux(WikiDto.class)
                .map(WikiDto::toResponse)
                .collectList()
                .block();

        if(result.size() == 0)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public List<ResponseWikiDto> searchPartial(int typeNum, String inputString, boolean reliable, int maxResults) {
        if(maxResults <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        List<ResponseWikiDto> result = webClient.get().uri(uriBuilder -> {
                    uriBuilder
                            .path("/partial")
                            .queryParam("typeNum",typeNum)
                            .queryParam("inputString",inputString)
                            .queryParam("reliable",reliable)
                            .queryParam("maxResults",maxResults);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(WikiDto.class)
                .map(WikiDto::toResponse)
                .collectList()
                .block();
        if(result.size() == 0)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public List<ResponseWikiDto> fuzzinessSearch(int typeNum, String inputString, boolean reliable, int maxResults, int fuzziness) {
        if(maxResults <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        List<ResponseWikiDto> result = webClient.get().uri(uriBuilder -> {
                    uriBuilder
                            .path("/fuzzy")
                            .queryParam("typeNum",typeNum)
                            .queryParam("inputString",inputString)
                            .queryParam("reliable",reliable)
                            .queryParam("maxResults",maxResults)
                            .queryParam("fuzziness", fuzziness);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(WikiDto.class)
                .map(WikiDto::toResponse)
                .collectList()
                .block();
        if(result.size() == 0)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public List<ResponseWikiDto> searchAll(String content, int maxResults) {
        if(maxResults <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        List<ResponseWikiDto> result = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/search").build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new SearchAllDto(content, maxResults)))
                .retrieve()
                .bodyToFlux(WikiDto.class)
                .map(WikiDto::toResponse)
                .collectList()
                .block();
        if(result.size() == 0)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public List<ResponseWikiDto> searchFuzzyAndNgram(String title, int maxResults, int fuzziness, boolean reliable, boolean fuzzyPrimary) {
        if(maxResults <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        List<ResponseWikiDto> result = webClient.get().uri(uriBuilder -> {
                    uriBuilder
                            .path("/title/aggregate-search")
                            .queryParam("title",title)
                            .queryParam("maxResults",maxResults)
                            .queryParam("fuzziness",fuzziness)
                            .queryParam("reliable",reliable)
                            .queryParam("fuzzyPrimary",fuzzyPrimary);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(WikiDto.class)
                .map(WikiDto::toResponse)
                .collectList()
                .block();
        if(result.size() == 0)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public ResponseWikiDto searchTitleCorrect(String title, boolean reliable) {
        ResponseWikiDto result = webClient.get().uri(uriBuilder -> {
                    uriBuilder
                            .path("/title/correct")
                            .queryParam("title",title)
                            .queryParam("reliable",reliable);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(WikiDto.class)
                .map(WikiDto::toResponse)
                .block();
        if(result == null)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public List<ResponseWikiDto> searchTitleUseFuzzyDto(String title, int maxResults, int fuzziness, boolean reliable) {
        if(maxResults <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        List<ResponseWikiDto> result =  webClient.get().uri(uriBuilder -> {
                    uriBuilder
                            .path("/title/fuzzy")
                            .queryParam("title",title)
                            .queryParam("maxResults",maxResults)
                            .queryParam("fuzziness",fuzziness)
                            .queryParam("reliable",reliable);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(WikiDto.class)
                .map(WikiDto::toResponse)
                .collectList()
                .block();
        if(result.size() == 0)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public List<ResponseWikiDto> searchTitleUseNgramDto(String title, int maxResults, boolean reliable) {
        if(maxResults <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        List<ResponseWikiDto> result =  webClient.get().uri(uriBuilder -> {
                    uriBuilder
                            .path("/title/ngram")
                            .queryParam("title",title)
                            .queryParam("maxResults",maxResults)
                            .queryParam("reliable",reliable);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(WikiDto.class)
                .map(WikiDto::toResponse)
                .collectList()
                .block();

        if(result.size() == 0)throw new CommonException(ExceptionType.SEARCH_NODATA_EXCEPTION);
        return result;
    }

    public Mono<String> test(){
        return webClient.get()
                .retrieve()
                .bodyToMono(String.class);
    }
}
