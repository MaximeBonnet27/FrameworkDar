package com.upmc.stl.framework.request.implem;

import com.upmc.stl.framework.request.enums.ERequestHeaderItem;
import com.upmc.stl.framework.request.interfaces.IRequestHeader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestHeader implements IRequestHeader {

    private HashMap<ERequestHeaderItem, Set<String>> map;

    public RequestHeader() {
        this.map = new HashMap<>();
    }

    @Override
    public void addItem(ERequestHeaderItem item, String value) {
        if (!map.containsKey(item)) {
            map.put(item, new HashSet<>());
        }
        map.get(item).add(value);
    }

    @Override
    public void addItems(ERequestHeaderItem item, List<String> values) {
        values.stream().forEach(v -> this.addItem(item, v));
    }

    @Override
    public Set<String> getValues(ERequestHeaderItem item) {
        return map.get(item);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        map.entrySet().stream().
                forEach(i ->
                        sb.append(i.getKey())
                                .append(": ")
                                .append(i.getValue().stream().findFirst().get())
                                .append("\r\n"));
        return sb.toString();
    }
}
