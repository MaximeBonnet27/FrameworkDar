package com.wasp.server.process.router;

import com.wasp.schemas.wasp_conf.ApplicationType;
import com.wasp.schemas.wasp_conf.WaspConfigType;
import com.wasp.server.process.interfaces.IProcess;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.util.HashMap;


public class Router implements IProcess {
    private static Logger logger = Logger.getLogger(Router.class);

    private HashMap<String, Application> applications;

    //TODO change WaspConfigType to WaspConfig
    public Router(WaspConfigType configuration) {
        this.applications = new HashMap<>();
        for (ApplicationType app : configuration.getWasps().getApplication()) {
            String context = app.getContext();
            String location = app.getLocation();
            Application application = new Application(location);

            if (application.hasConflict())
                application.setLoaded(false);
            else {
                application.setLoaded(true);
                applications.put(context, application);
            }

            if (application.isLoaded()) {
                logger.info("Application for context: " + context + " loaded");
            } else {
                logger.warn("Application for context: " + context + " not loaded");
            }

            logger.info("context " + context + " redirect to " + app.getLocation());
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
                return DefaultResponseFactory.createResponseMappingException(e,request);
            }
        }
        logger.warn("Unknown context : " + context);
        return DefaultResponseFactory.createResponseNoApplicationFoundForContext(request);
    }


}

