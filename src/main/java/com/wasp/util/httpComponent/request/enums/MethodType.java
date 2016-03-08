package com.wasp.util.httpComponent.request.enums;

import java.util.Arrays;
import java.util.List;

public final class MethodType {

    public static final String GET ="GET";
    public static final String HEAD ="HEAD";
    public static final String POST ="POST";
    public static final String PUT ="PUT";
    public static final String DELETE ="DELETE";
    public static final String TRACE ="TRACE";
    public static final String OPTIONS ="OPTIONS";
    public static final String CONNECT ="CONNECT";
    public static final String PATCH ="PATCH";

    public static List<String> getAllMethodeType() {
        return Arrays.asList(GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,CONNECT,PATCH);
    }

}

