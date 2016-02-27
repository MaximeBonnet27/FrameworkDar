package com.wasp.server.process.router;

import com.wasp.schemas.wasp.ControllerType;
import com.wasp.schemas.wasp.RequestMappingType;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller extends ControllerType {

    private final ControllerType delegate;
    private final List<RequestMapping> requestMappings;

    public Controller(ControllerType delegate, ApplicationJarLoader jarLoader) {
        this.delegate=delegate;
        Object controller = jarLoader.newInstance(getClazz());
        this.requestMappings=new ArrayList<>();
        this.requestMappings.addAll(delegate
                .getRequestMapping()
                .stream()
                .map(requestMappingType ->
                        new RequestMapping(requestMappingType, controller,jarLoader))
                .collect(Collectors.toList()));
    }

    public RequestMapping findRequestMapping(IHttpRequest request) {
        for(RequestMapping requestMapping:requestMappings){
            if (requestMapping.isMapping(request))
                return requestMapping;
        }
        return null;
    }

    @Override
    public List<RequestMappingType> getRequestMapping() {
        return delegate.getRequestMapping();
    }

    @Override
    public String getClazz() {
        return delegate.getClazz();
    }

    @Override
    public void setClazz(String value) {
        delegate.setClazz(value);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        return delegate.append(locator, buffer, strategy);
    }

    @Override
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        return delegate.appendFields(locator, buffer, strategy);
    }
}
