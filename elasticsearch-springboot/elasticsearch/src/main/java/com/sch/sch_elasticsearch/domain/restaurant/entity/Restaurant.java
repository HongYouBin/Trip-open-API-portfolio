package com.sch.sch_elasticsearch.domain.restaurant.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "scrap_restaurant_1114")
@Mapping(mappingPath = "jsonlist/restaurant/restaurant-mapping.json")
@Setting(settingPath = "jsonlist/restaurant/restaurant-setting.json")
@Getter
public class Restaurant {
    @Id
    private String id;

    float score;

    @Field(name = "food_id")
    String foodId;

    @Field(name = "food_name")
    String foodName;

    @Field(name = "food_type_main")
    String foodTypeMain;

    @Field(name = "food_type_sub")
    String foodTypeSub;

    @Field(name = "food_latitude")
    float foodLatitude;

    @Field(name = "food_longitude")
    float foodLongitude;

    @Field(name = "food_like")
    Integer foodLike;

    @Field(name = "food_score")
    Integer foodScore;

    @Field(name = "food_star")
    float foodStar;

    @Field(name = "food_staruser")
    Integer foodStarUser;

    public void setScore(float score) {
        this.score = score;
    }
}
