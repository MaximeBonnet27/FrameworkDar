package com.wasp.configuration.wasp;

@SuppressWarnings("unused")
public class Argument {
    private String sourceType;
    private String sourceRef;
    private String Type;

    public Argument() {
    }

    public Argument(String sourceType, String sourceRef, String type) {
        this.sourceType = sourceType;
        this.sourceRef = sourceRef;
        Type = type;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "Argument{" +
                "sourceType='" + sourceType + '\'' +
                ", sourceRef='" + sourceRef + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
