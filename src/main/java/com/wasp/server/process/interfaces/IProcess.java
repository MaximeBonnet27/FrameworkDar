package com.wasp.server.process.interfaces;

import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;

public interface IProcess {
    IHttpResponse run(IHttpRequest request);
}
