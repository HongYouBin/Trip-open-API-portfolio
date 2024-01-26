package com.sch.sch_elasticsearch.domain.wiki.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Match Term 검증용 DTO
 */
@Getter
@AllArgsConstructor
public class TermDTO {
    String overview;
    String wikiContent;
    String pkId;
}
