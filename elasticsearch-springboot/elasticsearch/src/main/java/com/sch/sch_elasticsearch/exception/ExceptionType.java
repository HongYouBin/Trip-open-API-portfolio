package com.sch.sch_elasticsearch.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {
    /**
     * CODE : 4자리 양의 정수
     * MESSAGE : 예외 메시지
     */

    /** +========= Wiki Domain : 0xxx =========+ **/
    /** ETC : 00xx **/
    /** WikiTitleController**/
    WIKI_AGGREGATE_TITLE_SEARCH_FAIL(0001, "제목 검색 : 제목 일치 or (Fuzzy + ngram) 에 실패하였습니다."),
    /** ToolsForWikiService **/
    WIKI_TYPENUM_IS_INVALID(0002, "키워드 검색용 TypeNum 번호가 유효하지 않습니다."),

    /** WikiServiceBasic : 01xx **/
    WIKI_SEARCH_EXACT_FAIL(0101, "키워드 일치 검색에 실패하였습니다. (searchExact)"),
    WIKI_SEARCH_PARTIAL_FAIL(0102, "부분 일치 검색에 실패하였습니다. (searchPartial)"),
    WIKI_INSERT_FAIL(0103, "새로운 위키 저장에 실패하였습니다."),

    /** WikiServiceExtend : 02xx **/
    WIKI_GET_SAME_ATTNAME_AND_WIKITITLE_FAIL(0201, "동일 제목 결과를 찾는 데 실패하였습니다."),
    WIKI_FUZZINESS_SEARCH_FAIL(0202, "fuzziness 검색에 실패하였습니다."),
    WIKI_SEARCH_ALL_BY_DTO_FAIL(0203, "가장 유사도가 높은 내용을 검색하는 데 실패하였습니다. "),
    WIKI_SEARCH_ALL_BY_PARAM_FAIL(0204, "가장 유사도가 높은 내용을 검색하는 데 실패하였습니다. "),

    /** WikiServiceTermSimilary : 03xx **/
    WIKI_USE_ANALYZER_AND_GET_TERMS_FAIL(0301, "분석기를 사용한 용어 단위 분석이 실패했습니다."),
    WIKI_FIND_WIKI_NOT_EXIST_TERMS(0302, "Term 필드가 Null인 도큐먼트들을 가져오는데 실패했습니다."),
    WIKI_CHECK_CONTENT_IS_NULL(0303,
            "term 필드가 null인 값 중 wiki_countent가 빈 값들의 term 값 삽입을 실패했습니다."),
    WIKI_CALCULATE_SIMILARY_OVERVIEW_AND_CONTENT(0304, "필드 간의 문장 유사성을 비교하는 데 실패했습니다."),
    WIKI_UPDATE_NULL_TERMS_FAIL(0304, "새로운 term 계산 값 갱신에 실패하였습니다." ),

    /** WikiServiceTitle : 04xx**/
    WIKI_SEARCH_CORRECT_TITLE_FAIL(0401, "일치하는 제목을 찾는 중 오류가 발생했습니다." ),
    WIKI_SEARCH_FUZZY_TITLE_FAIL(0402, "Fuzzyniss 제목 유사 검색이 실패하였습니다."),
    WIKI_SEARCH_NGRAM_TITLE_FAIL(0403, "Ngram 제목 유사 검색이 실패하였습니다."),
    WIKI_SEARCH_FUZZY_AND_NGRAM_TITLE_FAIL(0404, "Fuzzy 검색과 Ngram 통합 검색을 집계하는 과정이 실패했습니다."),




    /** +========= Restaurant Domain : 1xxx =========+ **/
    /** ETC : 00xx **/
    /* RestaurantTitleController */
    RESTAURANT_AGGREGATE_TITLE_SEARCH_FAIL(1001, "제목 검색 : 제목 일치 or (Fuzzy + ngram) 에 실패하였습니다."),
    /*ToolsForRestaurantService */
    RESTAURANT_TYPENUM_IS_INVALID(1002, "키워드 검색용 TypeNum 번호가 유효하지 않습니다."),

    /** RestaurantServiceBasic : 11xx **/
    RESTAURANT_SEARCH_EXACT_RESTAURANT_NAME_FAIL(1102, "일치하는 제목을 찾는 중 오류가 발생했습니다." ),
    RESTAURANT_SEARCH_PARTIAL_FAIL(1102, "부분 일치 검색에 실패하였습니다. (searchPartial)"),

    /** RestaurantServiceExtend : 12xx **/
//    RESTAURANT_GET_SAME_ATTNAME_AND_WIKITITLE_FAIL(0201, "동일 제목 결과를 찾는 데 실패하였습니다."),
    RESTAURANT_FUZZINESS_SEARCH_FAIL(1202, "fuzziness 검색에 실패하였습니다."),
    RESTAURANT_SEARCH_ALL_BY_PARAM_FAIL(1203, "가장 유사도가 높은 내용을 검색하는 데 실패하였습니다. "),
    RESTAURANT_SEARCH_ALL_EXTEND_SCORE_FAIL(1204, "가장 유사도가 높은 내용, 스코어 점수를 포함한 내용 검색에 실패했습니다. "),
    RESTAURANT_SEARCH_ALL_EXTEND_DISTANCE_FAIL(1205, "가장 유사도가 높은 내용, 거리를 포함한 내용 검색에 실패했습니다. "),
    RESTAURANT_SEARCH_ALL_EXTEND_SCORE_AND_DISTANCE_FAIL(1206, "가장 유사도가 높은 내용, 스코어 점수, 거리를 포함한 내용 검색에 실패했습니다. "),

    /** RestaurantServiceTitle : 13xx **/
    RESTAURANT_SEARCH_FUZZY_TITLE_FAIL(1301, "Fuzzyniss 제목 유사 검색이 실패하였습니다."),
    RESTAURANT_SEARCH_NGRAM_TITLE_FAIL(1302, "Ngram 제목 유사 검색이 실패하였습니다."),
    RESTAURANT_SEARCH_FUZZY_AND_NGRAM_TITLE_FAIL(1303, "Fuzzy 검색과 Ngram 통합 검색을 집계하는 과정이 실패했습니다."),

    /** +========= Accommodation Domain : 2xxx =========+ **/
    ACCOMMODATION_SAVE_FAIL(2000, "숙소 데이터 저장 실패"),
    ACCOMMODATION_SEARCH_FUZZY_TITLE_FAIL(2001, "Fuzzyniss 제목 유사 검색이 실패하였습니다."),
    ACCOMMODATION_SEARCH_NGRAM_TITLE_FAIL(2002, "Ngram 제목 유사 검색이 실패하였습니다."),
    ACCOMMODATION_SEARCH_FUZZY_AND_NGRAM_TITLE_FAIL(2003, "Fuzzy 검색과 Ngram 통합 검색을 집계하는 과정이 실패했습니다."),


    /** +========= CheckParameterInterceptor : 9xxx +========= **/
    INTERCEPTOR_TOO_MANY_MAX_RESULTS(9001, "너무 많은 maxResult 매개변수입니다. (100 초과)"),
    INTERCEPTOR_TOO_MANY_FUZZINESS(9002, "너무 많은 fuzziness 매개변수입니다. (10 초과)"),
    INTERCEPTOR_TOO_MANY_KILOS(9003, "너무 많은 kilo 매개변수입니다. (300 초과)"),


    ;
    private final int code;
    private final String message;
}
