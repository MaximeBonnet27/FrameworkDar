package com.wasp.util.httpComponent.common.enums;

import java.util.Arrays;
import java.util.List;

public class HttpContentTypes {
    public static final String TEXT_PLAIN = "text/plain";
    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_HTML = "text/html";
    public static final String APPLICATION_XML = "application/xml";
    public static final String QUERY_STRING= "application/x-www-form-urlencoded";
    public static final String TEXT_CSS = "text/css";
    public static final String IMAGE= "image";
    public static final String IMAGE_PNG= "image/png";
    public static List<String> getAllContentTypes() {
        return Arrays.asList(TEXT_PLAIN, APPLICATION_JSON, TEXT_HTML, QUERY_STRING, APPLICATION_XML, TEXT_CSS, IMAGE,IMAGE_PNG);
    }

}
