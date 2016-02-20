package com.upmc.stl.framework.process;

import com.upmc.stl.framework.process.interfaces.IProcess;
import com.upmc.stl.framework.request.interfaces.IHttpRequest;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.HttpResponseBuilder;
import com.upmc.stl.framework.response.interfaces.IHttpResponse;

import java.util.List;
import java.util.stream.Collectors;

public class Routeur implements IProcess{

    private List<Controller> controllers;

    public Routeur(List<Class> controllers) {
        this.controllers=controllers.stream().map(Controller::new).collect(Collectors.toList());
        //TODO vérifié collision mapping
    }

    @Override
    public IHttpResponse run(IHttpRequest request) {
        IHttpResponse response;
        for(Controller controller: controllers){
            response=controller.invoke(request);
            if(response!=null)
                return response;
        }
        //TODO default 404 error content
        return new HttpResponseBuilder().status(EStatus.NOT_FOUND).build();
    }
}
