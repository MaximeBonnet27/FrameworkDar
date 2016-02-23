package com.wasp.server.process.router;

import com.wasp.AppUtils;
import com.wasp.schemas.wasp.RequestMappingType;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;
import static com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields.CONTENT_TYPE;

public class RequestMapping{

    public static IHttpResponse callback(JarClassLoader jcl,RequestMappingType requestMapping, IHttpRequest request){
        Object o = JclObjectFactory.getInstance().create(jcl, requestMapping.getController());
        try {
            Method[] declaredMethods = o.getClass().getDeclaredMethods();
            for(Method m:declaredMethods)
                System.out.println(m.getName());
            Method callback = o.getClass().getDeclaredMethod(requestMapping.getCallback());
            Object result = callback.invoke(o);
            if(result.getClass().isAssignableFrom(IHttpResponse.class))
                return (IHttpResponse)result;
            return convertToHttpResponse(result,request.getHeader().get(ACCEPT));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isMapping(RequestMappingType requestMapping, IHttpRequest request) {
        //TODO to complete
        return requestMapping.getResource().equals(request.getMethod().getUrl().getResource().replaceFirst("^/[^/]+", "")) &&
                requestMapping.getMethod().equals(request.getMethod().getMethodType().toString());
    }

    public static RequestMappingType findRequestMappindType(List<RequestMappingType> requestMappingTypes, IHttpRequest request) {
        for(RequestMappingType requestMappingType : requestMappingTypes){
            if(isMapping(requestMappingType,request))
                return requestMappingType;
        }
        return null;
    }

    private static IHttpResponse convertToHttpResponse(Object result, Set<String> strings) {
        HttpResponseBuilder builder = new HttpResponseBuilder();

        if(strings.contains("text/plain")){
            builder.header(CONTENT_TYPE,"text/plain");
            builder.ok(result.toString());

        }else if(strings.contains("application/json")){
            builder.header(CONTENT_TYPE, "application/json");
            builder.ok(new AppUtils().toJSON(result));

        }else if(strings.contains("application/xml")){
            builder.header(CONTENT_TYPE,"application/xml");
            builder.ok("");
            //TODO
        }else {
            builder.noContent();
        }
        return builder.build();
    }


}
