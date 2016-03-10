package com.wasp.util.httpComponent.response.implem;

import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.exceptions.StatusException;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;

import java.util.List;

@SuppressWarnings("unused")
public class HttpResponseBuilder {

    private IHttpResponse response;

    public HttpResponseBuilder(){
        response = new HttpResponse();
    }

    public HttpResponseBuilder(IHttpResponse response) {
        this.response = response;
    }

    public HttpResponseBuilder protocol(String protocol){
        response.setProtocol(protocol);
        return this;
    }

    public HttpResponseBuilder status(EStatus status){
        response.setStatus(status);
        return this;
    }

    public HttpResponseBuilder status(int code) throws StatusException {
        response.setStatus(EStatus.getStatus(code));
        return this;
    }

    public HttpResponseBuilder noContent(){
        return status(EStatus.NO_CONTENT).content("");
    }

    public HttpResponseBuilder ok(String content){
        return status(EStatus.OK).content(content);
    }

    public HttpResponseBuilder header(String field, String value) {
        response.getHeader().addItem(field, value);
        return this;
    }

    public HttpResponseBuilder header(String field, List<String> values){
        response.getHeader().addItems(field, values);
        return this;
    }

    public HttpResponseBuilder content(String content){
        response.setContent(content);
        return this;
    }

    public HttpResponseBuilder setCookie(HttpCookie cookie){
        response.setCookie(cookie);
        return this;
    }

    public HttpResponseBuilder setEntity(Object o){
        response.setEntity(o);
        return this;
    }
    public IHttpResponse build(){
        return response;
    }
}
