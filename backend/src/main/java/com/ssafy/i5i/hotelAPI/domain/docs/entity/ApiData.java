package com.ssafy.i5i.hotelAPI.domain.docs.entity;

import com.ssafy.i5i.hotelAPI.domain.docs.dto.VariableDto;
import com.ssafy.i5i.hotelAPI.domain.docs.dto.ApiDataDto;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name="api_data")
public class ApiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_id")
    private Long apiDataId;

    @JoinColumn(name = "api_type_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ApiType apiType;

    @OneToMany(mappedBy = "apiId", fetch = FetchType.LAZY)
    private List<ApiDataVariable> variable = new ArrayList<>();

    @Column(length = 100)
    private String title;
    @Column(length = 1000)
    private String content;

    @Column(length = 10)
    private String method;

    private String name;
    @Column(name="return_type", length = 100)
    private String returnType;

    @Column(name="content_type", length = 100)
    private String contentType;

    @Column(name = "endpoint", length = 1000)
    private String endpoint;

    @Column(name = "return_example", columnDefinition = "TEXT")
    private String returnExample;

    @Column(name= "request_url_example")
    private String requestUrlExample;

    @Column(name = "api_front_id")
    private Long apiFrontId;
}
