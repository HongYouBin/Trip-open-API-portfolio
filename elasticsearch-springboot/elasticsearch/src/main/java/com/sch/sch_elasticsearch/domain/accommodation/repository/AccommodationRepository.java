package com.sch.sch_elasticsearch.domain.accommodation.repository;

import com.sch.sch_elasticsearch.domain.accommodation.entity.Accommodation;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends ElasticsearchRepository<Accommodation, String> {
    List<Accommodation> findByAccommodationName(String name);

    @Query("{\"match\":{\"accommodation_name\":\"?0\"}}")
    List<Accommodation> matchAccommoName(String name);

    @Query("{\"fuzzy\":{\"accommodation_name\":{\"value\":\"?0\",\"fuzziness\":1}}}}")
    List<Accommodation> fuzzyAccommoName(String name);

    @Query("{\"_source\":[\"accommodation_lng\",\"accommodation_price\",\"accommodation_addr\",\"accommodation_lat\",\"accommodation_score\",\"accommodation_name\",\"accommodation_pic\"],\"fuzzy\":{\"accommodation_name\":{\"value\":\"?0\",\"fuzziness\":1}}}")
    List<Accommodation> getSource(String name);

}
