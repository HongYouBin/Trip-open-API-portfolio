package com.sch.sch_elasticsearch.domain.accommodation.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;

@Getter @Setter
public class AccommodationDTO {
    private float accommodationLng;
    private float accommodationLat;
    private String accommodationName;
    private String accommodationType;
    private String accommodationAddr;
    private String accommodationPic;
    private int accommodationPrice;
    private float score;
}
