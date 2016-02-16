package com.upmc.stl.framework.common.enums;

// TODO: 16/02/16 Verifier les codes
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

}
