package com.ssafy.i5i.hotelAPI.domain.docs.controller;

import com.ssafy.i5i.hotelAPI.common.response.DataResponse;
import com.ssafy.i5i.hotelAPI.domain.docs.dto.ApiDataDto;
import com.ssafy.i5i.hotelAPI.domain.docs.dto.TypeResponseDto;
import com.ssafy.i5i.hotelAPI.domain.docs.dto.VariableDto;
import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiData;
import com.ssafy.i5i.hotelAPI.domain.docs.service.DocsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/docs/data")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DocsController {
    private final DocsService docsService;

    //전체 api 정보 list 반환
    @GetMapping("/apilist")
    public DataResponse<?> getApiList() {
        List<ApiDataDto.ApiDataList> data = docsService.getApiList();
        return new DataResponse<>(200, "success", data);
    }

    //특정 api에 대한 상세 변수 정보 제공
    //api 정보 제외한 요청, 응답 변수만 제공
    @GetMapping("/api/variable/{apiId}")
    public DataResponse<?> getVariable(@PathVariable("apiId") Long id) {
        List<VariableDto> data = docsService.getApiVariable(id);
        return new DataResponse<>(200, "success", data);
    }

    //특정 api에 대한 상세 정보 제공
    //api 정보 및 요청, 응답 변수 모두 제공
    @GetMapping("/api/info/{apiId}")
    public DataResponse<?> getApiInfo(@PathVariable("apiId") Long id) {
        ApiDataDto.ApiDataInfo data = docsService.getApiInfo(id);
        return new DataResponse<>(200, "success", data);
    }

    //api type 제공
    @GetMapping("/apitype")
    public DataResponse<?> getType(){
        List<TypeResponseDto> data = docsService.getTypeList();
        return new DataResponse<>(200, "success", data);
    }

    @GetMapping("/apidata/{api_type_id}")
    public DataResponse<?> getData(@PathVariable("api_type_id") Long typeId){
        List<ApiDataDto.ApiDataList> data = docsService.getApiByTypeId(typeId);
        return new DataResponse<>(200, "success", data);
    }
}
