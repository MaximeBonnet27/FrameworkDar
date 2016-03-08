package com.wasp.server.process.router;

import com.wasp.configuration.wasp_conf.WaspServer;
import com.wasp.server.process.interfaces.IProcess;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.exception.JclException;

import java.util.HashMap;

public class Router implements IProcess {
    private static Logger logger = Logger.getLogger(Router.class);

    private HashMap<String, Application> applications;

    public Router(WaspServer configuration) {
        this.applications = new HashMap<>();
        for (com.wasp.configuration.wasp_conf.Application app : configuration.getApplications()) {
            String context = app.getContext();
            String location = app.getLocation();
            try {
                Application application = new Application(location);
                if(application.isLoaded()){
                    applications.put(context, application);
                    logger.info("Application for context: " + context + " loaded");
                    logger.info("context " + context + " redirect to " + app.getLocation());
                } else {
                    logger.warn("Application for context: " + context + " not loaded");
                }
            }catch (JclException e){
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public IHttpResponse run(IHttpRequest request) {
        String context = request.getMethod().getUrl().getContext();
        Application app = applications.get(context);
        if (app != null) {
            try {
                return app.receive(request);
            } catch (MappingException e) {
                logger.warn(e.getMessage());
                return DefaultResponseFactory.createResponseBadRequestException(e, request);
            }
        }
        logger.warn("Unknown context : " + context);
        return DefaultResponseFactory.createResponseNoApplicationFoundForContext(request);
    }


}

