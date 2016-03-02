package com.wasp.util.httpComponent.response.implem;

import java.util.*;

import static java.util.Collections.singletonList;

@SuppressWarnings("unused")
public class HttpCookie {
    private HashMap<String,List<String>> data;
    private boolean secure;
    private boolean httpOnly;

    public HttpCookie() {
        this.data=new HashMap<>();
        this.secure=false;
        this.httpOnly=false;
    }

    public HttpCookie(String name, String expires, String path, String domain) {
        this();
        addName(name);
        setExpires(expires);
        setPath(path);
        setDomain(domain);
    }

    public List<String> getNames() {
        return data.get("name");
    }

    public void addName(String name) {
        if(!data.containsKey("name"))
            data.put("name",new ArrayList<>());
        getNames().add(name);
    }

    public String getExpires() {
        List<String> expires = data.get("expires");
        return expires==null? null : expires.get(0);
    }

    public void setExpires(String expires) {
        data.put("expires", singletonList(expires));
    }

    public String getPath() {
        List<String> path = data.get("path");
        return path==null? null : path.get(0);
    }

    public void setPath(String path) {
        data.put("path",singletonList(path));
    }

    public String getDomain() {
        List<String> domain = data.get("domain");
        return domain==null? null : domain.get(0);
    }

    public void setDomain(String domain) {
        data.put("domain",singletonList(domain));
    }

    public List<String> get(String key,String value){
        return data.get(key);
    }

    public void add(String key,String value){
        if(!data.containsKey(key))
            data.put(key,new ArrayList<>());
        data.get(key).add(value);
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Set-Cookie:");
        for(Map.Entry<String,List<String>> entry:data.entrySet()){
            for(String value:entry.getValue()){
                builder.append(" ")
                        .append(entry.getKey())
                        .append("=")
                        .append(value)
                        .append(";");
            }
        }
        if(isHttpOnly())
            builder.append("; HttpOnly");
        if(isSecure())
            builder.append("; secure");
        return builder.toString();
    }
}
