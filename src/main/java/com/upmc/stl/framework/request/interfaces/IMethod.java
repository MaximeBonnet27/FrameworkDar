package com.upmc.stl.framework.request.interfaces;

import com.upmc.stl.framework.request.enums.EMethodType;
import com.upmc.stl.framework.common.enums.EProtocol;

public interface IMethod {

    EMethodType getMethodType();
    void setMethodType(EMethodType methodType);

    String getURL();
    void setURL(String urlName);

    EProtocol getProtocol();
    void setProtocol(EProtocol protocol);

}
