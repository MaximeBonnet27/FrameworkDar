package com.wasp.util.httpComponent.response.implem;


import com.wasp.util.httpComponent.common.implem.HttpHeader;

import java.util.ArrayList;
import java.util.List;

public class HttpResponseHeader extends HttpHeader {
    private List<HttpCookie> cookies;
    public HttpResponseHeader() {
        super();
        this.cookies=new ArrayList<>();
    }

    public List<HttpCookie> getCookies() {
        return cookies;
    }

    public void addCookie(HttpCookie cookie){
        cookies.add(cookie);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(HttpCookie cookie: cookies)
            builder.append(cookie).append("\n");
        builder.append(super.toString());
        return builder.toString();
    }
}
