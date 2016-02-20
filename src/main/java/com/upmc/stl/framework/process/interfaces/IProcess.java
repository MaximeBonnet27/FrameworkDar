package com.upmc.stl.framework.process.interfaces;

import com.upmc.stl.framework.request.interfaces.IHttpRequest;
import com.upmc.stl.framework.response.interfaces.IHttpResponse;

public interface IProcess {
    IHttpResponse run(IHttpRequest request);
}
