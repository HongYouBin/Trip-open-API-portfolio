//package com.ssafy.i5i.hotelAPI.domain.hotel.repository;
//
//import com.ssafy.i5i.hotelAPI.domain.food.entity.Food;
//import com.ssafy.i5i.hotelAPI.domain.hotel.entity.Accommodation;
//import com.ssafy.i5i.hotelAPI.domain.hotel.entity.AttractionAccommodation;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface AttractionAccommdodationRepository extends JpaRepository<AttractionAccommodation, Long> {
//    // input: title -> AttractionAccommodation
//    @Query("SELECT aa FROM AttractionAccommodation aa " +
//            "JOIN fetch Accommodation a ON a.accommodationId = aa.accommodation.accommodationId  " +
//            "JOIN fetch Attraction ai ON ai.contentId = aa.attraction.contentId " +
//            "WHERE ai.title = :title")
//    Optional<List<AttractionAccommodation>> findByTitle(String title);
//
//    @Query("SELECT a FROM Accommodation a WHERE a.accommodationLongitude >= :minX AND a.accommodationLongitude <= :maxX AND a.accommodationLatitude >= :minY AND a.accommodationLatitude <= :maxY")
//    Optional<List<Accommodation>> findByCoordinate(double maxY, double maxX, double minY, double minX);
//}
