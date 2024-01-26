package com.ssafy.i5i.hotelAPI.domain.food.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

public class FoodRequestDto {
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Title {
		private String attractionName;
		private Double distance;
		private String sorted;
		private Integer maxResults;
		private Integer page;
	}

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Coordi {
		private Double latitude;
		private Double longitude;
		private Double distance;
		private String sorted;
		private Integer maxResults;
		private Integer page;
	}
}
