package com.wasp.configuration.wasp_conf;

import java.util.List;

public class WaspServer {
    private int port;
    private List<Application> applications;

    public WaspServer() {
    }

    public WaspServer(int port, List<Application> applications) {
        this.port = port;
        this.applications = applications;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    @Override
    public String toString() {
        return "WaspConfig{" +
                "port=" + port +
                ", applications=" + applications +
                '}';
    }
}
