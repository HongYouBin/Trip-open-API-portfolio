package com.ssafy.i5i.hotelAPI.domain.elastic.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchAllDto {
    String searchContent;
    int maxResults;
    int attractionNameCorrectionFactor = 4; //AttName 제목 스코어 가중치. 기본은 4
    int overviewCorrectionFactor = 3; //Overview 공식 Description 스코어 가중치. 기본은 3
    int wikiTitleCorrectionFactor = 2; //위키 타이틀 스코어 가중치. 기본은 2
    int wikiContentCorrectionFactor = 1; //위키 Descrption 스코어 가중치. 기본은 1
    boolean useReliableSearch = true; //신뢰성 있는 검색 결과의 사용 여부. true일 경우 term의 정확도를 비교 분석하여 신뢰성 있는 정보만 검출하고, 나머지는 null의 결과를 가진다.

    public SearchAllDto(String searchContent, int maxResults) {
        this.searchContent = searchContent;
        this.maxResults = maxResults;
    }
}
