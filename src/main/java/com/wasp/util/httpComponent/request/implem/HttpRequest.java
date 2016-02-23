package com.wasp.util.httpComponent.request.implem;

import com.wasp.util.httpComponent.request.interfaces.IMethod;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;

public class HttpRequest implements IHttpRequest {

    private IMethod method;
    private HttpRequestHeader header;
    private String content;

    public HttpRequest() {
        header = new HttpRequestHeader();
        method = new Method();
        content="";
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
    public HttpRequestHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(HttpRequestHeader header) {
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