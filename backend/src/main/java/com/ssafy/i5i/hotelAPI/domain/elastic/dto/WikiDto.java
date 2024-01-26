package com.ssafy.i5i.hotelAPI.domain.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WikiDto {
    float score;
    String contentId;
    String attractionName;
    String wiki_title;
    String wiki_content;
    String overview;
    Integer matchTerm;
    Integer totalTerm;

    public ResponseWikiDto toResponse(){
        return new ResponseWikiDto(attractionName, wiki_title, wiki_content, overview, matchTerm, totalTerm);
    }
}
