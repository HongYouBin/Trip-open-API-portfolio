package com.ssafy.i5i.hotelAPI.domain.food.dto.response;

import lombok.*;

public class FoodResponseDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TitleD {
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
		private Double Distance;
	}


	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Coordi {
		private Long id;
		private String restaurantName;
		private String restaurantType;
		private Double restaurantLongitude;
		private Double restaurantLatitude;
		private Integer restaurantLike;
		private Integer restaurantScore;
		private Double restaurantStar;
		private Integer restaurantStarUser;
		private Double Distance;
		private String attractionName;
	}
}
