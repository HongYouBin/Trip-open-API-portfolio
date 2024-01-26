package com.sch.sch_elasticsearch.domain.wiki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;

@Getter
@AllArgsConstructor
public class ResponseWikiDto {
    float score;
    String contentId;
    String attractionName;
    String wiki_title;
    String wiki_content;
    String overview;
    Integer matchTerm;
    Integer totalTerm;

    public ResponseWikiDto() {

    }
}
