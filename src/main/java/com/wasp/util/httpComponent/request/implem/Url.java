package com.wasp.util.httpComponent.request.implem;

import com.wasp.util.httpComponent.request.interfaces.IUrl;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Url implements IUrl {
    private String context;
    private String resource;
    private HttpParameters arguments;

    public Url(String url) throws MalformedURLException {
        Pattern pattern = Pattern.compile("^(/[^/]*)((/[^/?]*)*)(\\?(.*))?");
        Matcher matcher = pattern.matcher(url);
        if(matcher.matches()) {
            this.context= matcher.group(1);
            this.resource=matcher.group(2);
            this.arguments = new HttpParameters(matcher.group(5));
        }else{
            throw new MalformedURLException("cannot parser "+url);
        }
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public String getResource() {
        return this.resource;
    }

    @Override
    public Integer getIntegerValue(String key) {
        return Integer.parseInt(getValue(key));
    }

    @Override
    public Float getFloatValue(String key) {
        return Float.parseFloat(getValue(key));
    }

    @Override
    public String getValue(String key) {
        return arguments.get(key);
    }

    @Override
    public Map<String, String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return context+resource+arguments;
        //StringBuilder result = new StringBuilder(context+resource+arguments);
        /*if(!arguments.isEmpty()) {
            result.append("?");
            arguments.entrySet().forEach(entry -> result
                    .append(entry.getKey())
                    .append("=").append(entry.getValue())
                    .append("&"));*/
        //}
        //return result.toString()//.replaceFirst("&$","");
    }
}
