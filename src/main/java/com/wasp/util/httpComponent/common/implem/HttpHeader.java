package com.wasp.util.httpComponent.common.implem;

import com.wasp.util.httpComponent.common.interfaces.IHttpHeader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class HttpHeader extends HashMap<String, Set<String>> implements IHttpHeader {

    public HttpHeader() {
    }


    @Override
    public void addItem(String field, String value) {
        if (!containsKey(field)) {
            put(field, new HashSet<>());
        }
        get(field).add(value);
    }

    @Override
    public void addItems(String field, List<String> values) {
        for (String value : values)
            addItem(field, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Entry<String,Set<String>> entry:entrySet()){
            sb.append(entry.getKey()).append(": ");
            for(String value:entry.getValue()) {
                sb.append(value).append("; ");
            }
            sb.append("\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }
}
