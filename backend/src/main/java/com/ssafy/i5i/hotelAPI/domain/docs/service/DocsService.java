package com.ssafy.i5i.hotelAPI.domain.docs.service;

import com.ssafy.i5i.hotelAPI.common.exception.CommonException;
import com.ssafy.i5i.hotelAPI.common.exception.ExceptionType;
import com.ssafy.i5i.hotelAPI.domain.docs.dto.ApiDataDto;
import com.ssafy.i5i.hotelAPI.domain.docs.dto.TypeResponseDto;
import com.ssafy.i5i.hotelAPI.domain.docs.dto.VariableDto;
import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiData;
import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiDataVariable;
import com.ssafy.i5i.hotelAPI.domain.docs.repository.ApiDataRepository;
import com.ssafy.i5i.hotelAPI.domain.docs.repository.ApiTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ssafy.i5i.hotelAPI.common.exception.ExceptionType.VARIABLE_INVALID_EXCEPTION;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocsService {
    private final ApiDataRepository apiDataRepository;
    private final ApiTypeRepository apiTypeRepository;

    public List<VariableDto> getApiVariable(Long apiId) {
        List<ApiDataVariable> variableInfo = apiDataRepository.getVariableInfo(apiId).orElseThrow(()->{
            throw new CommonException(VARIABLE_INVALID_EXCEPTION);
        });
        List<VariableDto> variableDtoList = new ArrayList<>();

        variableInfo.forEach((variable) -> {
            VariableDto variableDto = VariableDto.builder()
                    .title(variable.getTitle())
                    .type(variable.getType())
                    .detail(variable.getDetail())
                    .is_essential(variable.getIsEssential())
                    .is_parameter(variable.getIsParameter())
                    .is_request(variable.getIsRequest())
                    .build();
            variableDtoList.add(variableDto);
        });
        return variableDtoList;
    }

    public List<ApiDataDto.ApiDataList> getApiList() {
        return apiDataRepository.findAll()
                .stream()
                .map(api -> {
                    return ApiDataDto.ApiDataList.builder()
                            .api_data_id(api.getApiDataId())
                            .api_type(api.getApiType().getApiTypeId())
                            .title(api.getTitle())
                            .content(api.getContent())
                            .name(api.getName())
                            .method(api.getMethod())
                            .return_type(api.getReturnType())
                            .content_type(api.getContentType())
                            .endpoint(api.getEndpoint())
                            .return_example(api.getReturnExample())
                            .requestUrlExample(api.getRequestUrlExample())
                            .apiFrontId(api.getApiFrontId())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public ApiDataDto.ApiDataInfo getApiInfo(Long apiId) {
        log.info("DocsService getApiInfo, apiId = {}", apiId);
        ApiData apiInfo = apiDataRepository.getApiInfo(apiId).orElseThrow(()->{
            throw new CommonException(ExceptionType.DATA_INVALID_EXCEPTION);
        });

        ApiDataDto.ApiDataInfo apiDataInfo = ApiDataDto.ApiDataInfo.setApiInfo(apiInfo);
        return apiDataInfo;
    }

    public List<TypeResponseDto> getTypeList(){
        return apiTypeRepository.findAll()
                .stream()
                .map(apiType -> {return apiType.toDto();})
                .collect(Collectors.toList());
    }

    public List<ApiDataDto.ApiDataList> getApiByTypeId(Long typeId){
        List<ApiData> apiDataList = apiDataRepository.getDataByType(typeId).orElseThrow(()->{
            throw new CommonException(ExceptionType.TYPE_INVALID_EXCEPTION);
        });
        return apiDataList.stream()
                .map(apiData -> {
                    return ApiDataDto.ApiDataList.builder()
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
                            .build();
                })
                .collect(Collectors.toList());
    }
}
