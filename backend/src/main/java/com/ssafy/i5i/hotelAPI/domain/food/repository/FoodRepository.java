package com.ssafy.i5i.hotelAPI.domain.food.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ssafy.i5i.hotelAPI.domain.food.dto.response.FoodTitleResponseDto;
import com.ssafy.i5i.hotelAPI.domain.food.entity.Food;


public interface FoodRepository extends JpaRepository<Food, Long> {

	@Query("SELECT f FROM Food f WHERE f.restaurantLongitude >= :minX AND f.restaurantLongitude <= :maxX AND f.restaurantLatitude >= :minY AND f.restaurantLatitude <= :maxY")
	Optional<List<Food>> getFoodFromLngLatv(@Param("maxY") double maxY, @Param("maxX") double maxX, @Param("minY") double minY, @Param("minX") double minX);

	@Query("SELECT new com.ssafy.i5i.hotelAPI.domain.food.dto.response.FoodTitleResponseDto(a.contentId, a.title, a.longitude, a.latitude, " +
		"f.id, f.restaurantName, f.restaurantType, f.restaurantLongitude, f.restaurantLatitude, " +
		"f.restaurantLike, f.restaurantScore, f.restaurantStar, f.restaurantStarUser) " +
		"FROM AttractionFood af " +
		"LEFT JOIN Attraction a ON af.attraction.contentId = a.contentId " +
		"LEFT JOIN Food f ON f.id = af.food.id " +
		"WHERE a.title = :title")
	Optional<List<FoodTitleResponseDto>> getFoodFromTravle(@Param("title") String title);
}