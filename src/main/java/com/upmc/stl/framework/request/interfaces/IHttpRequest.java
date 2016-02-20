package com.upmc.stl.framework.request.interfaces;

import com.upmc.stl.framework.request.implem.HttpRequestHeader;

@SuppressWarnings("unused")
public interface IHttpRequest {

    IMethod getMethod();
    void setMethod(IMethod method);

    HttpRequestHeader getHeader();
    void setHeader(HttpRequestHeader header);

    String getContent();
    void setContent(String content);

}
