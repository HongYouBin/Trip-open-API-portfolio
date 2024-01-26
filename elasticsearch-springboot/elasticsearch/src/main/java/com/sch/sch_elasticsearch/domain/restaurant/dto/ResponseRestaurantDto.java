package com.sch.sch_elasticsearch.domain.restaurant.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseRestaurantDto {
    int RestaurantId;
    float score;
    String RestaurantName;
    String RestaurantTypeMain;
    String RestaurantTypeSub;
    float RestaurantLat;
    float RestaurantLng;
    int RestaurantLike;
    int RestaurantScore;
    float RestaurantStar;
    int RestaurantStarUser;

}
