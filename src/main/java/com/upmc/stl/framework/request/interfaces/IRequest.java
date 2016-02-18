package com.upmc.stl.framework.request.interfaces;

import com.upmc.stl.framework.request.implem.RequestHeader;

public interface IRequest {

    IMethod getMethod();
    void setMethod(IMethod method);

    RequestHeader getHeader();
    void setHeader(RequestHeader header);

    String getContent();
    void setContent(String content);

}
