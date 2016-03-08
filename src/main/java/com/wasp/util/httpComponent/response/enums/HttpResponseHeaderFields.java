package com.wasp.util.httpComponent.response.enums;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public final class HttpResponseHeaderFields {

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCEPT_PATCH = "Accept-Patch";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String AGE = "Age";
    public static final String ALLOW = "Allow";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DATE = "Date";
    public static final String ETAG = "ETag";
    public static final String EXPIRES = "Expires";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LINK = "Link";
    public static final String LOCATION = "Location";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SERVER = "Server";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String STATUS = "Status";
    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
    public static final String TRAILER = "Trailer";
    public static final String TRANSFER_ENCODING = "Tranger-Encoding";
    public static final String TSV = "TSV";
    public static final String UPGRADE = "Upgrade";
    public static final String VARY = "Vary";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    public static boolean isDefined(String field) {
        Field[] fields = HttpResponseHeaderFields.class.getDeclaredFields();
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

    private HttpResponseHeaderFields() {
    }
}
