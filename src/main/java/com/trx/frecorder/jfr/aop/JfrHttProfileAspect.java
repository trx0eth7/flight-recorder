package com.trx.frecorder.jfr.aop;

import com.trx.frecorder.jfr.event.HttpSampleEvent;
import jdk.jfr.Event;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author vasilev
 */
@Aspect
@Component
public class JfrHttProfileAspect {
    private static final Logger log = LoggerFactory.getLogger(JfrHttProfileAspect.class);

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public JfrHttProfileAspect(HttpServletRequest request,
                               HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Around("@annotation(com.trx.frecorder.jfr.aop.annotation.JfrHttProfile)")
    public Object profile(ProceedingJoinPoint joinPoint) throws Throwable {
        var event = new HttpSampleEvent();

        if (isProfiled(event)) {
            event.begin();
            log.debug("Http profiling is enabled");
        }

        var proceed = joinPoint.proceed();

        event.end();

        if (event.shouldCommit()) {
            // request
            event.method = request.getMethod();
            event.javaMethod = joinPoint.getSignature().toString();
            event.mediaType = request.getContentType();
            event.path = request.getRequestURL().toString();
            event.queryParameters = request.getQueryString();
            event.length = request.getContentLength();

            // response
            event.status = response.getStatus();
            event.responseLength = response.getHeader(HttpHeaders.CONTENT_LENGTH);
            event.responseHeaders = String.join(",", response.getHeaderNames());

            event.commit();
        }

        return proceed;
    }

    private boolean isProfiled(Event event) {
        return event.isEnabled() && request != null && response != null;
    }

}
