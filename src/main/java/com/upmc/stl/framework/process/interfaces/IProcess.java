package com.upmc.stl.framework.process.interfaces;

import com.upmc.stl.framework.request.interfaces.IRequest;
import com.upmc.stl.framework.response.interfaces.IResponse;

import java.net.Socket;

public interface IProcess {
    IResponse run(IRequest request);
}
