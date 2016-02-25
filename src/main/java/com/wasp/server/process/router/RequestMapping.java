package com.wasp.server.process.router;

import com.wasp.schemas.wasp.RequestMappingType;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import org.apache.log4j.Logger;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RequestMapping extends RequestMappingType{
    private static Logger logger=Logger.getLogger(RequestMapping.class);
    private final RequestMappingType delegate;
    private final Object controller;
    private Method method;

    public RequestMapping(RequestMappingType delegate, Object controller) {
        this.delegate=delegate;
        this.controller=controller;
        try {
            // TODO: 25/02/16 A CHANGER en lisant les args dans le xml
            this.method=this.controller.getClass().getDeclaredMethod(getCallback());
            logger.info(this);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        }

    }

    public Object callback(Object[] args) throws InvocationTargetException, IllegalAccessException {

        return this.method.invoke(controller, args);
    }

    public boolean isMapping(IHttpRequest request) {
        //TODO to complete
        logger.info("Request : " + request.getMethod().getUrl().getResource().replaceFirst("^/[^/]+", ""));
        logger.info("Method : " + request.getMethod().getMethodType().toString());
        return getResource().equals(request.getMethod().getUrl().getResource().replaceFirst("^/[^/]+", "")) &&
                getMethod().equals(request.getMethod().getMethodType().toString());
    }

    @Override
    public String getResource() {
        return delegate.getResource();
    }

    @Override
    public void setResource(String value) {
        delegate.setResource(value);
    }

    @Override
    public String getMethod() {
        return delegate.getMethod();
    }

    @Override
    public void setMethod(String value) {
        delegate.setMethod(value);
    }

    @Override
    public String getCallback() {
        return delegate.getCallback();
    }

    @Override
    public void setCallback(String value) {
        delegate.setCallback(value);
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
