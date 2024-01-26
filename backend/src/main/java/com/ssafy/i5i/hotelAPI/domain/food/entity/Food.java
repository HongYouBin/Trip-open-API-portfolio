package com.ssafy.i5i.hotelAPI.domain.food.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.ssafy.i5i.hotelAPI.domain.food.dto.response.FoodResponseDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "food")
@AllArgsConstructor
public class Food {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "food_id")
	private Long id;

	//음식점 이름
	@Column(name = "food_name")
	private String restaurantName;

	//음식점 카테고리
	@Column(name = "food_type")
	private String restaurantType;

	//음식점 경도
	@Column(name = "food_longitude")
	private Double restaurantLongitude;

	//음식점 위도
	@Column(name = "food_latitude")
	private Double restaurantLatitude;

	//음식점 찜
	@Column(name = "food_jjim")
	private Integer restaurantLike;

	//음식점 점수
	@Column(name = "food_score")
	private Integer restaurantScore;

	//음식점 별점
	@Column(name = "food_star")
	private Double restaurantStar;

	//음식점 별점을 준 사람의 수
	@Column(name = "food_staruser")
	private Integer restaurantStarUser;

	public FoodResponseDto.Coordi convertToDto (){
		return new FoodResponseDto.Coordi(this.id, this.restaurantName, this.restaurantType, this.restaurantLongitude, this.restaurantLatitude, this.restaurantLike, this.restaurantScore, this.restaurantStar, this.restaurantStarUser, null,null);
	}

}
