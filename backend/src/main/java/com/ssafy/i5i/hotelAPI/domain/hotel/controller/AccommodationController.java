package com.ssafy.i5i.hotelAPI.domain.hotel.controller;

import com.ssafy.i5i.hotelAPI.common.response.DataResponse;
import com.ssafy.i5i.hotelAPI.domain.hotel.dto.request.AttractionCoordinateRequestDto;
import com.ssafy.i5i.hotelAPI.domain.hotel.dto.request.AttractionNameRequestDto;
import com.ssafy.i5i.hotelAPI.domain.hotel.dto.response.AccommodationResponseDto;
import com.ssafy.i5i.hotelAPI.domain.hotel.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
@CrossOrigin(origins = "*")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @GetMapping("/by-name")
    public List<AccommodationResponseDto> getAccommodationByName(
            @RequestParam("name") String name,
            @RequestParam("distance") Double distance,
            @RequestParam("sorted") String sorted,
            @RequestParam("maxResults") Integer maxResults,
            @RequestParam("page") Integer page){
        AttractionNameRequestDto attractionNameRequestDto = new AttractionNameRequestDto(name, distance, sorted, maxResults, page);
        List<AccommodationResponseDto> data = accommodationService.getAccommodationByName(attractionNameRequestDto);
        return data;
    }

    @GetMapping("/by-coordinate")
    public List<AccommodationResponseDto> getAccomodationByCoordinate(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("distance") Double distance,
            @RequestParam("sorted") String sorted,
            @RequestParam("maxResults") Integer maxResults,
            @RequestParam("page") Integer page){
        AttractionCoordinateRequestDto attractionCoordinateRequestDto = new AttractionCoordinateRequestDto(longitude, latitude, distance, sorted, maxResults, page);

        List<AccommodationResponseDto> data = accommodationService.getAccommodationByCoordinate(attractionCoordinateRequestDto);
        return data;
    }
}

