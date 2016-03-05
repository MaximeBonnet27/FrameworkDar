package com.wasp.configuration.wasp;

import java.util.ArrayList;
import java.util.List;

public class Wasp {
    private List<Controller> controllers;

    public Wasp() {
    }

    public Wasp(List<Controller> controllers) {
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

    @Override
    public String toString() {
        return "Wasp{" +
                "controllers=" + controllers +
                '}';
    }
}
