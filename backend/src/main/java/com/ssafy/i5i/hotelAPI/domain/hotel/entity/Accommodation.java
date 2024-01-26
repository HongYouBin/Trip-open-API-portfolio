package com.ssafy.i5i.hotelAPI.domain.hotel.entity;

import com.ssafy.i5i.hotelAPI.domain.hotel.dto.response.AccommodationResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "accommodation")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Accommodation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accommodation_id")
    private Long accommodationId;

    @Column(name = "accommodation_name")
    private String accommodationName;

    @Column(name = "accommodation_type")
    private String accommodationType;

    @Column(name = "accommodation_addr")
    private String accommodationAddr;

    @Column(name = "accommodation_score")
    private Double accommodationScore;

    @Column(name = "accommodation_img")
    private String accommodationImg;

    @Column(name = "accommodation_price")
    private Long accommodationPrice;

    @Column(name = "accommodation_latitude")
    private Double accommodationLatitude;

    @Column(name = "accommodation_longitude")
    private Double accommodationLongitude;

    public AccommodationResponseDto toDto (){
        return new AccommodationResponseDto(this.accommodationName, this.accommodationType, this.accommodationAddr, this.accommodationScore, this.accommodationImg, this.accommodationPrice, this.accommodationLatitude, this.accommodationLongitude, null, null);
    }
}

