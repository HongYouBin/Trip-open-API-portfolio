package com.ssafy.i5i.hotelAPI.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS 설정에 대한 상세 설정을 담고 있는 메서드.
 * CorsConfiguration 객체를 반환하는 getCorsConfiguration 메서드를 정의.
 * http.cors()를 통해 CORS 옵션을 활성화하면 적용된다.
 */
@Configuration
public class CorsConfig {

    //허가 페이지 목록
    private List<String> allowedSite = List.of("http://localhost:3000", "http://localhost:8080", "http://localhost:5173","http://localhost:13744", "http://localhost:5000", "https://k9b209.p.ssafy.io/", "http://k9b209.p.ssafy.io/", "https://j9b209.p.ssafy.io/","https://api4u.site", "https://api4u.site/");
    private List<String> allowedHeader = List.of("*","Access-Token", "Refresh-Token"); //허용되는 헤더
    private List<String> allowedExposeHeader = List.of("*","Access-Token", "Refresh-Token"); //서버가 클라이언트에게 응답할 때 브라우저에서 노출되어야 하는 헤더(응답 헤더)를 지정


    @Bean
    @Primary
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        //corsConfigure 설정
        corsConfiguration.setAllowCredentials(true); //CORS 자격 증명 요청
        corsConfiguration.setAllowedOrigins(allowedSite); //해당 페이지 요청 허용
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT","OPTIONS")); //허용 HTTP 메서드
        corsConfiguration.setAllowedHeaders(allowedHeader);
        corsConfiguration.setExposedHeaders(allowedExposeHeader);

        //설정된 값을 urlConf에 저장
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }
}