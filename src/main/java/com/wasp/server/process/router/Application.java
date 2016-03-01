package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.schemas.JXBStringUtil;
import com.wasp.schemas.wasp.RequestMappingType;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.common.enums.HttpContentTypes;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;

public class Application extends ApplicationJarLoader {
    private static Logger logger = Logger.getLogger(Application.class);

    private List<Controller> controllers;
    private boolean loaded;
    private HashMap<RequestMappingType, Set<RequestMappingType>> conflicts;

    public Application(String jarLocation) {
        super(jarLocation);
        this.controllers = new ArrayList<>();
        controllers.addAll(getApplicationConfiguration()
                .getController()
                .stream()
                .map(controller -> new Controller(controller, this))
                .collect(Collectors.toList()));

        loaded = checkConflicts();
    }


    private boolean checkConflicts() {
        conflicts = new HashMap<>();
        boolean conflictsFound = false;
        List<RequestMapping> mappingTypes = controllers.stream()
                .map(Controller::getRequestMappings)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        for (int i = 0; i < mappingTypes.size() - 1; i++) {
            RequestMapping rm1 = mappingTypes.get(i);
            for (int j = i + 1; j < mappingTypes.size(); j++) {
                RequestMapping rm2 = mappingTypes.get(j);
                if (clashing(rm1, rm2))
                    continue;
                if (RequestMapping.clashingWith(rm1, rm2)) {
                    registeConflict(rm1, rm2);
                    conflictsFound = true;
                    logger.error("--- CONFLICT ---\n" + rm1 + "\n " + rm2);
                }
            }
        }
        return conflictsFound;
    }

    private void registeConflict(RequestMappingType rmt1, RequestMappingType rmt2) {
        if (!conflicts.containsKey(rmt1))
            conflicts.put(rmt1, new HashSet<>());
        if (!conflicts.containsKey(rmt2))
            conflicts.put(rmt2, new HashSet<>());

        conflicts.get(rmt1).add(rmt2);
        conflicts.get(rmt2).add(rmt1);
    }

    private boolean clashing(RequestMappingType rmt1, RequestMappingType rmt2) {
        Set<RequestMappingType> set = conflicts.get(rmt1);
        return set != null && set.contains(rmt2);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public IHttpResponse receive(IHttpRequest request) throws MappingException {
        RequestMapping requestMapping = findRequestMapping(request);
        if (requestMapping == null) {
            throw new MappingException("no mapping for resource " + request.getMethod().getUrl().getResource());
        }


        try {
          /*  Object[] args = null;
            // Si methode POST, il faut aller chercher les arguments dans le body
            if(request.getMethod().getMethodType().equals(EMethodType.POST)){
                args = HttpArgumentsParser.parseBody(request);
            }
            // Sinon, arguments dans la suite de l'url
            else{
                // Si il y en a
                if(request.getMethod().getUrl().getArguments().size() > 0){
                    args = HttpArgumentsParser.parseUrl(request);
                }
            }

*/
            Object result = requestMapping.callback(request);
            if (result == null)
                return new HttpResponseBuilder().noContent().build();
            if (result.getClass().isAssignableFrom(IHttpResponse.class))
                return (IHttpResponse) result;
            return convertToHttpResponse(result, request);
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        //TODO error intern
        return null;

    }

    public RequestMapping findRequestMapping(IHttpRequest request) {
        for (Controller controller : controllers) {
            RequestMapping requestMapping = controller.findRequestMapping(request);
            if (requestMapping != null)
                return requestMapping;
        }
        return null;
    }

    //TODO completed
    private IHttpResponse convertToHttpResponse(Object obj, IHttpRequest request) {
        Set<String> accepted = request.getHeader().get(ACCEPT);
        HttpResponseBuilder builder = new HttpResponseBuilder();

        if (accepted.contains(HttpContentTypes.TEXT)) {
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.TEXT);
            builder.ok(obj.toString());

        } else if (accepted.contains(HttpContentTypes.JSON)) {
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.JSON);
            builder.ok(new AppUtils().toJSON(obj));

        } else if (accepted.contains(HttpContentTypes.XML)) {
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.XML);
            builder.ok("");
            //TODO
        } else {
            builder.noContent();
        }
        return builder.build();
    }

}
