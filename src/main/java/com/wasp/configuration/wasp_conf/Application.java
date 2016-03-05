package com.wasp.configuration.wasp_conf;

public class Application {
    private String context;
    private String location;
    private String description;

    public Application() {
    }

    public Application(String context, String location, String description) {
        this.context = context;
        this.location = location;
        this.description = description;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Application{" +
                "context='" + context + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
