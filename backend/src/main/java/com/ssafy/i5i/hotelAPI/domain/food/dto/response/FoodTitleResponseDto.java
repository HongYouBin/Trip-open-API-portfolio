package com.ssafy.i5i.hotelAPI.domain.food.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
public class FoodTitleResponseDto {
	private Integer attractionId;
	private String attracionName;
	private Double attractionLongitude;
	private Double attractionLatitude;
	private Long restaurantId;
	private String restaurantName;
	private String restaurantType;
	private Double restaurantLongitude;
	private Double restaurantLatitude;
	private Integer restaurantLike;
	private Integer restaurantScore;
	private Double restaurantStar;
	private Integer restaurantStarUser;

	public FoodResponseDto.TitleD convertToFDto (){
		return new FoodResponseDto.TitleD(this.attractionId, this.attracionName,
			this.attractionLongitude,this.attractionLatitude,
			this.restaurantId,this.restaurantName,this.restaurantType,
			this.restaurantLongitude, this.restaurantLatitude, this.restaurantLike, this.restaurantScore, this.restaurantStar,
			this.restaurantStarUser, null);
	}
}
