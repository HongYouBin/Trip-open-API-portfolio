package com.ssafy.i5i.hotelAPI.domain.hotel.service;

import com.ssafy.i5i.hotelAPI.common.exception.CommonException;
import com.ssafy.i5i.hotelAPI.common.exception.ExceptionType;
import com.ssafy.i5i.hotelAPI.domain.elastic.dto.ResponseWikiDto;
import com.ssafy.i5i.hotelAPI.domain.elastic.service.ElasticService;
import com.ssafy.i5i.hotelAPI.domain.hotel.entity.Accommodation;
import com.ssafy.i5i.hotelAPI.domain.hotel.entity.Attraction;
import com.ssafy.i5i.hotelAPI.domain.hotel.dto.request.AttractionCoordinateRequestDto;
import com.ssafy.i5i.hotelAPI.domain.hotel.dto.request.AttractionNameRequestDto;
import com.ssafy.i5i.hotelAPI.domain.hotel.dto.response.AccommodationResponseDto;
import com.ssafy.i5i.hotelAPI.domain.hotel.entity.AttractionAccommodation;
import com.ssafy.i5i.hotelAPI.domain.hotel.repository.AccommodationRepository;
import com.ssafy.i5i.hotelAPI.domain.hotel.repository.AttractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class  AccommodationService {
    private final AttractionRepository attractionRepository;
    private final AccommodationRepository accommodationRepository;
    private final ElasticService elasticService;

    public static final Double RADIUS_OF_EARTH = 6371.0;

    // sort
    public List<AccommodationResponseDto> sort(List<AccommodationResponseDto> data, String type){
        if(type.equalsIgnoreCase("distance")){
            data.sort((o1, o2) -> (o1.getRelativeDistance().compareTo(o2.getRelativeDistance())));
        } else if (type.equalsIgnoreCase("score")) {
            data.sort(((o1, o2) -> o1.getAccommodationScore().compareTo(o2.getAccommodationScore())));
        } else if (type.equalsIgnoreCase("type")) {
            data.sort(((o1, o2) -> o1.getAccommodationType().compareTo(o2.getAccommodationType())));
        } else{
            throw new CommonException(ExceptionType.SORTED_TYPE_EXCEPTION);
        }
        return data;
    }

    // input: attraction_id -> output: Accommodation
    public List<AccommodationResponseDto> getAccommodationByName(AttractionNameRequestDto requestDto){
        if(requestDto.getPage() <= 0 || requestDto.getMaxResults() <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        ResponseWikiDto wiki = elasticService.searchFuzzyAndNgram(requestDto.getAttractionName(),1,1,false,true).get(0);
        requestDto.setAttractionName(wiki.getAttractionName());

        Attraction attraction = attractionRepository.findTopByTitle(requestDto.getAttractionName())
                .orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION));
        System.out.println(attraction.getTitle());
        System.out.println(wiki.getAttractionName());
        //현재 위도 좌표 (y 좌표)
        double nowLatitude = attraction.getLatitude();
        //현재 경도 좌표 (x 좌표)
        double nowLongitude = attraction.getLongitude();

        //km당 y 좌표 이동 값
        double mForLatitude =(1 /(RADIUS_OF_EARTH* 1 *(Math.PI/ 180)));
        //km당 x 좌표 이동 값
        double mForLongitude =(1 /(RADIUS_OF_EARTH* 1 *(Math.PI/ 180)* Math.cos(Math.toRadians(nowLatitude))));

        //현재 위치 기준 검색 거리 좌표
        double maxY = nowLatitude +(requestDto.getDistance()* mForLatitude);
        double minY = nowLatitude -(requestDto.getDistance()* mForLatitude);
        double maxX = nowLongitude +(requestDto.getDistance()* mForLongitude);
        double minX = nowLongitude -(requestDto.getDistance()* mForLongitude);

        List<AccommodationResponseDto> response = accommodationRepository.findByCoordinate(maxY, maxX, minY, minX)
                .orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION))
                .stream()
                .map(data -> {
                    AccommodationResponseDto now = data.toDto();
                    now.setRelativeDistance(calculateDistance(nowLatitude, nowLongitude, now.getAccommodationLatitude(), now.getAccommodationLongitude()));
                    now.setAttractionName(wiki.getAttractionName());
                    return now;
                })
                .filter(dto -> dto.getRelativeDistance() < requestDto.getDistance())
                .collect(Collectors.toList());
        int fromIndex = (requestDto.getPage() - 1) * requestDto.getMaxResults();

        if(response.isEmpty()) throw new CommonException(ExceptionType.NULL_POINT_EXCEPTION);

        return sort(response, requestDto.getSorted()).subList(fromIndex, Math.min(fromIndex + requestDto.getMaxResults(),response.size()));
    }

    // input: coordinate -> output: Accommodation
    public List<AccommodationResponseDto> getAccommodationByCoordinate(AttractionCoordinateRequestDto requestDto){
        if(requestDto.getPage() <= 0 || requestDto.getMaxResults() <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);
        //현재 위도 좌표 (y 좌표)
        double nowLatitude = requestDto.getLatitude();
        //현재 경도 좌표 (x 좌표)
        double nowLongitude = requestDto.getLongitude();

        //km당 y 좌표 이동 값
        double mForLatitude =(1 /(RADIUS_OF_EARTH* 1 *(Math.PI/ 180)));
        //km당 x 좌표 이동 값
        double mForLongitude =(1 /(RADIUS_OF_EARTH* 1 *(Math.PI/ 180)* Math.cos(Math.toRadians(nowLatitude))));

        //현재 위치 기준 검색 거리 좌표
        double maxY = nowLatitude +(requestDto.getDistance()* mForLatitude);
        double minY = nowLatitude -(requestDto.getDistance()* mForLatitude);
        double maxX = nowLongitude +(requestDto.getDistance()* mForLongitude);
        double minX = nowLongitude -(requestDto.getDistance()* mForLongitude);

        List<AccommodationResponseDto> response = accommodationRepository.findByCoordinate(maxY, maxX, minY, minX)
                .orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION))
                .stream()
                .map(data -> {
                    AccommodationResponseDto now = data.toDto();
                    now.setRelativeDistance(calculateDistance(nowLatitude, nowLongitude, now.getAccommodationLatitude(), now.getAccommodationLongitude()));
                    return now;
                })
                .filter(dto -> dto.getRelativeDistance() < requestDto.getDistance())
                .collect(Collectors.toList());

        int fromIndex = (requestDto.getPage() - 1) * requestDto.getMaxResults();

        if(response.isEmpty()) throw new CommonException(ExceptionType.NULL_POINT_EXCEPTION);

        return sort(response, requestDto.getSorted()).subList(fromIndex, Math.min(fromIndex + requestDto.getMaxResults(),response.size()));

    }

    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        // 위도와 경도를 사용하여 거리를 계산하는 로직을 구현
        Double lat1Rad = lat1 * Math.PI / 180;
        Double lon1Rad = lon1 * Math.PI / 180;
        Double lat2Rad = lat2 * Math.PI / 180;
        Double lon2Rad = lon2 * Math.PI / 180;

        // Haversine 공식을 사용하여 거리 계산
        Double dLat = Math.abs(lat2Rad - lat1Rad);
        Double dLon = Math.abs(lon2Rad - lon1Rad);
        Double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = RADIUS_OF_EARTH * c;

        return distance;
    }

