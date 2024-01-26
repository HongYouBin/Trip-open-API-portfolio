package com.ssafy.i5i.hotelAPI.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    /**
     * MESSAGE : 예외 메시지
     */
    //3000번은 Docs와 관련된 에러
    USER_WRONGPASSWORD_EXCEPTION(3000,"비밀번호가 틀렸습니다."),
    USER_INVALID_EXCEPTION(3001, "유저 정보가 없습니다"),
    USER_DUPLICATE_EXCEPTION(3002, "유저가 정보가 있습니다."),
    USER_EMAIL_UNAUTHORIZED(3003, "이메일 인증이 되지 않았거나 인증 코드가 잘못되었습니다."),
    //4000번은 jwt 와 관련된 에러
    JWT_TOKEN_EXPIRED(4000, "토큰이 만료되었습니다."),
    JWT_PARSER_FAILED(4001, "토큰 파싱에 실패했습니다."),
    //5000번은 데이터와 관련된 에러
    NULL_POINT_EXCEPTION(5001, "데이터가 없습니다."),
    SORTED_TYPE_EXCEPTION(5002,"정렬 타입이 잘못되었습니다."),
    PAGE_MAXRESULTS_EXCEPTION(5003,"페이지네이션 or 최대 데이터 수가 잘못되었습니다."),
    //6000번은 api token 관련 에러

    API_TOKEN_EXCEPTION(6000, "pk와 일치하는 api 토큰이 없습니다."),
    TOKEN_UPDATE_EXCEPTION(6001, "api 토큰을 업데이트를 실패했습니다"),

    //7000번 api docs 데이터 에러
    DATA_INVALID_EXCEPTION(7001, "api docs 정보가 없습니다."),
    VARIABLE_INVALID_EXCEPTION(7002, "api 변수 정보가 없습니다."),
    TYPE_INVALID_EXCEPTION(7003, "type에 맞는 api data 정보가 없습니다"),

    //8000번 웹 클라이언트 에러
    WEBCLIENT_INVALID_EXCEPTION(8001, "WebClient 데이터 손실이 있습니다"),
    SEARCH_NODATA_EXCEPTION(8002,"검색 결과가 없습니다."),
    //9000번 email 에러
    EMAIL_EXIST_EXCEPTION(9000, "이미 회원가입된 이메일입니다"),
    EMAIL_SEND_FAIL(9001, "이메일 전송에 실패하였습니다"),
    EMAIL_INVALID_EXCEPTION(9002, "등록된 이메일이 없습니다."),
    CODE_INVALID_EXCEPTION(9003, "코드 번호가 틀렸습니다"),
    CODE_TIME_FAIL(9004, "코드 시간이 초과되었습니다. 다시 이메일 인증을 해주세요"),
    EMAIL_AUTH_FAIL(9005, "이메일 인증에 실패하였습니다.")
    ;

    private final int code;
    private final String message;
}
