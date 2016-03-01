package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.schemas.wasp.RequestMappingType;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.common.enums.HttpContentTypes;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;

@SuppressWarnings("JavaDoc")
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

        checkConflicts();
    }

    /**
     * verifie que les requetes ne sont pas en collistion en fonction:
     * - des resources
     * - des methodes
     * - des content-types
     * - des produce-types
     */
    private void checkConflicts() {
        conflicts = new HashMap<>();
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
                    logger.error("--- CONFLICT ---\n" + rm1 + "\n " + rm2);
                }
            }
        }
    }

    /**
     * registe two RequestMapping as conflict
     * @param rmt1
     * @param rmt2
     */
    private void registeConflict(RequestMappingType rmt1, RequestMappingType rmt2) {
        if (!conflicts.containsKey(rmt1))
            conflicts.put(rmt1, new HashSet<>());
        if (!conflicts.containsKey(rmt2))
            conflicts.put(rmt2, new HashSet<>());

        conflicts.get(rmt1).add(rmt2);
        conflicts.get(rmt2).add(rmt1);
    }

    /**
     *
     * @param rmt1
     * @param rmt2
     * @return true if rmt1 and rmt2 are registered as in conflict, else false
     */
    private boolean clashing(RequestMappingType rmt1, RequestMappingType rmt2) {
        Set<RequestMappingType> set = conflicts.get(rmt1);
        return set != null && set.contains(rmt2);
    }


    public boolean hasConflict(){
        return conflicts.entrySet().stream().anyMatch(e -> !e.getValue().isEmpty());
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }


    /**
     * find the correct callback to the request and return a IHttpResponse
     * with correct content-type
     * @param request to answer
     * @return a IHttpResponse from the return's callback
     * @throws MappingException if no callback found
     */
    public IHttpResponse receive(IHttpRequest request) throws MappingException {
        RequestMapping requestMapping = findRequestMapping(request);

        if (requestMapping == null) {
            throw new MappingException("no mapping for resource " + request.getMethod().getUrl().getResource());
        }

        try {
            Object result = requestMapping.callback(request);
            if (result == null)
                return new HttpResponseBuilder().noContent().build();
            if (result.getClass().isAssignableFrom(IHttpResponse.class))
                return (IHttpResponse) result;
            return convertToHttpResponse(result, request);
        } catch (InvocationTargetException | IllegalAccessException | JAXBException e) {
            logger.error(e.getMessage());
            //TODO return internal error response with e.getMessage()
            return null;
        }

    }

    /**
     *
     * @param request to mapping
     * @return the correct RequestMapping for this request
     */
    public RequestMapping findRequestMapping(IHttpRequest request) {
        for (Controller controller : controllers) {
            RequestMapping requestMapping = controller.findRequestMapping(request);
            if (requestMapping != null)
                return requestMapping;
        }
        return null;
    }

    //TODO completed

    /**
     *
     * @param obj to tranform in the correct format
     * @param request who contains the format which the obj will be transformed to
     * @return a IHttpResponse with a content-type who correspond to this obj transformed
     */
    private IHttpResponse convertToHttpResponse(Object obj, IHttpRequest request) throws JAXBException {
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
            builder.ok(new AppUtils().toXml(obj));
        } else {
            builder.noContent();
        }
        return builder.build();
    }

}