//    List<AccommodationResponseDto> response = attractionAccommdodationRepository.findByTitle(requestDto.getAttractionName())
//            .orElseThrow(() -> {throw new CommonException(ExceptionType.NULL_POINT_EXCEPTION);})
//            .stream()
//            .map(data -> {
//                AccommodationResponseDto now = data.getAccommodation().toDto();
//                now.setRelativeDistance(calculateDistance(data.getAttraction().getLatitude(), data.getAttraction().getLongitude(), now.getAccommodationLatitude(), now.getAccommodationLongitude()));
//                return now;
//            })
//            .filter(dto -> dto.getRelativeDistance() < requestDto.getDistance())
//            .collect(Collectors.toList());
//
//    // sort
//        return sort(response, requestDto.getSorted());
//}
//
//    // input: coordinate -> output: Accommodation
//    public List<AccommodationResponseDto> getAccommodationByCoordinate(AttractionCoordinateRequestDto requestDto){
//        //현재 위도 좌표 (y 좌표)
//        double nowLatitude = requestDto.getLatitude();
//        //현재 경도 좌표 (x 좌표)
//        double nowLongitude = requestDto.getLongitude();
//
//        //km당 y 좌표 이동 값
//        double mForLatitude =(1 /(RADIUS_OF_EARTH* 1 *(Math.PI/ 180)));
//        //km당 x 좌표 이동 값
//        double mForLongitude =(1 /(RADIUS_OF_EARTH* 1 *(Math.PI/ 180)* Math.cos(Math.toRadians(nowLatitude))));
//
//        //현재 위치 기준 검색 거리 좌표
//        double maxY = nowLatitude +(requestDto.getDistance()* mForLatitude);
//        double minY = nowLatitude -(requestDto.getDistance()* mForLatitude);
//        double maxX = nowLongitude +(requestDto.getDistance()* mForLongitude);
//        double minX = nowLongitude -(requestDto.getDistance()* mForLongitude);
//
//        List<AccommodationResponseDto> response = attractionAccommdodationRepository.findByCoordinate(maxY, maxX, minY, minX)
//                .orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION))
//                .stream()
//                .map(data -> {
//                    AccommodationResponseDto now = data.toDto();
//                    now.setRelativeDistance(calculateDistance(requestDto.getLatitude(), requestDto.getLongitude(), now.getAccommodationLatitude(), now.getAccommodationLongitude()));
//                    return now;
//                })
//                .filter(dto -> dto.getRelativeDistance() < requestDto.getDistance())
//                .collect(Collectors.toList());
//
//        return sort(response, requestDto.getSorted());
//    }

}

