package com.sch.sch_elasticsearch.domain.accommodation.service;

import com.sch.sch_elasticsearch.domain.accommodation.dto.AccommodationDTO;
import com.sch.sch_elasticsearch.domain.accommodation.entity.Accommodation;
import com.sch.sch_elasticsearch.domain.restaurant.dto.ResponseRestaurantDto;
import com.sch.sch_elasticsearch.domain.restaurant.entity.Restaurant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ToolsForAccommodationService {
    public List<AccommodationDTO> getListBySearchHits(SearchHits<Accommodation> search) {
        List<AccommodationDTO> accommodationDTOList = new ArrayList<>();
        for (SearchHit<Accommodation> hit : search) {
            AccommodationDTO accommodationDTO = new AccommodationDTO();
            accommodationDTO.setScore(hit.getScore());
            accommodationDTO.setAccommodationAddr(hit.getContent().getAccommodationAddr());
            accommodationDTO.setAccommodationLng(Float.parseFloat(hit.getContent().getAccommodationLng()));
            accommodationDTO.setAccommodationLat(Float.parseFloat(hit.getContent().getAccommodationLat()));
            accommodationDTO.setAccommodationName(hit.getContent().getAccommodationName());
            accommodationDTO.setAccommodationPic(hit.getContent().getAccommodationPic());
            accommodationDTO.setAccommodationPrice(Integer.parseInt(hit.getContent().getAccommodationPrice()));
            accommodationDTO.setAccommodationType(hit.getContent().getAccommodationType());
            accommodationDTOList.add(accommodationDTO);
        }
        return accommodationDTOList;
    }
}
