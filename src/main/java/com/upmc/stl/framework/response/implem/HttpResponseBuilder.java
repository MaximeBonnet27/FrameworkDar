package com.upmc.stl.framework.response.implem;

import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.exceptions.StatusException;
import com.upmc.stl.framework.response.interfaces.IHttpResponse;

import java.util.List;

import static com.upmc.stl.framework.response.enums.EStatus.*;

@SuppressWarnings("unused")
public class HttpResponseBuilder {

    private IHttpResponse response;

    public HttpResponseBuilder(){
        response = new HttpResponse();
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
        response.setStatus(getStatus(code));
        return this;
    }

    public HttpResponseBuilder noContent(){
        return status(NO_CONTENT).content("");
    }

    public HttpResponseBuilder ok(String content){
        return status(OK).content(content);
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

    public IHttpResponse build(){
        return response;
    }
}
