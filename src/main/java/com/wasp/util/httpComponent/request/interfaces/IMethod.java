package com.wasp.util.httpComponent.request.interfaces;

import com.wasp.util.httpComponent.request.enums.EMethodType;

public interface IMethod {

    EMethodType getMethodType();
    void setMethodType(EMethodType methodType);

    IUrl getUrl();
    void setUrl(IUrl urlName);

    String getProtocol();
    void setProtocol(String protocol);

}
