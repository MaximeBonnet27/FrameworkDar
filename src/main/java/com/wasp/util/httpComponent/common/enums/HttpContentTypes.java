package com.wasp.util.httpComponent.common.enums;

import java.util.Arrays;
import java.util.List;

public class HttpContentTypes {
    public static final String TEXT= "text/plain";
    public static final String JSON= "application/json";
    public static final String HTML= "text/html";
    public static final String XML = "application/xml";
    public static final String QUERY_STRING= "application/x-www-form-urlencoded";
    public static final String CSS= "text/css";

    public static List<String> getAllContentTypes() {
        return Arrays.asList(TEXT, JSON, HTML, QUERY_STRING, XML,CSS);
    }

}
