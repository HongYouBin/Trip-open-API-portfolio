package com.ssafy.i5i.hotelAPI.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
//24시간 동안 토큰 정보 저장
@RedisHash(value = "token", timeToLive = 86400L)
@AllArgsConstructor
public class Token {
    @Id
    private String token;
    private Integer count;
}
