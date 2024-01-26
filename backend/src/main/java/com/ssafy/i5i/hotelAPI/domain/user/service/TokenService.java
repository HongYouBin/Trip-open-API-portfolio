package com.ssafy.i5i.hotelAPI.domain.user.service;

import com.ssafy.i5i.hotelAPI.domain.user.entity.Token;
import com.ssafy.i5i.hotelAPI.domain.user.entity.User;
import com.ssafy.i5i.hotelAPI.domain.user.repository.TokenRedisRepository;
import com.ssafy.i5i.hotelAPI.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final TokenRedisRepository tokenRedisRepository;
    private final UserRepository userRepository;
//    private final RedissonClient redissonClient;

//    @Transactional
//    public boolean checkValidToken(String tokenId) {
//        boolean flag = false;
//        RLock lock = redissonClient.getLock(tokenId);
//        try {
//            boolean isLocked = lock.tryLock(5, 5, TimeUnit.SECONDS);
//            if(!isLocked) {
//                throw new Exception();
//            }
//            Token tokenFromRedis = tokenRedisRepository.findById(tokenId).orElse(null);
//            if (tokenFromRedis == null) {
//                Optional<User> user = userRepository.findByToken(tokenId);
//                if (user.isPresent()) {
//                    saveToken(user.get().getToken());
//                    incrementTokenCount(user.get().getToken());
//                    flag = true;
//                }
//            }
//            else {
//                log.info("TokenService 23 lines, count = {}", tokenFromRedis.getCount());
//                if (tokenFromRedis.getCount() < 100000) {
//                    incrementTokenCount(tokenFromRedis.getToken());
//                    flag = true;
//                }
//            }
//        }
//        catch (Exception e) {
//
//        }
//        finally {
//            try {
//                lock.unlock();   // (4)
//            } catch (IllegalMonitorStateException e) {
//                log.info("Redisson Lock Already UnLock");
//            }
//        }
//        return flag;
//    }

    @Transactional(readOnly = true)
    public synchronized boolean checkValidToken(String tokenId) {
        Token tokenFromRedis = tokenRedisRepository.findById(tokenId).orElse(null);
        if (tokenFromRedis == null) {
            log.info("TokenService 69 lines, tokenId = {}", tokenId);
            Optional<User> user = userRepository.findByToken(tokenId);
            if (!user.isPresent()) return false;
            saveToken(user.get().getToken());
            incrementTokenCount(user.get().getToken());
            return true;
        }
        log.info("TokenService 23 lines, count = {}", tokenFromRedis.getCount());
        if (tokenFromRedis.getCount() < 10000000) {
            incrementTokenCount(tokenFromRedis.getToken());
            return true;
        }
        return false;
    }

    public boolean incrementTokenCount(String tokenId) {
        Token token = tokenRedisRepository.findById(tokenId).orElse(null);
        if (token != null) {
            token.setCount(token.getCount() + 1);
            tokenRedisRepository.save(token);
            return true;
        }
        return false;
    }
    public boolean saveToken(String tokenId) {
        Token token = new Token(tokenId, 0);
        tokenRedisRepository.save(token);
        return true;
    }
}
