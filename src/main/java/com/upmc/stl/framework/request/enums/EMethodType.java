package com.upmc.stl.framework.request.enums;

import java.util.Arrays;
import java.util.Optional;

// TODO: 16/02/16 Rajouter attributs pour le parsing ?
public enum EMethodType {

    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    TRACE,
    OPTIONS,
    CONNECT,
    PATCH,
    UNKNWON;

    public static EMethodType getMethod(String name) {
        Optional<EMethodType> itemOptional = Arrays.asList(EMethodType.values()).stream()
                .filter(e -> e.toString().equals(name))
                .findFirst();
        return itemOptional.isPresent() ? itemOptional.get() : UNKNWON;
    }
}

