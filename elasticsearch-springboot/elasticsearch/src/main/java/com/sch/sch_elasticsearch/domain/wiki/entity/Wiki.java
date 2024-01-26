package com.sch.sch_elasticsearch.domain.wiki.entity;

import com.sch.sch_elasticsearch.domain.wiki.dto.ResponseWikiDto;
import com.sch.sch_elasticsearch.domain.wiki.dto.WikiDTO;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "scrap_wiki_final_1113")
@Mapping(mappingPath = "jsonlist/wiki/wiki-mapping.json")
@Setting(settingPath = "jsonlist/wiki/wiki-setting.json")
@Getter
public class Wiki {
    @Id
    private String id;

    float score;

    @Field(name = "pk_id")
    String pkId;

    @Field(name = "content_id")
    String contentId;

    @Field(name = "attraction_name")
    String attractionName;

    @Field(name = "number")
    String number;

    @Field(name = "wiki_title")
    String wiki_title;

    @Field(name = "wiki_content")
    String wiki_content;

    @Field(name = "overview")
    String overview;

    @Field(name = "match_term")
    Integer matchTerm;

    @Field(name = "total_term")
    Integer totalTerm;

    public void setMatchTerm(int value) {
        this.matchTerm = value;
    }
    public void setTotalTerm(int value) {
        this.totalTerm = value;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void setWikiContentAndWikiTitleNull() {
        this.wiki_content = null;
        this.wiki_title = null;
    }

    public ResponseWikiDto toDto(){
        return new ResponseWikiDto(score, contentId, attractionName, wiki_title, wiki_content, overview, matchTerm, totalTerm);
    }
}
