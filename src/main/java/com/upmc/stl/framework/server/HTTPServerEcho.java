package com.upmc.stl.framework.server;

import com.upmc.stl.framework.process.ProcessEcho;

import java.io.IOException;

public class HttpServerEcho extends AbstractHttpServer {

    public HttpServerEcho(int port) throws IOException {
        super(port, new ProcessEcho());
    }
}