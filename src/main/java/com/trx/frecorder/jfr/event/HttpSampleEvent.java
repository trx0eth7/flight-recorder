package com.trx.frecorder.jfr.event;

import jdk.jfr.Category;
import jdk.jfr.DataAmount;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author vasilev
 */
@Component
@Scope()
@Category("Http")
@Label("Http Sample Event")
@Name(HttpSampleEvent.NAME)
@StackTrace(false)
public class HttpSampleEvent extends Event {
    static final String NAME = "com.trx.frecorder.jfr.HttpSample";

    @Label("Resource Method")
    public String method;

    @Label("Media Type")
    public String mediaType;

    @Label("Path")
    public String path;

    @Label("Query Parameters")
    public String queryParameters;

    @Label("Length")
    @DataAmount
    public int length;

    @Label("Response Headers")
    public String responseHeaders;

    @Label("Response Length")
    public String responseLength;

    @Label("Response Status")
    public int status;
}
