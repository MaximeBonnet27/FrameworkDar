package com.wasp.server.process.router;

import com.wasp.schemas.wasp_conf.ApplicationType;
import com.wasp.schemas.wasp_conf.WaspConfigType;
import com.wasp.server.process.interfaces.IProcess;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.util.HashMap;


public class Router implements IProcess {
    private static Logger logger= Logger.getLogger(Router.class);

    private HashMap<String, Application> applications;

    public Router(WaspConfigType configuration) {
        this.applications = new HashMap<>();
        for(ApplicationType app:configuration.getWasps().getApplication()) {
            String context = app.getContext();
            String location = app.getLocation();
            applications.put(context, new Application(location));
            logger.info("context "+ context +" redirect to "+app.getLocation());
        }
    }

    @Override
    public IHttpResponse run(IHttpRequest request) {
        String[] split = request.getMethod().getUrl().getResource().split("/");
        String context = split.length > 1 ? "/" + split[1] : "/";

        Application app= applications.get(context);
        if (app != null) {
            try {
                return app.receive(request);
            } catch (MappingException e) {
                logger.warn(e.getMessage());
                //TODO resource non accessible
                return new HttpResponseBuilder().status(EStatus.NOT_FOUND).build();
            }
        }
        logger.warn("Unknown context : "+context);
        //TODO default 404 error content
        return new HttpResponseBuilder().status(EStatus.NOT_FOUND).build();
    }


}

