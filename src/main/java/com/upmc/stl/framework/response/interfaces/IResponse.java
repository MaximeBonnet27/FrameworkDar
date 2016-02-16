package com.upmc.stl.framework.response.interfaces;


import com.upmc.stl.framework.common.enums.EProtocol;
import com.upmc.stl.framework.response.enums.EStatus;

public interface IResponse {

    EProtocol getProtocol();
    void setProtocol(EProtocol protocol);

    EStatus getStatus();
    void setStatus(EStatus status);

    IResponseHeader getHeader();
    void setHeader(IResponseHeader header);

    String getContent();
    void setContent(String content);

}
