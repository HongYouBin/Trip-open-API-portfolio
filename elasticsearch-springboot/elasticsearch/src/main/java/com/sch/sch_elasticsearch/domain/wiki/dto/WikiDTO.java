package com.sch.sch_elasticsearch.domain.wiki.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WikiDTO {
    String pkId;
    String contentId;
    String attractionName;
    String number;
    String wiki_title;
    String wiki_content;
    String overview;
}
