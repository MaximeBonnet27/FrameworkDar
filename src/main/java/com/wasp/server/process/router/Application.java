package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.schemas.wasp.WaspType;
import com.wasp.server.HttpArgumentsParser;
import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.common.enums.HttpContentTypes;
import com.wasp.util.httpComponent.request.enums.EMethodType;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;

public class Application{
    private static Logger logger=Logger.getLogger(Application.class);

    private List<Controller> controllers;
    private JarClassLoader jcl;

    public Application(String jarLocation) {
        logger.info("loading jar "+jarLocation);
        this.jcl = new JarClassLoader();
        this.controllers=new ArrayList<>();
        try {
            jcl.add(new FileInputStream(jarLocation));
            URL url = new URL("jar:file:" + jarLocation + "!/wasp.xml");
            InputStream stream = url.openStream();
            WaspType waspType = new AppUtils().loadXML(stream, WaspType.class);
            controllers.addAll(waspType.getController().stream().map(controller -> new Controller(controller, jcl)).collect(Collectors.toList()));
            logger.debug("Controllers size = " + controllers.size());
        } catch ( ParserConfigurationException | JAXBException | SAXException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    public IHttpResponse receive(IHttpRequest request) throws MappingException {
        RequestMapping requestMapping = findRequestMapping(request);
        if(requestMapping==null) {
            throw new MappingException("no mapping for resource " + request.getMethod().getUrl().getResource());
        }

        try {
            Object[] args = null;
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


            Object result = requestMapping.callback(args);
            if(result.getClass().isAssignableFrom(IHttpResponse.class))
                return (IHttpResponse)result;
            return convertToHttpResponse(result,request);
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        //TODO error intern
        return null;

    }

    public  RequestMapping findRequestMapping(IHttpRequest request) {
        for(Controller controller : controllers){
            RequestMapping requestMapping = controller.findRequestMapping(request);
            if(requestMapping!=null)
                return requestMapping;
        }
        return null;
    }

    private IHttpResponse convertToHttpResponse(Object obj, IHttpRequest request) {
        Set<String> accepted = request.getHeader().get(ACCEPT);
        HttpResponseBuilder builder = new HttpResponseBuilder();

        if(accepted.contains(HttpContentTypes.TEXT)){
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.TEXT);
            builder.ok(obj.toString());

        }else if(accepted.contains(HttpContentTypes.JSON)){
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE, HttpContentTypes.JSON);
            builder.ok(new AppUtils().toJSON(obj));

        }else if(accepted.contains(HttpContentTypes.XML)){
            builder.header(HttpResponseHeaderFields.CONTENT_TYPE,HttpContentTypes.XML);
            builder.ok("");
            //TODO
        }else {
            builder.noContent();
        }
        return builder.build();
    }

}
