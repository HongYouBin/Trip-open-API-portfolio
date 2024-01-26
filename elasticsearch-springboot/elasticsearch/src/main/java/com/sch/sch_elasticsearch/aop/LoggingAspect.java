package com.sch.sch_elasticsearch.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    //Domain 패턴에 적용 및 내 커스텀 Annotation이 설정된 파일만 포인트컷으로 적용
    @Pointcut("execution(* com.sch.sch_elasticsearch.domain..*.*(..)) " +
            "&& @annotation(com.sch.sch_elasticsearch.aop.SaveLogging)" )
    public void pointCut() {
    }

    //포인트컷 전 데이터 수집
    @Before("pointCut()")
    public void logBeforeMethod() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            MDC.put("ip", request.getRemoteAddr()); //MDC에 추가하여 ip를 콘솔 로그에 담아낼 계획이다.
        }
    }

    //지정된 포인트컷 표현식에 맞는 메서드가 성공적으로 반환될 때 실행, xml에서 DEBUG만 저장하기 때문에 DEBUG 형태로 출력
    @AfterReturning("pointCut()")
    public void logAfterMethodReturn(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName(); //클래스
        String methodName = joinPoint.getSignature().getName(); //메서드
        logger.debug("{},{}", className, methodName); //클래스와 메서드를 로그 파일에 저장
        logger.info("[LoggingAspect] " + joinPoint.getSignature().toShortString()); //콘솔 출력
    }

}
