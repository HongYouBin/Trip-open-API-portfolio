package com.ssafy.i5i.hotelAPI.domain.hotel.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AttractionCoordinateRequestDto {
    private Double longitude;
    private Double latitude;
    private Double distance;
    private String sorted;
    private Integer maxResults;
    private Integer page;
}