package com.upmc.stl.framework.response.implem;

import com.upmc.stl.framework.response.enums.EResponseHeaderItem;
import com.upmc.stl.framework.response.interfaces.IResponseHeader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ResponseHeader implements IResponseHeader {

    private HashMap<EResponseHeaderItem, Set<String>> map;

    public ResponseHeader(){
        this.map = new HashMap<>();
    }

    @Override
    public void addItem(EResponseHeaderItem item, String value) {
        if (!map.containsKey(item)) {
            map.put(item, new HashSet<>());
        }
        map.get(item).add(value);
    }

    @Override
    public void addItems(EResponseHeaderItem item, List<String> values) {
        values.stream().forEach(v -> this.addItem(item, v));
    }

    @Override
    public Set<String> getValues(EResponseHeaderItem item) {
        return map.get(item);
    }

    // TODO: 16/02/16 Mettre au propre ?

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        map.entrySet().stream().
                forEach(i -> sb
                        .append(i.getKey())
                        .append(": ")
                        .append(i.getValue().stream().findFirst().get())
                        .append("\r\n"));
        return sb.toString();
    }
}
