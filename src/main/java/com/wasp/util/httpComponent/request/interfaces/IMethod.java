package com.wasp.util.httpComponent.request.interfaces;

public interface IMethod {

    String getMethodType();
    void setMethodType(String methodType);

    IUrl getUrl();
    void setUrl(IUrl urlName);

    String getProtocol();
    void setProtocol(String protocol);

}
