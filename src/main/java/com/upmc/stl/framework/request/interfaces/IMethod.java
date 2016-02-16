package com.upmc.stl.framework.request.interfaces;

import com.upmc.stl.framework.request.enums.EMethodType;
import com.upmc.stl.framework.common.enums.EProtocol;

import java.net.URL;

public interface IMethod {

    EMethodType getMethodType();
    void setMethodType(EMethodType methodType);

    URL getURL();
    void setURL(URL url);

    EProtocol getProtocol();
    void setProtocol(EProtocol protocol);

}
