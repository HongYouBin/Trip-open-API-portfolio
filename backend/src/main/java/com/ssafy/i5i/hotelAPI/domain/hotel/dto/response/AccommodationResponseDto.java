package com.ssafy.i5i.hotelAPI.domain.hotel.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor()
public class AccommodationResponseDto {
    private String accommodationName;

    private String accommodationType;

    private String accommodationAddr;

    private Double accommodationScore;

    private String accommodationImg;

    private Long accommodationPrice;

    private Double accommodationLatitude;

    private Double accommodationLongitude;

    private Double relativeDistance;

    private String attractionName;
}