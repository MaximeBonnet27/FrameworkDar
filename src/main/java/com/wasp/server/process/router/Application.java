package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.common.enums.HttpContentTypes;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import com.wasp.util.views.IView;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;

@SuppressWarnings("JavaDoc")
public class Application extends ApplicationJarLoader {
    private static Logger logger = Logger.getLogger(Application.class);

    private List<ControllerExtends> controllerExtendses;
    private boolean loaded;
    private HashMap<RequestMappingExtends, Set<RequestMappingExtends>> conflicts;

    public Application(String jarLocation) {
        super(jarLocation);
        this.controllerExtendses = new ArrayList<>();
        if (getApplicationConfiguration() != null) {
            controllerExtendses.addAll(getApplicationConfiguration()
                    .getControllers()
                    .stream()
                    .map(controller -> new ControllerExtends(controller, this))
                    .collect(Collectors.toList()));
            checkConflicts();
            if (hasConflict()) {
                setLoaded(false);
            } else {
                setLoaded(true);
            }
        } else {
            setLoaded(false);
        }

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
        List<RequestMappingExtends> mappingTypes = controllerExtendses.stream()
                .map(ControllerExtends::getRequestMappingExtendses)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        for (int i = 0; i < mappingTypes.size() - 1; i++) {
            RequestMappingExtends rm1 = mappingTypes.get(i);
            for (int j = i + 1; j < mappingTypes.size(); j++) {
                RequestMappingExtends rm2 = mappingTypes.get(j);
                if (clashing(rm1, rm2))
                    continue;
                if (RequestMappingExtends.clashingWith(rm1, rm2)) {
                    registeConflict(rm1, rm2);
                    logger.error("--- CONFLICT ---\n" + rm1 + "\n " + rm2);
                }
            }
        }
    }

    /**
     * registe two RequestMapping as conflict
     *
     * @param rmt1
     * @param rmt2
     */
    private void registeConflict(RequestMappingExtends rmt1, RequestMappingExtends rmt2) {
        if (!conflicts.containsKey(rmt1))
            conflicts.put(rmt1, new HashSet<>());
        if (!conflicts.containsKey(rmt2))
            conflicts.put(rmt2, new HashSet<>());

        conflicts.get(rmt1).add(rmt2);
        conflicts.get(rmt2).add(rmt1);
    }

    /**
     * @param rmt1
     * @param rmt2
     * @return true if rmt1 and rmt2 are registered as in conflict, else false
     */
    private boolean clashing(RequestMappingExtends rmt1, RequestMappingExtends rmt2) {
        Set<RequestMappingExtends> set = conflicts.get(rmt1);
        return set != null && set.contains(rmt2);
    }


    public boolean hasConflict() {
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
     *
     * @param request to answer
     * @return a IHttpResponse from the return's callback
     * @throws MappingException if no callback found
     */
    public IHttpResponse receive(IHttpRequest request) throws MappingException {
        RequestMappingExtends requestMappingExtends = findRequestMapping(request);

        if (requestMappingExtends == null) {
            String resourceContent = getResourceContent(request.getMethod().getUrl().getResource());
            if (resourceContent != null) {
                HttpResponseBuilder responseBuilder = new HttpResponseBuilder().ok(resourceContent);
                if (request.getHeader().containsKey(ACCEPT)) {
                    responseBuilder = responseBuilder.header(HttpResponseHeaderFields.CONTENT_TYPE, new ArrayList<>(request.getHeader().get(ACCEPT)).get(0));
                }
                return responseBuilder.build();
            }
            return DefaultResponseFactory.createNotFoundResource(request);
        }

        try {
            Object result = requestMappingExtends.callback(request);
            if (result == null)
                return new HttpResponseBuilder().noContent().build();
            IHttpResponse response;
            if (!(result instanceof IHttpResponse)) {
                response = new HttpResponseBuilder().setEntity(result).build();
            } else {
                response = (IHttpResponse) result;
            }
            adaptResponse(response, request);
            return response;

        } catch (InvocationTargetException | IllegalAccessException | JAXBException e) {
            logger.error(e.getMessage());
            return DefaultResponseFactory.createResponseInternalError(e, request);
        }

    }

    /**
     * @param request to mapping
     * @return the correct RequestMapping for this request
     */
    public RequestMappingExtends findRequestMapping(IHttpRequest request) {
        for (ControllerExtends controllerExtends : controllerExtendses) {
            RequestMappingExtends requestMappingExtends = controllerExtends.findRequestMapping(request);
            if (requestMappingExtends != null)
                return requestMappingExtends;
        }
        return null;
    }

    //TODO completed

    /**
     * @param request who contains the format which the obj will be transformed to
     * @return a IHttpResponse with a content-type who correspond to this obj transformed
     */
    private void adaptResponse(IHttpResponse response, IHttpRequest request) throws JAXBException, MappingException {
        Set<String> accepted = request.getHeader().get(ACCEPT);
        HttpResponseBuilder builder = new HttpResponseBuilder(response);

        Object obj = response.getContent() == null ? response.getEntity(): response.getContent();
        if (accepted == null || accepted.contains(HttpContentTypes.TEXT_PLAIN)) {
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.TEXT_PLAIN);
            builder.ok(obj.toString());

        } else if (accepted.contains(HttpContentTypes.TEXT_HTML) && obj instanceof IView) {
            IView view = (IView) obj;
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.TEXT_HTML);
            initContentView(view);
            builder.ok(view.evaluate());
        } else if (accepted.contains(HttpContentTypes.APPLICATION_JSON)) {
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.APPLICATION_JSON);
            builder.ok(new AppUtils().toJSON(obj));

        } else if (accepted.contains(HttpContentTypes.APPLICATION_XML)) {
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.APPLICATION_XML);
            builder.ok(new AppUtils().toXml(obj));
        } else {
            builder.noContent();
        }
    }

}
