package com.upmc.stl.framework.process;

import com.upmc.stl.framework.common.enums.ProtocolVersions;
import com.upmc.stl.framework.process.interfaces.IProcess;
import com.upmc.stl.framework.request.enums.ERequestHeaderItem;
import com.upmc.stl.framework.request.interfaces.IMethod;
import com.upmc.stl.framework.request.interfaces.IRequest;
import com.upmc.stl.framework.response.enums.EResponseHeaderItem;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.ResponseBuilder;
import com.upmc.stl.framework.response.interfaces.IResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.Set;

import static com.upmc.stl.framework.request.enums.ERequestHeaderItem.*;

public class ProcessEcho implements IProcess {

    @Override
    public IResponse run(IRequest request) {
        Set<String> acceptes = request.getHeader().get(ACCEPT);
        String contentType = selectContentType(acceptes);
        String message = buildContent(contentType, request);
        return new ResponseBuilder()
                .protocol(ProtocolVersions.HTTP_1_1)
                .status(EStatus.OK)
                .header(EResponseHeaderItem.CONTENT_TYPE, contentType)
                .header(EResponseHeaderItem.CONTENT_LENGTH, message.length() + "")
                .content(message)
                .build();
    }

    public String selectContentType(Set<String> contentTypes){
        if(contentTypes.contains("text/html"))
            return  "text/html";
        if(contentTypes.contains("application/json"))
            return "application/json";
        return "text/plain";
    }
    public String buildContent(String contentType,IRequest request){
        switch (contentType){
            case "text/plain":
                return buildContentPlain(request);
            case "text/html":
                return  buildContentHTML(request);
            case "application/json":
                return buildContentJSON(request);
            default:
                return "";
        }
    }

    private String buildContentJSON(IRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String buildContentHTML(IRequest request) {
        String html = "<html><head><title>Serveur Echo</title></head><body>%s</body/></html>";

        IMethod method = request.getMethod();
        String firstLine = String.format("<p>Methode : %s</p><p>Resource : %s</p><p>Protocole : %s</p>",
                method.getMethodType(),
                method.getURL(),
                method.getProtocol());

        StringBuilder headerTable=new StringBuilder("<table border=\"1px\"><tr><th>Item</th><th>Value</th></tr>");
        String headerLine = "<tr><td>%s</td><td>%s</td></tr>";
        for(ERequestHeaderItem item:ERequestHeaderItem.values()){
            headerTable.append(String.format(headerLine,item,request.getHeader().get(item)));
        }
        headerTable.append("</table>");

        String content = "<p>Content :" + request.getContent() + "</p>";
        return String.format(html,firstLine+headerTable.toString()+content);
    }

    private String buildContentPlain(IRequest request) {
        return request.toString();
    }
}
