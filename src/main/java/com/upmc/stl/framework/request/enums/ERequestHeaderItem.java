package com.upmc.stl.framework.request.enums;

// TODO: 16/02/16 Remplir avec les headers à implémenter
public enum ERequestHeaderItem {
    ACCEPT("Accept");
    private String name;

    ERequestHeaderItem(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
