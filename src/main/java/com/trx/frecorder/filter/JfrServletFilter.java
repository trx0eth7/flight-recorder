package com.trx.frecorder.filter; /**
 * @author vasilev
 */

import com.trx.frecorder.jfr.event.HttpSampleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@WebFilter(filterName = "JfrWebFilter")
public class JfrServletFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(JfrServletFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        var event = new HttpSampleEvent();

        if (event.isEnabled()) {
            event.begin();

            log.debug("Http profiling is enabled");
        }

        chain.doFilter(request, response);

        event.end();

        if (event.shouldCommit()) {
            // request
            if (request instanceof HttpServletRequest) {
                event.method = ((HttpServletRequest) request).getMethod();
                event.path = ((HttpServletRequest) request).getRequestURL().toString();
                event.queryParameters = ((HttpServletRequest) request).getQueryString();
            }

            event.mediaType = request.getContentType();
            event.length = request.getContentLength();

            // response
            if (response instanceof HttpServletResponse) {
                event.status = ((HttpServletResponse) response).getStatus();
                event.responseLength = ((HttpServletResponse) response).getHeader(HttpHeaders.CONTENT_LENGTH);
                event.responseHeaders = String.join(",", ((HttpServletResponse) response).getHeaderNames());
            }

            event.commit();
        }
    }
}
