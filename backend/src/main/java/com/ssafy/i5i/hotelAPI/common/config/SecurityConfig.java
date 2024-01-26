package com.ssafy.i5i.hotelAPI.common.config;

import com.ssafy.i5i.hotelAPI.common.filter.ApiTokenCheckFilter;
import com.ssafy.i5i.hotelAPI.common.filter.JwtAuthenticationFilter;
import com.ssafy.i5i.hotelAPI.domain.user.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    private final TokenService tokenService;
    private final CorsConfigurationSource corsConfigurationSource;

    private static final String[] PERMIT_URL = {
            "/docs/data/**",
            "/docs/service/**",
            "/docs/email/**",
            "/docs/service",
            "/docs/service/login",
            "/api/**",
            "/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable() // Post나 Put과 같이 리소스를 변경하는 요청의 경우 내가 내보냈던 리소스에서 올라온 요청인지 확인
                .formLogin().disable()
                .headers().disable()
                .httpBasic().disable()
                .cors() //허가된 사이트나 클라이언트의 요청인지 검사하는 역할
                .and()
                //세선 사용 X ( JWT 사용 )
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //token 확인용 filter
                .addFilterBefore(new ApiTokenCheckFilter(tokenService), UsernamePasswordAuthenticationFilter.class)

                //URL 허용
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(PERMIT_URL).permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()

                //docs용 로그인 jwt 검증 필터
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }
}

