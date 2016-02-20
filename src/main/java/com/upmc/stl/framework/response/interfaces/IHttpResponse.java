package com.upmc.stl.framework.response.interfaces;


import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.HttpResponseHeader;

@SuppressWarnings("unused")
public interface IHttpResponse {

    String getProtocol();
    void setProtocol(String protocol);

    EStatus getStatus();
    void setStatus(EStatus status);

    HttpResponseHeader getHeader();
    void setHeader(HttpResponseHeader header);

    String getContent();
    void setContent(String content);

}
