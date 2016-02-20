package com.upmc.stl.framework.request.implem;


import com.upmc.stl.framework.common.implem.HttpHeader;
import com.upmc.stl.framework.request.enums.HttpRequestHeaderFields;

public class HttpRequestHeader extends HttpHeader {

    public HttpRequestHeader() {
        super();
    }

    @Override
    public boolean accept(String field) {
        return HttpRequestHeaderFields.isDefined(field);
    }
}
