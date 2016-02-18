package com.upmc.stl.framework.request.enums;

import java.util.Arrays;
import java.util.Optional;

// TODO: 16/02/16 Transformer HeaderItem en String
// TODO: 16/02/16 Remplir avec les headers à implémenter
public enum ERequestHeaderItem {
    ACCEPT("Accept"),
    EXPECT("Expect"),
    HOST("Host"),
    USERAGENT("User-Agent"),
    UNKNOWN("Unknown");
    private String name;

    ERequestHeaderItem(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

    public static ERequestHeaderItem getItem(String name){
        Optional<ERequestHeaderItem> itemOptional =
                Arrays.asList(ERequestHeaderItem.values()).stream()
                .filter(item -> item.toString().equals(name))
                .findAny();

        return itemOptional.isPresent() ? itemOptional.get() : UNKNOWN;
    }


}
