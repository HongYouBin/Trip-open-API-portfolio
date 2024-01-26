package com.ssafy.i5i.hotelAPI.domain.docs.repository;

import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiDataVariable;
import com.ssafy.i5i.hotelAPI.domain.docs.entity.ApiType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApiTypeRepository extends JpaRepository<ApiType,Long> {
    List <ApiType> findAll();

//    @Query("SELECT a FROM ApiType a JOIN FETCH a.apiTypeId WHERE a.apiId.apiDataId = :apiDataId")
//    Optional<List<ApiDataVariable>> getVariableInfo(@Param("apiDataId") Long apiDataId);
}
