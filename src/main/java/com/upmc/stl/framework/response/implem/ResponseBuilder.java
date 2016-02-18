package com.upmc.stl.framework.response.implem;

import com.upmc.stl.framework.response.enums.EResponseHeaderItem;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.exceptions.StatusException;
import com.upmc.stl.framework.response.interfaces.IResponse;

import java.util.List;

import static com.upmc.stl.framework.response.enums.EStatus.*;

@SuppressWarnings("unused")
public class ResponseBuilder {

    private IResponse response;

    public ResponseBuilder(){
        response = new Response();
    }

    public ResponseBuilder protocol(String protocol){
        response.setProtocol(protocol);
        return this;
    }

    public ResponseBuilder status(EStatus status){
        response.setStatus(status);
        return this;
    }

    public ResponseBuilder status(int code) throws StatusException {
        response.setStatus(getStatus(code));
        return this;
    }

    public ResponseBuilder noContent(){
        return status(NO_CONTENT).content("");
    }

    public ResponseBuilder ok(String content){
        return status(OK).content(content);
    }

    public ResponseBuilder header(EResponseHeaderItem item, String value){
        response.getHeader().addItem(item, value);
        return this;
    }

    public ResponseBuilder header(EResponseHeaderItem item, List<String> values){
        response.getHeader().addItems(item, values);
        return this;
    }

    public ResponseBuilder content(String content){
        response.setContent(content);
        return this;
    }

    public IResponse build(){
        return response;
    }
}
