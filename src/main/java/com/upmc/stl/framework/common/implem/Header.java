package com.upmc.stl.framework.common.implem;

import com.upmc.stl.framework.common.interfaces.IHeader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Header<T> extends HashMap<T, Set<String>> implements IHeader<T> {

    public Header() {
    }

    @Override
    public void addItem(T item, String value) {
        if (!containsKey(item)) {
            put(item, new HashSet<>());
        }
        get(item).add(value);
    }

    @Override
    public void addItems(T item, List<String> values) {
        values.stream().forEach(v -> this.addItem(item, v));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        entrySet().stream()
                .forEach(entry -> sb
                        .append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue().toString().replaceAll("^\\[|]$",""))
                        .append("\r\n"));
        return sb.toString();
    }
}
