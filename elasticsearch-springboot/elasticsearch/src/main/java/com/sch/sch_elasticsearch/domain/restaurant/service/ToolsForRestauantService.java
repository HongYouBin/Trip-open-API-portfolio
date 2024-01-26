package com.sch.sch_elasticsearch.domain.restaurant.service;

import com.sch.sch_elasticsearch.domain.restaurant.dto.ResponseRestaurantDto;
import com.sch.sch_elasticsearch.domain.restaurant.entity.Restaurant;
import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToolsForRestauantService {

    public static final Double EARTH_RADIUS = 6371.0;

    /**
     * 두 좌표를 입력받아, KM 차이를 반환한다.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat/2)* Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))* Math.cos(Math.toRadians(lat2))* Math.sin(dLon/2)* Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d =EARTH_RADIUS* c;
        return d;
    }

    /**
     * 초기 위경도를 입력받고, km 범위를 입력받아 최소 최대 위경도값을 반환한다.
     * @param lat 초기 위도
     * @param lon 초기 경도
     * @param distance KM 단위의 거리
     * @return 위도와 경도의 범위를 포함하는 배열
     */
    public static double[][] calculateLatLonRange(double lat, double lon, double distance) {
        final double R = EARTH_RADIUS; // 지구의 평균 반지름 (km)

        double latChange = Math.toDegrees(distance / R);
        double lonChange = Math.toDegrees(distance / (R * Math.cos(Math.toRadians(lat))));

        double minLat = lat - latChange;
        double maxLat = lat + latChange;
        double minLon = lon - lonChange;
        double maxLon = lon + lonChange;

        return new double[][]{{minLat, minLon}, {maxLat, maxLon}};
    }

    /**
     * typeNum 입력을 통해 필드 용어를 리턴한다.
     * @param typeNum
     * @return
     */
    public String getType(int typeNum) {
        String type = "";
        switch (typeNum) {
            case 0:
                type = "food_name.keyword";
                break;
            case 1:
                type = "food_name";
                break;
            case 2:
                type = "food_type_main";
                break;
            case 3:
                type = "food_type_sub";
                break;
            default:
                throw new CommonException(ExceptionType.RESTAURANT_TYPENUM_IS_INVALID);
        }
        return type;
    }

    /**
     * SearchHits의 값을 전처리를 거쳐 List<ResponseRestaurantDto> 값으로 출력
     * @param SearchHits<Restaurant> result
     * @return List<ResponseRestaurantDto>
     */
    public List<ResponseRestaurantDto> getListBySearchHits(SearchHits<Restaurant> result) {
        // SearchHits 내부 Content를 List<Wiki>로 변환
        List<ResponseRestaurantDto> restaurantDtoList = new ArrayList<>();
        for (SearchHit<Restaurant> hit : result) {
            ResponseRestaurantDto responseRestaurantDto = new ResponseRestaurantDto();
            responseRestaurantDto.setScore(hit.getScore());
            responseRestaurantDto.setRestaurantId(Integer.parseInt(hit.getContent().getFoodId()));
            responseRestaurantDto.setRestaurantName(hit.getContent().getFoodName());
            responseRestaurantDto.setRestaurantTypeMain(hit.getContent().getFoodTypeMain());
            responseRestaurantDto.setRestaurantTypeSub(hit.getContent().getFoodTypeSub());
            responseRestaurantDto.setRestaurantLat(hit.getContent().getFoodLatitude());
            responseRestaurantDto.setRestaurantLng(hit.getContent().getFoodLongitude());
            responseRestaurantDto.setRestaurantLike(hit.getContent().getFoodLike());
            responseRestaurantDto.setRestaurantScore(hit.getContent().getFoodScore());
            responseRestaurantDto.setRestaurantStar(hit.getContent().getFoodStar());
            responseRestaurantDto.setRestaurantStarUser(hit.getContent().getFoodStarUser());
            restaurantDtoList.add(responseRestaurantDto);
        }
        return restaurantDtoList;
    }

}
