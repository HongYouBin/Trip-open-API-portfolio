package com.ssafy.i5i.hotelAPI.domain.user.dto;

import lombok.*;


import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class UserDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfo {
        private Long user_id;
        private String id;
        private String api_token;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginInfo {
        private String access_token;
        private UserInfo user_info;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SignUp {
        private String id;
        private String password;
        private Long code;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginDto {
        private String id;
        private String password;
    }
}
