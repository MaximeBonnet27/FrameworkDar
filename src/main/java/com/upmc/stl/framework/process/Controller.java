package com.upmc.stl.framework.process;

import com.upmc.stl.framework.request.interfaces.IHttpRequest;
import com.upmc.stl.framework.response.interfaces.IHttpResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    //class annoté de @Controller
    private final Class clazz;
    //methodes annoté de @RequestMapping
    //TODO creer class RequestMapping pour facilité
    private final List<Method> methods;

    public Controller(Class clazz) {
        this.clazz = clazz;
        this.methods=new ArrayList<>();
        //TODO initialiser methods
    }

    public IHttpResponse invoke(IHttpRequest request) {
        //TODO invoke la bonne methode
        return null;
    }
}
