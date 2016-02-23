package com.wasp.util.httpComponent.request.interfaces;

import com.wasp.util.httpComponent.request.implem.HttpRequestHeader;

@SuppressWarnings("unused")
public interface IHttpRequest {

    IMethod getMethod();
    void setMethod(IMethod method);

    HttpRequestHeader getHeader();
    void setHeader(HttpRequestHeader header);

    String getContent();
    void setContent(String content);

}
