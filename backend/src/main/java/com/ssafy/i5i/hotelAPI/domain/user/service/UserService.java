package com.ssafy.i5i.hotelAPI.domain.user.service;

import com.ssafy.i5i.hotelAPI.common.exception.CommonException;
import com.ssafy.i5i.hotelAPI.common.exception.ExceptionType;
import com.ssafy.i5i.hotelAPI.common.userSecurity.JwtAuthenticationProvider;
import com.ssafy.i5i.hotelAPI.common.userSecurity.JwtUtill;
import com.ssafy.i5i.hotelAPI.domain.user.dto.UserDto;
import com.ssafy.i5i.hotelAPI.domain.user.entity.Token;
import com.ssafy.i5i.hotelAPI.domain.user.entity.User;
import com.ssafy.i5i.hotelAPI.domain.user.repository.EmailRepository;
import com.ssafy.i5i.hotelAPI.domain.user.repository.TokenRedisRepository;
import com.ssafy.i5i.hotelAPI.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;
    private final TokenRedisRepository tokenRedisRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final EmailRepository emailRepository;
    private static final int TOKEN_LENGTH = 32;

    public boolean isValidToken(String tokenId) {
        User user = userRepository.findByToken(tokenId).orElse(null);
        log.info("UserService 19 lines, user = {}", user.getToken());
        if(user == null){
            return false;
        }
        return true;
    }

    public UserDto.UserInfo getUserInfo(String id) {
        User user = userRepository.selectUserById(id).orElseThrow(()->{
            throw new CommonException(ExceptionType.USER_INVALID_EXCEPTION);
        });
        UserDto.UserInfo userInfo = UserDto.UserInfo.builder()
                .user_id(user.getUserId())
                .id(user.getId())
                .api_token(user.getToken())
                .build();

        return userInfo;
    }

    //회원가입
    public void signUp(UserDto.SignUp signUp) {
        signUp.setPassword(passwordEncoder.encode(signUp.getPassword()));
        if(userRepository.selectAllUserById(signUp.getId()).isPresent()) {
            log.error("UserService signUp, userId dupplicate error");
            throw new CommonException(ExceptionType.USER_DUPLICATE_EXCEPTION);
        }
        if(emailRepository.selectAuthorizedEmailWithCode(signUp.getId(), signUp.getCode()).isEmpty()) {
            throw new CommonException(ExceptionType.USER_EMAIL_UNAUTHORIZED);
        }
        User user = User.builder()
                .id(signUp.getId())
                .password(signUp.getPassword())
                .token(generateRandomToken(signUp.getId()))
                .build();
        userRepository.save(user);
    }

    //로그인
    public UserDto.LoginInfo login(UserDto.LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());

        Authentication authentication = jwtAuthenticationProvider.authenticate(authenticationToken);
        User principal = (User) authentication.getPrincipal();

        String accessToken = JwtUtill.generateAccessToken(String.valueOf(principal.getId()));
        UserDto.UserInfo userInfo = UserDto.UserInfo.builder()
                .user_id(principal.getUserId())
                .id(principal.getId())
                .api_token(principal.getToken())
                .build();

        UserDto.LoginInfo loginInfo = new UserDto.LoginInfo(accessToken, userInfo);

        return loginInfo;
    }

    //API용 토근 재발급
    @Transactional
    public UserDto.UserInfo updateApiToken(String userId) {
        User user = userRepository.selectUserById(userId).orElseThrow(()-> {
            log.error("UserService 47 lines, invalid user info");
            throw new CommonException(ExceptionType.USER_INVALID_EXCEPTION);
        });
        String newTokenId = "";
        Token tokenFromRedis = tokenRedisRepository.findById(user.getToken()).orElse(null);
//        Token newToken = new Token();
        try {
            //새로운 토큰 생성
            newTokenId = generateRandomToken(userId);
            //새로운 토큰 하루 요청 수 redis에 저장 및 이전 토큰 레디스에서 삭제
            if(tokenFromRedis != null) {
                setRedisToken(newTokenId, tokenFromRedis);
                tokenRedisRepository.deleteById(tokenFromRedis.getToken());
            }
            //생성한 새로운 토큰 sql 업데이트
            userRepository.updateUserToken(userId, newTokenId);
            UserDto.UserInfo userInfo = UserDto.UserInfo.builder()
                    .user_id(user.getUserId())
                    .id(user.getId())
                    .api_token(newTokenId)
                    .build();

            return userInfo;
        }
        //예외 발생시 roll back
        catch(Exception e) {
            //이전 토큰으로 sql update
            userRepository.updateUserToken(userId, tokenFromRedis.getToken());
            if(tokenFromRedis != null) {
                //새로운 토큰 redis에 삭제 및 이전 토큰 redis에 저장
                tokenRedisRepository.deleteById(newTokenId);
                tokenRedisRepository.save(tokenFromRedis);
            }
            throw new CommonException(ExceptionType.TOKEN_UPDATE_EXCEPTION);
        }
    }

    public synchronized Token setRedisToken(String newTokenId, Token tokenFromRedis) {
        if (tokenFromRedis == null) return null;
        log.info("UserService 123 lines, token count = {}", tokenFromRedis.getCount());
        Token token = new Token(newTokenId, tokenFromRedis.getCount());
        tokenRedisRepository.save(token);
        return token;
    }

    //회원삭제
    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteUser(userId);
    }

    private String generateRandomToken(String userId) {
        byte[] tokenBytes = new byte[TOKEN_LENGTH];
        byte[] newIdBytes = new byte[TOKEN_LENGTH + userId.getBytes().length];

        secureRandom.nextBytes(tokenBytes);
        System.arraycopy(tokenBytes, 0, newIdBytes, 0, TOKEN_LENGTH);
        System.arraycopy(userId.getBytes(), 0, newIdBytes, TOKEN_LENGTH, userId.getBytes().length);
        return new String(Hex.encode(newIdBytes));
    }
}
