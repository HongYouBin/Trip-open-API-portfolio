package com.ssafy.i5i.hotelAPI.common.response;

import com.ssafy.i5i.hotelAPI.domain.elastic.dto.WikiDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class WikiResponse extends CommonResponse {
    public WikiDto data;
    public WikiResponse(int code, String message) {
        super(code, message);
    }

    public WikiResponse(int code, String message, WikiDto data) {
        super(code, message);
        this.data = data;
    }
}
