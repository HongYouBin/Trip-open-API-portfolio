package com.ssafy.i5i.hotelAPI.domain.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseWikiDto {

    String attractionName;
    String wiki_title;
    String wiki_content;
    String overview;
    Integer matchTerm;//overview와 wiki_content의 일치도
    Integer totalTerm;//토큰의 총합
}
