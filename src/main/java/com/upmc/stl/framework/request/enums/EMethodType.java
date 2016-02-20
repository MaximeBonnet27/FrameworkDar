package com.upmc.stl.framework.request.enums;

import com.upmc.stl.framework.request.exceptions.MethodeTypeException;

import java.util.Arrays;
import java.util.Optional;

public enum EMethodType {

    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    TRACE,
    OPTIONS,
    CONNECT,
    PATCH;

    public static EMethodType getMethod(String name) throws MethodeTypeException {
        Optional<EMethodType> itemOptional = Arrays.asList(EMethodType.values()).stream()
                .filter(e -> e.toString().equals(name))
                .findFirst();
        if(!itemOptional.isPresent())
            throw new MethodeTypeException("Unknown methode http "+name);
        return itemOptional.get();
    }
}

