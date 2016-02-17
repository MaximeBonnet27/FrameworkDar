package com.upmc.stl.framework.server;

import com.upmc.stl.framework.common.enums.EProtocol;
import com.upmc.stl.framework.process.ProcessEcho;
import com.upmc.stl.framework.process.interfaces.IProcess;
import com.upmc.stl.framework.request.enums.EMethodType;
import com.upmc.stl.framework.request.enums.ERequestHeaderItem;
import com.upmc.stl.framework.request.implem.Method;
import com.upmc.stl.framework.request.implem.Request;
import com.upmc.stl.framework.request.implem.RequestHeader;
import com.upmc.stl.framework.request.interfaces.IMethod;
import com.upmc.stl.framework.request.interfaces.IRequest;
import com.upmc.stl.framework.request.interfaces.IRequestHeader;
import com.upmc.stl.framework.response.enums.EResponseHeaderItem;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.ResponseBuilder;
import com.upmc.stl.framework.response.interfaces.IResponse;
import com.upmc.stl.framework.server.AbstractServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ServerEcho extends AbstractServer{

    public ServerEcho(int port) {
        super(port, new ProcessEcho());
    }
}