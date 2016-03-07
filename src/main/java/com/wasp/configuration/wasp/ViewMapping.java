package com.wasp.configuration.wasp;

@SuppressWarnings("unused")
public class ViewMapping {
    private String prefix;
    private String suffix;

    public ViewMapping() {
    }

    public ViewMapping(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public String toString() {
        return "ViewMapping{" +
                "prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                '}';
    }
}
