package com.upmc.stl.framework.response.interfaces;


import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.ResponseHeader;

public interface IResponse {

    String getProtocol();
    void setProtocol(String protocol);

    EStatus getStatus();
    void setStatus(EStatus status);

    ResponseHeader getHeader();
    void setHeader(ResponseHeader header);

    String getContent();
    void setContent(String content);

}
