package com.ssafy.i5i.hotelAPI.domain.hotel.repository;

import com.ssafy.i5i.hotelAPI.domain.hotel.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// input: accommodation_name -> output: accommodation
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    @Query("SELECT a FROM Accommodation a WHERE a.accommodationLongitude >= :minX AND a.accommodationLongitude <= :maxX AND a.accommodationLatitude >= :minY AND a.accommodationLatitude <= :maxY")
    Optional<List<Accommodation>> findByCoordinate(double maxY, double maxX, double minY, double minX);
}
