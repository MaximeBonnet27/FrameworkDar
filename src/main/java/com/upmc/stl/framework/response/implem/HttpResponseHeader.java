package com.upmc.stl.framework.response.implem;


import com.upmc.stl.framework.common.implem.HttpHeader;
import com.upmc.stl.framework.response.enums.HttpResponseHeaderFields;

public class HttpResponseHeader extends HttpHeader {

    public HttpResponseHeader() {
        super();
    }

    @Override
    public boolean accept(String field) {
        return HttpResponseHeaderFields.isDefined(field);
    }
}
