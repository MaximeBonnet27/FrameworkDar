package com.wasp.util.httpComponent.request.implem;

import com.wasp.util.httpComponent.request.interfaces.IUrl;

import java.util.HashMap;
import java.util.Map;

public class Url implements IUrl {
    private String resource;
    private HashMap<String, String> arguments;

    public Url(String url) {
        //TODO throw exception si pas conforme
        String[] split = url.split("\\?");
        this.resource = split[0];
        this.arguments= new HashMap<>();
        if(split.length>1) {
            String[] args = split[1].split("&");
            initArguments(args);
        }
    }

    private void initArguments(String[] args) {
        for (String arg : args) {
            String[] kv = arg.split("=");
            arguments.put(kv[0], kv[1]);
        }
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
    public String getValue(String key) {
        return arguments.get(key);
    }

    @Override
    public Map<String, String> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(resource);
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
