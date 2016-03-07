package com.wasp.configuration.wasp;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Wasp {
    private List<Controller> controllers;
    private ViewMapping viewMapping;
    public Wasp() {
    }

    public Wasp(List<Controller> controllers,ViewMapping viewMapping) {
        this.controllers = controllers;
    }

    public List<Controller> getControllers() {
        if(controllers==null)
            controllers=new ArrayList<>();
        return controllers;
    }

    public void setControllers(List<Controller> controllers) {
        this.controllers = controllers;
    }


    public ViewMapping getViewMapping() {
        return viewMapping;
    }

    public void setViewMapping(ViewMapping viewMapping) {
        this.viewMapping = viewMapping;
    }

    @Override
    public String toString() {
        return "Wasp{" +
                "controllers=" + controllers +
                ", viewMapping=" + viewMapping +
                '}';
    }
}
