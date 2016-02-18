package com.upmc.stl.framework.response.enums;

// TODO: 16/02/16 Transformer les Header item en String
// TODO: 16/02/16 Remplir avec les headers à implémenter
public enum EResponseHeaderItem {
    CONTENT_ENCODING("Content-Encoding"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    DATE("Date") // RFC 1123-[8] format
    ;

    private String name;

    EResponseHeaderItem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
