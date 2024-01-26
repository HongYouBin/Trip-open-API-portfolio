package com.ssafy.i5i.hotelAPI.domain.docs.dto;

import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiData;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class ApiDataDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiDataList {
        private Long api_data_id;
        private Long api_type;
        private String title;
        private String name;
        private String content;
        private String method;
        private String return_type;
        private String content_type;
        private String endpoint;
        private String return_example;
        private String requestUrlExample;
        private Long apiFrontId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiDataInfo {
        private Long api_data_id;
        private String title;
        private String content;
        private String method;
        private String return_type;
        private String content_type;
        private String endpoint;
        private String return_example;
        private String requestUrlExample;
        private Long apiFrontId;
        @Builder.Default
        public List<VariableDto> variable_info = new ArrayList<>();

        public static ApiDataInfo setApiInfo(ApiData apiData) {
            ApiDataInfo apiDataInfo = ApiDataInfo.builder()
                    .api_data_id(apiData.getApiDataId())
                    .title(apiData.getTitle())
                    .content(apiData.getContent())
                    .method(apiData.getMethod())
                    .return_type(apiData.getReturnType())
                    .content_type(apiData.getContentType())
                    .endpoint(apiData.getEndpoint())
                    .return_example(apiData.getReturnExample())
                .requestUrlExample(apiData.getRequestUrlExample())
                .apiFrontId(apiData.getApiFrontId())
                    .variable_info(new ArrayList<>())
                    .build();
            apiData.getVariable().forEach((variable) -> {
                VariableDto variableDto = VariableDto.builder()
                        .title(variable.getTitle())
                        .type(variable.getType())
                        .detail(variable.getDetail())
                        .is_parameter(variable.getIsParameter())
                        .is_essential(variable.getIsEssential())
                        .is_request(variable.getIsRequest())
                        .build();
                apiDataInfo.getVariable_info().add(variableDto);
            });
            return apiDataInfo;
        }
    }

}
