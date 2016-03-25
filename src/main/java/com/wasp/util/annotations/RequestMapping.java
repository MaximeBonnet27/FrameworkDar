package com.wasp.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.wasp.util.httpComponent.common.enums.HttpContentTypes.*;
import static com.wasp.util.httpComponent.request.enums.MethodType.*;

@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    /**
     *
     * @return regex to match a url
     */
    String resource() default ".*";

    String[] methods() default {GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, CONNECT, PATCH};

    String[] produceType() default {TEXT_PLAIN, APPLICATION_JSON, TEXT_HTML, QUERY_STRING, APPLICATION_XML, TEXT_CSS};

    String[] contentType() default {TEXT_PLAIN, APPLICATION_JSON, TEXT_HTML, QUERY_STRING, APPLICATION_XML, TEXT_CSS};

}
