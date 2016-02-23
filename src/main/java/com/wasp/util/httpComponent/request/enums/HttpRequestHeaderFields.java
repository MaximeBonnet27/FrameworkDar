package com.wasp.util.httpComponent.request.enums;

import java.lang.reflect.Field;

// TODO: 16/02/16 ADD DOC FROM WIKI
@SuppressWarnings("unused")
public final class HttpRequestHeaderFields {

    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_DATETIME = "Accept-Datetime";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONNECTION = "Connection";
    public static final String EXPECT = "Expect";
    public static final String FORWARDED = "Forwarded";
    public static final String FROM = "From";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";
    public static final String CONTENT_LENGTH="Content-Length";
    public static final String CONTENT_TYPE="Content-Type";
    // TODO: 19/02/16 To complete

    // TODO: 16/02/16 Refacto avec ResponseHeaderFields
    public static boolean isDefined(String field) {
        Field[] fields = HttpRequestHeaderFields.class.getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(null).equals(field))
                    return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public HttpRequestHeaderFields() {
    }
}
