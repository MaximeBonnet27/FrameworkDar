package com.wasp.server;

import com.wasp.util.httpComponent.common.enums.HttpContentTypes;
import com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;

import java.util.ArrayList;

// TODO: 24/02/16 Ajouter la vérification de types
public abstract class HttpArgumentsParser {


    public static Object[] parseBody(IHttpRequest request) {
        String contentType = request.getHeader()
                .get(HttpRequestHeaderFields.CONTENT_TYPE)
                .iterator().next();

        switch (contentType) {
            case HttpContentTypes.QUERY_STRING:
                return parseQueryString(request.getContent());
            default:
                return null;
        }


    }

    public static Object[] parseUrl(IHttpRequest request) {
        return null;
    }

    // TODO: 24/02/16 Gere que des entiers ici ...
    private static Object[] parseQueryString(String queryString) {
        ArrayList<Object> argsList = new ArrayList<>();
        String[] args = queryString.split("&");
        for(String arg : args){
            argsList.add(Integer.valueOf(arg.split("=")[1]));
        }
        return argsList.toArray();
    }

}
