package com.wasp.util.httpComponent.request.implem;

import com.wasp.util.httpComponent.request.interfaces.IMethod;
import com.wasp.util.httpComponent.request.interfaces.IUrl;

public class Method implements IMethod {

    private String methodType;
    private IUrl url;
    private String protocol;

    public Method() {
    }

    @Override
    public String getMethodType() {
        return methodType;
    }

    @Override
    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    @Override
    public IUrl getUrl() {
        return url;
    }

    @Override
    public void setUrl(IUrl url) {
        this.url = url;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return methodType + " " + url + " " + protocol;
    }
}
