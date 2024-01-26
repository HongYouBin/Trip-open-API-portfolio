package com.sch.sch_elasticsearch.domain.wiki.repository;

import com.sch.sch_elasticsearch.domain.accommodation.entity.Accommodation;
import com.sch.sch_elasticsearch.domain.wiki.entity.Wiki;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WikiRepository extends ElasticsearchRepository<Wiki, String> {
    List<Wiki> findByAttractionName(String input);

    @Query("{\"bool\":{\"must\":[{\"match\":{\"attraction_name\":{\"query\":\"?0\",\"fuzziness\":1}}}]}}")
    List<Wiki> findPartialAttractionName(String name);

    @Query("{\"script\":{" +
            "\"source\":\"ctx._source.wiki_title = params.wiki_title; " +
            "ctx._source.wiki_content = params.wiki_content;\"," +
            "\"lang\":\"painless\"," +
            "\"params\":{" +
            "\"wiki_title\":\"새로운 위키 제목2\"," +
            "\"wiki_content\":\"새로운 위키 내용2\"}}," +
            "\"query\":{\"" +
            "match\":{\"attraction_name\":\"새로운 관광지 이름\"}}}")
    void updataNameAndContent(String attName, String title, String content);

}
