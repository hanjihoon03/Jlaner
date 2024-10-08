package com.jlaner.project.aop;

import com.jlaner.project.logTrace.ThreadLocalLogTrace;
import com.jlaner.project.logTrace.TraceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


/**
 * 컨트롤러나 서비스 등 기능들이 동작할 때 트랜젝션 단위에 따라 로그 남기는 Aspect
 * 포인트 컷은 프로젝트 패키지 하위 모두 적용
 * 어드바이스는 로그 출력
 */

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LogTraceAspect {

    private final ThreadLocalLogTrace logTrace;

    @Around("com.jlaner.project.aop.Pointcuts.allPointAndMvc()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {


        TraceStatus status = null;

        try {
            String message = joinPoint.getSignature().toString();
            status = logTrace.begin(message);

            //로직 호출 부분
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
