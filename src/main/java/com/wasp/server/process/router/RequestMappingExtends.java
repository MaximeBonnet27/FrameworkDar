package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.configuration.wasp.Argument;
import com.wasp.configuration.wasp.RequestMapping;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wasp.util.httpComponent.common.enums.HttpContentTypes.*;
import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;
import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.CONTENT_TYPE;


public class RequestMappingExtends extends RequestMapping {
    private static Logger logger = Logger.getLogger(RequestMappingExtends.class);

    private final RequestMapping delegate;
    private final Object controller;
    private Method method;
    private ApplicationJarLoader jarLoader;
    //if resource if define as "/.../(a)/.../(b)/"
    //will contain {a -> 1 , b -> 2}
    private HashMap<String, Integer> pathIndexes;

    public RequestMappingExtends(RequestMapping delegate, Object controller, ApplicationJarLoader jarLoader) {
        this.delegate = delegate;
        this.controller = controller;
        this.jarLoader = jarLoader;

        try {
            if (hasArguments())
                this.method = this.controller.getClass().getDeclaredMethod(getCallback(), getArgumentsClasses());
            else
                this.method = this.controller.getClass().getDeclaredMethod(getCallback());

            this.pathIndexes = indexingPathVariable();

            logger.info(this);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * @return classes of callback's arguments
     */
    private Class<?>[] getArgumentsClasses() {
        ArrayList<Class<?>> classes = new ArrayList<>();
        List<Argument> arguments = getArguments();
        for (Argument arg : arguments) {
            try {
                classes.add(Class.forName(arg.getType()));
            } catch (ClassNotFoundException e) {
                if (jarLoader != null) {
                    classes.add(jarLoader.loadClass(arg.getType()));
                } else {
                    logger.error(e.getMessage());
                }
            }
        }
        return classes.toArray(new Class<?>[classes.size()]);
    }

    public boolean hasArguments() {
        return !getArguments().isEmpty();
    }

    public Object callback(IHttpRequest request) throws InvocationTargetException, IllegalAccessException, MappingException {
        try {
            return hasArguments() ?
                    this.method.invoke(controller, parseArguments(request)) :
                    this.method.invoke(controller);
        }catch (InvocationTargetException e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * init callback's arguments with the request
     *
     * @param request contains all the information for init arguments
     * @return Array of arguments initialized
     * @throws MappingException if request is not able to correctly init arguments
     */
    private Object[] parseArguments(IHttpRequest request) throws MappingException {
        ArrayList<Object> arguments = new ArrayList<>();
        List<Argument> argumentList = getArguments();

        for (Argument argument : argumentList) {
            switch (argument.getSourceType()) {
                case "path-variable":
                    arguments.add(createPathVariable(request, argument));
                    break;
                case "request-variable":
                    arguments.add(createRequestVariable(request, argument));
                    break;
                case "request-body":
                    arguments.add(createRequestBody(request, argument));
                    break;
                case "request":
                    arguments.add(request);
                    break;
                default:
                    logger.error(argument.getSourceType() + " not implemented");
            }
        }

        return arguments.toArray(new Object[arguments.size()]);
    }

    /**
     * @param request      contains the request body
     * @param argument contains the type with which get the body
     * @return object who contains the request-body
     * @throws MappingException if has any error with type
     */
    private Object createRequestBody(IHttpRequest request, Argument argument) throws MappingException {
        Class<?> clazz;
        try {
            clazz = Class.forName(argument.getType());
        } catch (ClassNotFoundException e) {
            clazz = jarLoader.loadClass(argument.getType());
        }
        return convertType(request.getContent(), clazz);
    }

    /**
     * @param request      contains the url where we wan't to get the value
     * @param argument contains the name of the value to get in the url
     * @return object who contains the value
     * @throws MappingException if value not found, bad type
     */
    private Object createRequestVariable(IHttpRequest request, Argument argument) throws MappingException {
        String value = request.getMethod().getUrl().getArguments().get(argument.getSourceRef());
        if (value == null)
            throw new MappingException("argument " + argument.getSourceRef() + " not found");
        try {
            return convertBasicType(value, Class.forName(argument.getType()));
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new MappingException("Request-variable must be java.lang.Integer, java.lang.Float or java.lang.String");
        }
    }

    /**
     * @param request      contains the resource
     * @param argument contains the index of the group to match in resource
     * @return object who contains the value
     * @throws MappingException if group not found or bad type
     */
    private Object createPathVariable(IHttpRequest request, Argument argument) throws MappingException {
        Integer index = pathIndexes.get(argument.getSourceRef());
        Matcher matcher = Pattern.compile(getResource()).matcher(request.getMethod().getUrl().getResource());
        if (matcher.matches()) {
            String token = matcher.group(index);
            try {
                return convertBasicType(token, Class.forName(argument.getType()));
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
                throw new MappingException("path-variable must be java.lang.Integer, java.lang.Float or java.lang.String");
            }
        } else {
            throw new MappingException("path-variable not found");
        }
    }


    /**
     * @param content to convert
     * @param clazz   type to convert to
     * @return object of type clazz from the converion of content
     * @throws MappingException if the convertion is not possible
     */
    @SuppressWarnings("unchecked")
    private Object convertType(String content, Class clazz) throws MappingException {
        List<String> contentTypes = getContentType();
        for (String contentType : contentTypes) {
            switch (contentType) {
                case TEXT_PLAIN:
                    try {
                        return convertBasicType(content, clazz);
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }
                    break;
                case TEXT_HTML:
                    //TODO parse to html
                    if (clazz.isAssignableFrom(String.class))
                        return content;
                    break;
                case APPLICATION_JSON:
                    try {
                        return new AppUtils().fromJSON(content, clazz);
                    } catch (IOException e) {
                        logger.warn(e.getMessage());
                        break;
                    }
                case APPLICATION_XML:
                    if (clazz.isAssignableFrom(String.class))
                        return content;
                    else
                        try {
                            new AppUtils().loadXML(new ByteArrayInputStream(content.getBytes()), clazz);
                        } catch (JAXBException | ParserConfigurationException | SAXException e) {
                            logger.warn(e.getMessage());
                        }
                    break;
                case QUERY_STRING:
                    //TODO parse
                    if (clazz.isAssignableFrom(String.class))
                        return content;
                    break;
            }
        }
        throw new MappingException("error format");
    }

    /**
     * @param token to convert
     * @param clazz type to convert to
     * @return object of type clazz from the converion of token
     * @throws MappingException if can't be convert
     */
    private Object convertBasicType(String token, Class clazz) throws MappingException {
        try {

            switch (clazz.getName()) {
                case "java.lang.Integer":
                    return Integer.parseInt(token);
                case "java.lang.Float":
                    return Float.parseFloat(token);
                case "java.lang.String":
                    return token;
                default:
                    throw new MappingException(token + " can't be parse to " + clazz.getName());
            }
        }catch (NumberFormatException e){
            throw new MappingException(e.getMessage());
        }
    }

    /**
     * indexation des groupes declaré dans resource et transformation en regex
     * exemple:
     * resource = "/path/(src)/to/(dest)
     * HashMap = src->1 ; dest-> 2
     * newresource = "/path/([^/]+)/to/([^/]+)"
     *
     * @return indexes of groups in resource regex
     */
    private HashMap<String, Integer> indexingPathVariable() {
        HashMap<String, Integer> result = new HashMap<>();
        int counter = 0;
        String resource = getResource();
        String newResource = "";
        for (int i = 0; i < resource.length(); i++) {
            if (resource.charAt(i) == '(') {
                newResource += "([^/]+)";
                counter++;
                int endIndex = resource.indexOf(")", i + 1);
                String id = resource.substring(i + 1, endIndex);
                i = endIndex;
                result.put(id, counter);
            } else
                newResource += resource.charAt(i);
        }
        setResource(newResource);
        return result;
    }

    public boolean isMapping(IHttpRequest request) {
        //TODO to verify
        //mapping resource?
        if (!Pattern.compile(getResource()).matcher(request.getMethod().getUrl().getResource()).matches()) {
            return false;
        }

        //mapping methode?
        if(getMethods().stream().noneMatch(m -> m.equals(request.getMethod().getMethodType().toString()))){
            return false;
        }

        //mapping contentType?
        if (request.getHeader().get(CONTENT_TYPE) != null) {
            if (request.getHeader().get(CONTENT_TYPE).stream().noneMatch(getContentType()::contains)) {
                return false;
            }
        }

        //mapping accept?
        if (request.getHeader().get(ACCEPT) != null) {
            if (request.getHeader().get(ACCEPT).stream().noneMatch(getProduceType()::contains)) {
                return false;
            }
        }
        return true;
    }

    public static boolean clashingWith(RequestMappingExtends rm1, RequestMappingExtends rm2) {
        return rm1.equivalentTo(rm2) || rm2.equivalentTo(rm1);
    }

    private boolean equivalentTo(RequestMappingExtends other) {
        return //Pattern.compile(getMethods()).matcher(other.getMethods()).matches() &&
                other.getMethods().stream().anyMatch(getMethods()::contains) &&
                (Pattern.compile(getResource()).matcher(other.getResource()).matches() || getResource().equals(other.getResource())) && // when both are regex
                other.getContentType().stream().anyMatch(getContentType()::contains) &&
                other.getProduceType().stream().anyMatch(getProduceType()::contains);
    }


    @Override
    public String getResource() {
        return delegate.getResource();
    }

    @Override
    public void setResource(String resource) {
        delegate.setResource(resource);
    }

    @Override
    public List<String> getMethods() {
        return delegate.getMethods();
    }

    @Override
    public void setMethods(List<String> method) {
        delegate.setMethods(method);
    }

    @Override
    public String getCallback() {
        return delegate.getCallback();
    }

    @Override
    public void setCallback(String callback) {
        delegate.setCallback(callback);
    }

    @Override
    public List<String> getContentType() {
        return delegate.getContentType();
    }

    @Override
    public void setContentType(List<String> contentType) {
        delegate.setContentType(contentType);
    }

    @Override
    public List<String> getProduceType() {
        return delegate.getProduceType();
    }

    @Override
    public void setProduceType(List<String> produceType) {
        delegate.setProduceType(produceType);
    }

    @Override
    public List<Argument> getArguments() {
        return delegate.getArguments();
    }

    @Override
    public void setArguments(List<Argument> arguments) {
        delegate.setArguments(arguments);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
