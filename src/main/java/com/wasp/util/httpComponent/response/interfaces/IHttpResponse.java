package com.wasp.util.httpComponent.response.interfaces;


import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.implem.HttpResponseHeader;

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
