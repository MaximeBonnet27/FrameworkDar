package com.upmc.stl.framework.process;

import com.upmc.stl.framework.common.enums.HttpProtocolVersions;
import com.upmc.stl.framework.process.interfaces.IProcess;
import com.upmc.stl.framework.request.interfaces.IMethod;
import com.upmc.stl.framework.request.interfaces.IHttpRequest;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.HttpResponseBuilder;
import com.upmc.stl.framework.response.interfaces.IHttpResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Set;

import static com.upmc.stl.framework.request.enums.HttpRequestHeaderFields.ACCEPT;
import static com.upmc.stl.framework.response.enums.HttpResponseHeaderFields.*;

public class ProcessEcho implements IProcess {

    @Override
    public IHttpResponse run(IHttpRequest request) {
        Set<String> acceptes = request.getHeader().get(ACCEPT);
        String contentType = selectContentType(acceptes);
        String message = buildContent(contentType, request);
        return new HttpResponseBuilder()
                .protocol(HttpProtocolVersions.HTTP_1_1)
                .status(EStatus.OK)
                .header(CONTENT_TYPE, contentType)
                .header(CONTENT_LENGTH, message.length() + "")
                .content(message)
                .build();
    }

    public String selectContentType(Set<String> contentTypes) {
        if (contentTypes.contains("text/html"))
            return "text/html";
        if (contentTypes.contains("application/json"))
            return "application/json";
        return "text/plain";
    }

    public String buildContent(String contentType, IHttpRequest request) {
        switch (contentType) {
            case "text/plain":
                return buildContentPlain(request);
            case "text/html":
                return buildContentHTML(request);
            case "application/json":
                return buildContentJSON(request);
            default:
                return "";
        }
    }

    private String buildContentJSON(IHttpRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String buildContentHTML(IHttpRequest request) {
        String html = "<html><head><title>Serveur Echo</title></head><body>%s</body></html>";

        IMethod method = request.getMethod();
        String firstLine = String.format("<p>Methode : %s</p><p>Resource : %s</p><p>Protocole : %s</p>",
                method.getMethodType(),
                method.getURL(),
                method.getProtocol());

        StringBuilder headerTable = new StringBuilder("<table border=\"1px\"><tr><th>Item</th><th>Value</th></tr>");
        String headerLine = "<tr><td>%s</td><td>%s</td></tr>";

        request.getHeader()
                .entrySet()
                .stream()
                .forEach(entry -> String.format(headerLine, entry.getKey(), entry.getValue()));
        headerTable.append("</table>");

        String content = "<p>Content :" + request.getContent() + "</p>";
        return String.format(html, firstLine + headerTable.toString() + content);
    }

    private String buildContentPlain(IHttpRequest request) {
        return request.toString();
    }
}
