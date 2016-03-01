package com.wasp.server.process.router;

import com.wasp.server.process.router.exceptions.MappingException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.request.interfaces.IUrl;
import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.wasp.util.httpComponent.response.enums.EStatus.BAD_MAPPING;
import static com.wasp.util.httpComponent.response.enums.EStatus.INTERNAL_SERVER_ERROR;
import static com.wasp.util.httpComponent.response.enums.EStatus.NOT_FOUND;

public abstract class DefaultResponseFactory {

    private static IHttpResponse create(EStatus status,String resource,String description,String exception){
        StringBuilder builder = new StringBuilder();
        builder.append("<html><head><title>Wasp</title></head>")
                .append("<body>")
                .append("<h1>HTTP Status ").append(status.getCode()).append(" (").append(status.getMessage()).append(")").append(" - ").append(resource).append("</h1>");
        if(description!=null)
            builder.append("<p>description:").append(description).append("</p>");
        if(exception!=null)
            builder.append("<pre>exception:\n").append(exception).append("</pre>");
        builder.append("</body></html>");

        return new HttpResponseBuilder().status(status).content(builder.toString()).build();
    }

    public static IHttpResponse createResponseNoApplicationFoundForContext(IHttpRequest request) {
        IUrl url = request.getMethod().getUrl();
        return create(NOT_FOUND, url.toString(),"No application found for context "+url.getContext(),null);
    }



    public static IHttpResponse createResponseMappingException(MappingException exception, IHttpRequest request) {
        StringWriter stack = new StringWriter();
        exception.printStackTrace(new PrintWriter(stack));
        return create(BAD_MAPPING,request.getMethod().getUrl().toString(),null,stack.toString());
    }

    public static IHttpResponse createResponseInternalError(Exception exception, IHttpRequest request) {
        StringWriter stack = new StringWriter();
        exception.printStackTrace(new PrintWriter(stack));
        return create(INTERNAL_SERVER_ERROR,request.getMethod().getUrl().toString(),null,stack.toString());

    }
}