package com.ssafy.i5i.hotelAPI.domain.food.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ssafy.i5i.hotelAPI.domain.elastic.dto.ResponseWikiDto;
import com.ssafy.i5i.hotelAPI.domain.elastic.service.ElasticService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.i5i.hotelAPI.common.exception.CommonException;
import com.ssafy.i5i.hotelAPI.common.exception.ExceptionType;
import com.ssafy.i5i.hotelAPI.domain.food.dto.request.FoodRequestDto;
import com.ssafy.i5i.hotelAPI.domain.food.dto.response.FoodResponseDto;
import com.ssafy.i5i.hotelAPI.domain.food.repository.FoodRepository;
import com.ssafy.i5i.hotelAPI.domain.hotel.entity.Attraction;
import com.ssafy.i5i.hotelAPI.domain.hotel.repository.AttractionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FoodServiceImpl implements FoodService{

	private final FoodRepository foodRepository;
	private final AttractionRepository attractionRepository;
	private final ElasticService elasticService;

	public static final Double EARTH_RADIUS = 6371.0;
	@Override
	// public List<FoodResponseDto.TitleD> getFoodFromTravle(FoodRequestDto.Title requestDto) {
	// 	log.info("title : {}",requestDto.getAttractionName());
	// 	return foodRepository.getFoodFromTravle(requestDto.getAttractionName())
	// 		.orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION))
	// 		.stream()
	// 		.map(data -> {
	// 			FoodResponseDto.TitleD now = data.convertToFDto();
	// 			now.setDistance(calculateDistance(now.getAttractionLatitude(), now.getAttractionLongitude(), now.getFoodLatitude(), now.getFoodLongitude()));
	// 			return now;
	// 		})
	// 		.filter(dto -> dto.getDistance() < requestDto.getDistance())
	// 		.sorted(getFoodTitleComparator(requestDto.getSorted()))
	// 		.collect(Collectors.toList());
	// }
	public List<FoodResponseDto.Coordi> getFoodFromTravle(FoodRequestDto.Title requestDto) {
		if(requestDto.getPage() <= 0 || requestDto.getMaxResults() <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);

		ResponseWikiDto wiki = elasticService.searchFuzzyAndNgram(requestDto.getAttractionName(),1,1,false, true).get(0);
		requestDto.setAttractionName(wiki.getAttractionName());

		Attraction attraction = attractionRepository.findTopByTitle(requestDto.getAttractionName())
				.orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION));

		//현재 위도 좌표 (y 좌표)
		double nowLatitude = attraction.getLatitude();

		//현재 경도 좌표 (x 좌표)
		double nowLongitude = attraction.getLongitude();

		//km당 y 좌표 이동 값
		double mForLatitude =(1 /(EARTH_RADIUS* 1 *(Math.PI/ 180)));
		//km당 x 좌표 이동 값
		double mForLongitude =(1 /(EARTH_RADIUS* 1 *(Math.PI/ 180)* Math.cos(Math.toRadians(nowLatitude))));

		//현재 위치 기준 검색 거리 좌표
		double maxY = nowLatitude +(requestDto.getDistance()* mForLatitude);
		double minY = nowLatitude -(requestDto.getDistance()* mForLatitude);
		double maxX = nowLongitude +(requestDto.getDistance()* mForLongitude);
		double minX = nowLongitude -(requestDto.getDistance()* mForLongitude);
		List<FoodResponseDto.Coordi> response = foodRepository.getFoodFromLngLatv(maxY, maxX, minY, minX)
				.orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION))
				.stream()
				.map(data -> {
					FoodResponseDto.Coordi now = data.convertToDto();
					now.setDistance(calculateDistance(attraction.getLatitude(), attraction.getLongitude(), now.getRestaurantLatitude(), now.getRestaurantLongitude()));
					now.setAttractionName(wiki.getAttractionName());
					return now;
				})
				.filter(dto -> dto.getDistance() < requestDto.getDistance())
				.sorted(getFoodCoordiComparator(requestDto.getSorted()))
				.collect(Collectors.toList());

		int fromIndex = (requestDto.getPage() - 1) * requestDto.getMaxResults();

		if(response.isEmpty()) throw new CommonException(ExceptionType.NULL_POINT_EXCEPTION);

		return response.subList(fromIndex, Math.min(fromIndex + requestDto.getMaxResults(),response.size()));
	}

	@Override
	public List<FoodResponseDto.Coordi> getFoodFromLngLatv(FoodRequestDto.Coordi requestDto) {
		if(requestDto.getPage() <= 0 || requestDto.getMaxResults() <= 0) throw new CommonException(ExceptionType.PAGE_MAXRESULTS_EXCEPTION);

		//현재 위도 좌표 (y 좌표)
		double nowLatitude = requestDto.getLatitude();

		//현재 경도 좌표 (x 좌표)
		double nowLongitude = requestDto.getLongitude();

		//km당 y 좌표 이동 값
		double mForLatitude =(1 /(EARTH_RADIUS* 1 *(Math.PI/ 180)));
		//km당 x 좌표 이동 값
		double mForLongitude =(1 /(EARTH_RADIUS* 1 *(Math.PI/ 180)* Math.cos(Math.toRadians(nowLatitude))));

		//현재 위치 기준 검색 거리 좌표
		double maxY = nowLatitude +(requestDto.getDistance()* mForLatitude);
		double minY = nowLatitude -(requestDto.getDistance()* mForLatitude);
		double maxX = nowLongitude +(requestDto.getDistance()* mForLongitude);
		double minX = nowLongitude -(requestDto.getDistance()* mForLongitude);


		List<FoodResponseDto.Coordi> response = foodRepository.getFoodFromLngLatv(maxY, maxX, minY, minX)
				.orElseThrow(() -> new CommonException(ExceptionType.NULL_POINT_EXCEPTION))
				.stream()
				.map(data -> {
					FoodResponseDto.Coordi now = data.convertToDto();
					now.setDistance(calculateDistance(requestDto.getLatitude(), requestDto.getLongitude(),  now.getRestaurantLatitude(), now.getRestaurantLongitude()));
					return now;
				})
				.filter(dto -> dto.getDistance() < requestDto.getDistance())
				.sorted(getFoodCoordiComparator(requestDto.getSorted()))
				.collect(Collectors.toList());

		int fromIndex = (requestDto.getPage() - 1) * requestDto.getMaxResults();

		if(response.isEmpty()) throw new CommonException(ExceptionType.NULL_POINT_EXCEPTION);

		return response.subList(fromIndex, Math.min(fromIndex + requestDto.getMaxResults(),response.size()));
	}

	//두 지점 간의 거리 계산
	public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(dLat/2)* Math.sin(dLat/2)
				+ Math.cos(Math.toRadians(lat1))* Math.cos(Math.toRadians(lat2))* Math.sin(dLon/2)* Math.sin(dLon/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d =EARTH_RADIUS* c;
		return d;
	}

	//정렬
	private Comparator<FoodResponseDto.Coordi> getFoodCoordiComparator(String sortingKey) {
		if(sortingKey.isEmpty() || sortingKey == null || sortingKey.equalsIgnoreCase("distance")){
			return Comparator.comparing(FoodResponseDto.Coordi::getDistance);
		}
		else if ("like".equalsIgnoreCase(sortingKey)) {
			return Comparator.comparing(FoodResponseDto.Coordi::getRestaurantLike);
		} else if ("score".equalsIgnoreCase(sortingKey)) {
			return Comparator.comparing(FoodResponseDto.Coordi::getRestaurantScore);
		} else if ("star".equalsIgnoreCase(sortingKey)) {
			return Comparator.comparing(FoodResponseDto.Coordi::getRestaurantStar);
		}else {
			throw new CommonException(ExceptionType.SORTED_TYPE_EXCEPTION);
		}
	}

	// private Comparator<FoodResponseDto.TitleD> getFoodTitleComparator(String sortingKey) {
	// 	if(sortingKey.isEmpty() || sortingKey == null || sortingKey.equals("DISTANCE")){
	// 		return Comparator.comparing(FoodResponseDto.TitleD::getDistance);
	// 	}
	// 	else if ("JJIM".equals(sortingKey)) {
	// 		return Comparator.comparing(FoodResponseDto.TitleD::getFoodJjim);
	// 	} else if ("SCORE".equals(sortingKey)) {
	// 		return Comparator.comparing(FoodResponseDto.TitleD::getFoodScore);
	// 	} else if ("STAR".equals(sortingKey)) {
	// 		return Comparator.comparing(FoodResponseDto.TitleD::getFoodStar);
	// 	} else {
	// 		throw new CommonException(ExceptionType.SORTED_TYPE_EXCEPTION);
	// 	}
	// }
}

