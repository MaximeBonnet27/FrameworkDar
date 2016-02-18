package com.upmc.stl.framework.request.interfaces;

import com.upmc.stl.framework.request.enums.EMethodType;

public interface IMethod {

    EMethodType getMethodType();
    void setMethodType(EMethodType methodType);

    String getURL();
    void setURL(String urlName);

    String getProtocol();
    void setProtocol(String protocol);

}
