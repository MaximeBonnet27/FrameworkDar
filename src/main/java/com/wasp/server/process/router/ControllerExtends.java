package com.wasp.server.process.router;

import com.wasp.configuration.wasp.Controller;
import com.wasp.configuration.wasp.RequestMapping;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerExtends extends Controller {

    private final Controller delegate;
    private final List<RequestMappingExtends> requestMappingExtendses;

    public ControllerExtends(Controller delegate, ApplicationJarLoader jarLoader) {
        this.delegate=delegate;
        Object controller = jarLoader.newInstance(getClassName());
        this.requestMappingExtendses =new ArrayList<>();
        //creation requestMappings
        this.requestMappingExtendses.addAll(getRequestMappings()
                .stream()
                .map(requestMapping ->
                        new RequestMappingExtends(requestMapping, controller,jarLoader))
                .collect(Collectors.toList()));
    }

    public RequestMappingExtends findRequestMapping(IHttpRequest request) {
        for(RequestMappingExtends requestMappingExtends : requestMappingExtendses){
            if (requestMappingExtends.isMapping(request))
                return requestMappingExtends;
        }
        return null;
    }

    public List<RequestMappingExtends> getRequestMappingExtendses() {
        return requestMappingExtendses;
    }

    @Override
    public String getClassName() {
        return delegate.getClassName();
    }

    @Override
    public void setClassName(String className) {
        delegate.setClassName(className);
    }

    @Override
    public List<RequestMapping> getRequestMappings() {
        return delegate.getRequestMappings();
    }

    @Override
    public void setRequestMappings(List<RequestMapping> requestMappings) {
        delegate.setRequestMappings(requestMappings);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
