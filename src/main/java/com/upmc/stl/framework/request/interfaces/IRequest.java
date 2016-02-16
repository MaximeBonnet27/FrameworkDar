package com.upmc.stl.framework.request.interfaces;

public interface IRequest {

    IMethod getMethod();
    void setMethod(IMethod method);

    IRequestHeader getHeader();
    void setHeader(IRequestHeader header);

    String getContent();
    void setContent(String content);
}
