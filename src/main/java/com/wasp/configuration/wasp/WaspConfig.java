package com.wasp.configuration.wasp;


import com.wasp.util.annotations.PathVariable;
import com.wasp.util.annotations.Request;
import com.wasp.util.annotations.RequestBody;
import com.wasp.util.annotations.RequestVariable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static java.util.Arrays.asList;

public abstract class WaspConfig extends Wasp {

    public abstract Class[] getAnnotatedControllers();

    public final void init() {
        Class[] annotatedControllers = getAnnotatedControllers();
        for (Class clazz : annotatedControllers) {
            if (clazz.isAnnotationPresent(com.wasp.util.annotations.Controller.class)) {
                this.getControllers().add(createController(clazz));
            }
        }
    }

    private Controller createController(Class clazz) {
        Controller controller = new Controller();
        controller.setClassName(clazz.getName());
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(com.wasp.util.annotations.RequestMapping.class)) {
                controller.getRequestMappings().add(createRequestMapping(method));
            }
        }
        return controller;
    }

    private RequestMapping createRequestMapping(Method method) {
        com.wasp.util.annotations.RequestMapping annotation = method.getAnnotation(com.wasp.util.annotations.RequestMapping.class);
        RequestMapping requestMapping = new RequestMapping();
        requestMapping.setResource(annotation.resource());
        requestMapping.setMethods(asList(annotation.methods()));
        requestMapping.setCallback(method.getName());
        requestMapping.setContentType(asList(annotation.contentType()));
        requestMapping.setProduceType(asList(annotation.produceType()));
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {

            Argument argument = new Argument();
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                argument.setSourceType("path-variable");
                argument.setSourceRef(pathVariable.value());
                argument.setType(parameter.getType().getName());
                requestMapping.getArguments().add(argument);
            } else if (parameter.isAnnotationPresent(Request.class)) {
                Request request = parameter.getAnnotation(Request.class);
                argument.setSourceType("request");
                argument.setType(parameter.getType().getName());
                requestMapping.getArguments().add(argument);
            } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                RequestBody requestBody = parameter.getAnnotation(RequestBody.class);
                argument.setSourceType("request-body");
                argument.setType(parameter.getType().getName());
                requestMapping.getArguments().add(argument);
            } else {
                if (parameter.isAnnotationPresent(RequestVariable.class)) {
                    RequestVariable requestVariable = parameter.getAnnotation(RequestVariable.class);
                    argument.setSourceType("request-variable");
                    argument.setSourceRef(requestVariable.value());
                    argument.setType(parameter.getType().getName());
                    requestMapping.getArguments().add(argument);
                }
            }
        }
        return requestMapping;

    }
}
