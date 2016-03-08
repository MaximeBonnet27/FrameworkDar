package com.wasp.server.process;

import com.wasp.server.process.interfaces.IProcess;
import com.wasp.util.httpComponent.common.enums.HttpContentTypes;
import com.wasp.util.httpComponent.common.enums.HttpProtocolVersions;
import com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.request.interfaces.IMethod;
import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.implem.HttpResponseBuilder;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Set;

import static com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields.CONTENT_LENGTH;
import static com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields.CONTENT_TYPE;

public class ProcessEcho implements IProcess {

    @Override
    public IHttpResponse run(IHttpRequest request) {
        Set<String> acceptes = request.getHeader().get(HttpRequestHeaderFields.ACCEPT);
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
        if (contentTypes.contains(HttpContentTypes.TEXT_HTML))
            return HttpContentTypes.TEXT_HTML;
        if (contentTypes.contains(HttpContentTypes.APPLICATION_JSON))
            return HttpContentTypes.APPLICATION_JSON;
        return HttpContentTypes.TEXT_PLAIN;
    }

    public String buildContent(String contentType, IHttpRequest request) {
        switch (contentType) {
            case HttpContentTypes.TEXT_PLAIN:
                return buildContentPlain(request);
            case HttpContentTypes.TEXT_HTML:
                return buildContentHTML(request);
            case HttpContentTypes.APPLICATION_JSON:
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
                method.getUrl(),
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
