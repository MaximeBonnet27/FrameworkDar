package com.upmc.stl.framework.request.implem;

import com.upmc.stl.framework.request.interfaces.IMethod;
import com.upmc.stl.framework.request.interfaces.IRequest;
import com.upmc.stl.framework.request.interfaces.IRequestHeader;

public class Request implements IRequest {

    private IMethod method;
    private IRequestHeader header;
    private String content;

    public Request() {
        header = new RequestHeader();
        method = new Method();
    }

    @Override
    public IMethod getMethod() {
        return method;
    }

    @Override
    public void setMethod(IMethod method) {
        this.method = method;
    }

    @Override
    public IRequestHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(IRequestHeader header) {
        this.header = header;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return method + "\r\n"
                + header
                + "\r\n"
                + content +"\r\n";
    }
}