package com.wasp.util.httpComponent.request.implem;

import com.wasp.util.httpComponent.request.interfaces.IUrl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Url implements IUrl {
    private String context;
    private String resource;
    private HashMap<String, String> arguments;

    public Url(String url) {
        //TODO throw exception si pas conforme
        Pattern pattern = Pattern.compile("^(/[^/]*)((/[^/?]*)*)(\\?(.*))?");
        Matcher matcher = pattern.matcher(url);
        if(matcher.matches()) {
            this.context= matcher.group(1);
            this.resource=matcher.group(2);
            this.arguments = new HashMap<>();
            String args = matcher.group(5);
            if(args!=null){
                initArguments(args.split("&"));
            }
        }
    }

    private void initArguments(String[] args) {
        for (String arg : args) {
            String[] kv = arg.split("=");
            arguments.put(kv[0], kv[1]);
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
        StringBuilder result = new StringBuilder(context+resource);
        if(!arguments.isEmpty()) {
            result.append("?");
            arguments.entrySet().forEach(entry -> result
                    .append(entry.getKey())
                    .append("=").append(entry.getValue())
                    .append("&"));
        }
        return result.toString().replaceFirst("&$","");
    }
}
