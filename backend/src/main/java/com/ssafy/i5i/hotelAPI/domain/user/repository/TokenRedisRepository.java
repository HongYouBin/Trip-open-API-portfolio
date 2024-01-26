package com.ssafy.i5i.hotelAPI.domain.user.repository;

import com.ssafy.i5i.hotelAPI.domain.user.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRedisRepository extends CrudRepository<Token, String> {
    public Optional<Token> findById(String token);
    public void deleteById(String tokenId);
}
