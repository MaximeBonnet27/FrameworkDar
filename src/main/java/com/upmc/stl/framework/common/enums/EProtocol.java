package com.upmc.stl.framework.common.enums;

import java.util.Arrays;

public enum EProtocol {

    HTTP_0_9("0.9"),
    HTTP_1_0("1.0"),
    HTTP_1_1("1.1"),
    HTTP_1_1_bis("1.1"),
    HTTP_2("2");

    EProtocol(String name) {
        this.name = "HTTP/"+name;
    }

    private String name;

    @Override
    public String toString() {
        return name;
    }

    public static EProtocol getProtocol(String protocolName){
        return Arrays.asList(EProtocol.values()).stream()
                .filter(name -> name.toString().equals(protocolName))
                .findFirst()
                .get();
    }


}
