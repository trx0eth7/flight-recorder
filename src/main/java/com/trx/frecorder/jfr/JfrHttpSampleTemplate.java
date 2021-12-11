package com.trx.frecorder.jfr;

import com.trx.frecorder.jfr.event.HttpSampleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiFunction;

/**
 * @author vasilev
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JfrHttpSampleTemplate {
    private static final Logger log = LoggerFactory.getLogger(JfrHttpSampleTemplate.class);

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    private HttpSampleEvent event;

    public JfrHttpSampleTemplate(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @PostConstruct
    protected void init() {
        event = new HttpSampleEvent();
    }

    public <T> T profile(BiFunction<ServletRequest, ServletResponse, T> operation) {
        if (event.isEnabled()) {
            event.begin();

            log.debug("Http profiling is enabled");
        }

        var result = operation.apply(request, response);

        event.end();

        if (event.shouldCommit()) {
            // request
            event.method = request.getMethod();
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

        return result;
    }
}
