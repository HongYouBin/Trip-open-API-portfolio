package com.sch.sch_elasticsearch.domain.accommodation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;


@Document(indexName = "scrap_accommo_1121")
@Mapping(mappingPath = "jsonlist/accommodation/accommodation-mapping.json")
@Setting(settingPath = "jsonlist/accommodation/accommodation-setting.json")
@Getter
public class Accommodation {
    @Id
    private String id;
    @Field(name = "accommodation_id")
    private String pkId;

    @Field(name = "accommodation_longitude")
    private String accommodationLng;

    @Field(name = "accommodation_latitude")
    private String accommodationLat;

    @Field(name = "accommodation_name")
    private String accommodationName;

    @Field(name = "accommodation_type")
    private String accommodationType;

    @Field(name = "accommodation_addr")
    private String accommodationAddr;

    @Field(name = "accommodation_img")
    private String accommodationPic;

    @Field(name = "accommodation_score")
    private String accommodationScore;

    @Field(name = "accommodation_price")
    private String accommodationPrice;

}
