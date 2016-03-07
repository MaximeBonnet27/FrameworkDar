package com.wasp.configuration.wasp;

import com.wasp.util.httpComponent.common.enums.HttpContentTypes;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RequestMapping {

    private String resource;
    private String method;
    private String callback;
    private List<String> contentType;
    private List<String> produceType;
    private List<Argument> arguments;

    public RequestMapping() {
    }

    public RequestMapping(String resource, String method, String callback, List<String> contentType, List<String> produceType, List<Argument> arguments) {
        this.resource = resource;
        this.method = method;
        this.callback = callback;
        this.contentType = contentType;
        this.produceType = produceType;
        this.arguments = arguments;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public List<String> getContentType() {
        if(contentType==null)
            contentType= HttpContentTypes.getAllContentTypes();
        return contentType;
    }

    public void setContentType(List<String> contentType) {
        this.contentType = contentType;
    }

    public List<String> getProduceType() {
        if(produceType==null)
            produceType=HttpContentTypes.getAllContentTypes();
        return produceType;
    }

    public void setProduceType(List<String> produceType) {
        this.produceType = produceType;
    }

    public List<Argument> getArguments() {
        if(arguments==null)
            arguments=new ArrayList<>();
        return arguments;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "RequestMapping{" +
                "resource='" + resource + '\'' +
                ", method='" + method + '\'' +
                ", callback='" + callback + '\'' +
                ", contentType=" + getContentType() +
                ", produceType=" + getProduceType() +
                ", arguments=" + arguments +
                '}';
    }
}
