package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.schemas.JXBStringUtil;
import com.wasp.schemas.wasp.*;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import org.apache.log4j.Logger;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.wasp.util.httpComponent.common.enums.HttpContentTypes.*;
import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;
import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.CONTENT_TYPE;

public class RequestMapping extends RequestMappingType {
    private static Logger logger = Logger.getLogger(RequestMapping.class);

    private final RequestMappingType delegate;
    private final Object controller;
    private Method method;
    private ApplicationJarLoader jarLoader;
    //if resource if define as "/.../(a)/.../(b)/"
    //will contain {a -> 1 , b -> 2}
    private HashMap<String, Integer> pathIndexes;

    public RequestMapping(RequestMappingType delegate, Object controller, ApplicationJarLoader jarLoader) {
        this.delegate = delegate;
        this.controller = controller;
        this.jarLoader = jarLoader;

        try {
            if (hasArguments())
                this.method = this.controller.getClass().getDeclaredMethod(getCallback(), getArgumentsClasses());
            else
                this.method = this.controller.getClass().getDeclaredMethod(getCallback());

            this.pathIndexes = indexingPathVariable();
            generateDefaultValueFormat();

            logger.info(this);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * if content-type is null that mean
     * that we'll need to map all format types
     * same thing for produce-type
     */
    private void generateDefaultValueFormat() {
        List<String> defaultsFormat = getDefaultsFormat();
        if (getContentType() == null) {
            FormatsType formatsType = new FormatsType();
            for (String s : defaultsFormat) {
                FormatType formatType = new FormatType();
                formatType.setValue(s);
                formatsType.getFormat().add(formatType);
            }
            setContentType(formatsType);
        }

        if (getProduceType() == null) {
            FormatsType formatsType = new FormatsType();
            for (String s : defaultsFormat) {
                FormatType formatType = new FormatType();
                formatType.setValue(s);
                formatsType.getFormat().add(formatType);
            }
            setProduceType(formatsType);
        }
    }

    private List<String> getDefaultsFormat() {
        return Arrays.asList(TEXT, JSON, HTML, QUERY_STRING, XML);
    }

    /**
     * @return classes of callback's arguments
     */
    private Class<?>[] getArgumentsClasses() {
        ArrayList<Class<?>> classes = new ArrayList<>();
        List<ArgumentType> arguments = getArguments().getArgument();
        for (ArgumentType arg : arguments) {
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
        return getArguments() != null;
    }

    public Object callback(IHttpRequest request) throws InvocationTargetException, IllegalAccessException, MappingException {
        return hasArguments() ?
                this.method.invoke(controller, parseArguments(request)) :
                this.method.invoke(controller);
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
        List<ArgumentType> argumentTypes = getArguments().getArgument();

        for (ArgumentType argumentType : argumentTypes) {
            switch (argumentType.getSourceType()) {
                case "path-variable":
                    arguments.add(createPathVariable(request, argumentType));
                    break;
                case "request-variable":
                    arguments.add(createRequestVariable(request, argumentType));
                    break;
                case "request-body":
                    arguments.add(createRequestBody(request, argumentType));
                    break;
                default:
                    logger.error(argumentType.getSourceType() + " not implemented");
            }
        }

        return arguments.toArray(new Object[arguments.size()]);
    }

    /**
     * @param request      contains the request body
     * @param argumentType contains the type with which get the body
     * @return object who contains the request-body
     * @throws MappingException if has any error with type
     */
    private Object createRequestBody(IHttpRequest request, ArgumentType argumentType) throws MappingException {
        Class<?> clazz;
        try {
            clazz = Class.forName(argumentType.getType());
        } catch (ClassNotFoundException e) {
            clazz = jarLoader.loadClass(argumentType.getType());
        }
        return convertType(request.getContent(), clazz);
    }

    /**
     * @param request      contains the url where we wan't to get the value
     * @param argumentType contains the name of the value to get in the url
     * @return object who contains the value
     * @throws MappingException if value not found, bad type
     */
    private Object createRequestVariable(IHttpRequest request, ArgumentType argumentType) throws MappingException {
        String value = request.getMethod().getUrl().getArguments().get(argumentType.getSourceRef());
        if (value == null)
            throw new MappingException("argument " + argumentType.getSourceRef() + " not found");
        try {
            return convertBasicType(value, Class.forName(argumentType.getType()));
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new MappingException("Request-variable must be java.lang.Integer, java.lang.Float or java.lang.String");
        }
    }

    /**
     * @param request      contains the resource
     * @param argumentType contains the index of the group to match in resource
     * @return object who contains the value
     * @throws MappingException if group not found or bad type
     */
    private Object createPathVariable(IHttpRequest request, ArgumentType argumentType) throws MappingException {
        Integer index = pathIndexes.get(argumentType.getSourceRef());
        Matcher matcher = Pattern.compile(getResource()).matcher(request.getMethod().getUrl().getResource());
        if (matcher.matches()) {
            String token = matcher.group(index);
            try {
                return convertBasicType(token, Class.forName(argumentType.getType()));
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
        List<String> formats = formatsToStrings(getContentType().getFormat());
        for (String format : formats) {
            switch (format) {
                case TEXT:
                    try {
                        return convertBasicType(content, clazz);
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }
                    break;
                case HTML:
                    //TODO parse to html
                    if (clazz.isAssignableFrom(String.class))
                        return content;
                    break;
                case JSON:
                    try {
                        return new AppUtils().fromJSON(content, clazz);
                    } catch (IOException e) {
                        logger.warn(e.getMessage());
                        break;
                    }
                case XML:
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
        if (!Pattern.compile(getMethod()).matcher(request.getMethod().getMethodType().toString()).matches()) {
            return false;
        }
        //mapping contentType?
        if (getContentType() != null && request.getHeader().get(CONTENT_TYPE) != null) {
            List<String> formats = formatsToStrings(getContentType().getFormat());
            if (!request.getHeader().get(CONTENT_TYPE).stream().anyMatch(formats::contains)) {
                return false;
            }
        }
        //mapping accet?
        if (getProduceType() != null && request.getHeader().get(ACCEPT) != null) {
            List<String> formats = formatsToStrings(getProduceType().getFormat());
            if (!request.getHeader().get(ACCEPT).stream().anyMatch(formats::contains)) {
                return false;
            }
        }
        return true;
    }

    private List<String> formatsToStrings(List<FormatType> formatTypes) {
        return formatTypes.stream().map(FormatType::getValue).collect(Collectors.toList());
    }

    public static boolean clashingWith(RequestMapping rm1, RequestMapping rm2) {
        return rm1.equivalentTo(rm2) || rm2.equivalentTo(rm1);
    }

    private boolean equivalentTo(RequestMapping other) {
        List<String> contentFormats = formatsToStrings(getContentType().getFormat());
        List<String> produceFormats = formatsToStrings(getProduceType().getFormat());
        return Pattern.compile(getMethod()).matcher(other.getMethod()).matches() &&
                (Pattern.compile(getResource()).matcher(other.getResource()).matches() || getResource().equals(other.getResource())) && // when both are regex
                formatsToStrings(other.getContentType().getFormat()).stream().anyMatch(contentFormats::contains) &&
                formatsToStrings(other.getProduceType().getFormat()).stream().anyMatch(produceFormats::contains);


    }

    @Override
    public FormatsType getContentType() {
        return delegate.getContentType();
    }

    @Override
    public void setContentType(FormatsType value) {
        delegate.setContentType(value);
    }

    @Override
    public FormatsType getProduceType() {
        return delegate.getProduceType();
    }

    @Override
    public void setProduceType(FormatsType value) {
        delegate.setProduceType(value);
    }

    @Override
    public ArgumentsType getArguments() {
        return delegate.getArguments();
    }

    @Override
    public void setArguments(ArgumentsType value) {
        delegate.setArguments(value);
    }

    @Override
    public String getResource() {
        return delegate.getResource();
    }

    @Override
    public void setResource(String value) {
        delegate.setResource(value);
    }

    @Override
    public String getMethod() {
        return delegate.getMethod();
    }

    @Override
    public void setMethod(String value) {
        delegate.setMethod(value);
    }

    @Override
    public String getCallback() {
        return delegate.getCallback();
    }

    @Override
    public void setCallback(String value) {
        delegate.setCallback(value);
    }

    @Override
    public String toString() {
        return JXBStringUtil.pretty(delegate);
    }

    @Override
    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        return delegate.append(locator, buffer, strategy);
    }

    @Override
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        return delegate.appendFields(locator, buffer, strategy);
    }

}
