package com.ssafy.i5i.hotelAPI.domain.docs.repository;

import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiData;
import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiDataVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ApiDataRepository extends JpaRepository<ApiData, Long> {
    List <ApiData> findAll();

    @Query("SELECT a FROM ApiData a JOIN FETCH a.variable WHERE a.apiDataId = :apiDataId")
    Optional<ApiData> getApiInfo(@Param("apiDataId") Long apiDataId);

    @Query("SELECT a FROM ApiDataVariable a JOIN FETCH a.apiId WHERE a.apiId.apiDataId = :apiDataId")
    Optional<List<ApiDataVariable>> getVariableInfo(@Param("apiDataId") Long apiDataId);

    @Query("SELECT a FROM ApiData a JOIN FETCH a.apiType WHERE a.apiType.apiTypeId = :apiTypeId")
    Optional<List<ApiData>> getDataByType(@Param("apiTypeId") Long apiTypeId);
}
