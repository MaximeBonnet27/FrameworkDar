package com.wasp.configuration.wasp;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private String className;
    private List<RequestMapping> requestMappings;

    public Controller() {
    }

    public Controller(String className, List<RequestMapping> requestMappings) {
        this.className = className;
        this.requestMappings = requestMappings;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<RequestMapping> getRequestMappings() {
        if(requestMappings==null)
            requestMappings=new ArrayList<>();
        return requestMappings;
    }

    public void setRequestMappings(List<RequestMapping> requestMappings) {
        this.requestMappings = requestMappings;
    }

    @Override
    public String toString() {
        return "Controller{" +
                "className='" + className + '\'' +
                ", requestMappings=" + getRequestMappings() +
                '}';
    }
}
