package com.upmc.stl.framework.server;

import com.upmc.stl.framework.process.ProcessEcho;

public class ServerEcho extends AbstractServer{

    public ServerEcho(int port) {
        super(port, new ProcessEcho());
    }
}