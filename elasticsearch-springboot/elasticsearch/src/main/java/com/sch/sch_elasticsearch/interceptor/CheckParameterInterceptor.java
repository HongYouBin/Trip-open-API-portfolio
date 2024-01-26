package com.sch.sch_elasticsearch.interceptor;

import com.sch.sch_elasticsearch.exception.CommonException;
import com.sch.sch_elasticsearch.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CheckParameterInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String maxResults = request.getParameter("maxResults");
        String fuzziness = request.getParameter("fuzziness");
        String kilo = request.getParameter("kilo");
        if (maxResults != null && Integer.parseInt(maxResults) > 100) {
            throw new CommonException(ExceptionType.INTERCEPTOR_TOO_MANY_MAX_RESULTS);
        }
        if (fuzziness != null && Integer.parseInt(fuzziness) > 10) {
            throw new CommonException(ExceptionType.INTERCEPTOR_TOO_MANY_FUZZINESS);
        }
        if (kilo != null && Integer.parseInt(kilo) > 300) {
            throw new CommonException(ExceptionType.INTERCEPTOR_TOO_MANY_FUZZINESS);
        }
        return true;
    }

    //postHandle 필요 시 추가할 것
}
