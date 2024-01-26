package com.ssafy.i5i.hotelAPI.domain.docs.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VariableDto {
    private String title;
    private String type;
    private String detail;
    private Boolean is_request;
    private Boolean is_parameter;
    private Boolean is_essential;
}
