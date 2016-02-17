package com.upmc.stl.framework.process;

import com.upmc.stl.framework.common.enums.EProtocol;
import com.upmc.stl.framework.process.interfaces.IProcess;
import com.upmc.stl.framework.request.enums.ERequestHeaderItem;
import com.upmc.stl.framework.request.interfaces.IMethod;
import com.upmc.stl.framework.request.interfaces.IRequest;
import com.upmc.stl.framework.response.enums.EResponseHeaderItem;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.implem.ResponseBuilder;
import com.upmc.stl.framework.response.interfaces.IResponse;

import java.util.Set;

import static com.upmc.stl.framework.request.enums.ERequestHeaderItem.*;

public class ProcessEcho implements IProcess {

    @Override
    public IResponse run(IRequest request) {
        Set<String> acceptes = request.getHeader().getValues(ACCEPT);
        String contentType = selectContentType(acceptes);
        System.out.println(" ----> " + contentType);
        String message = buildContent(contentType, request);
        IResponse response = new ResponseBuilder()
                .protocol(EProtocol.HTTP_1_1)
                .status(EStatus.OK)
                .header(EResponseHeaderItem.CONTENT_TYPE, contentType)
                .header(EResponseHeaderItem.CONTENT_LENGTH, message.length() + "")
                .content(message)
                .build();
        return response;
    }

    public String selectContentType(Set<String> contentTypes){
        // TODO: 17/02/16 string in list
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

    // TODO: 17/02/16 Enlever commentaires
    private String buildContentJSON(IRequest request) {
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);
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
            headerTable.append(String.format(headerLine,item,request.getHeader().getValues(item)));
        }
        headerTable.append("</table>");

        String content = "<p>Content :" + request.getContent() + "</p>";
        return String.format(html,firstLine+headerTable.toString()+content);
    }

    private String buildContentPlain(IRequest request) {
        return request.toString();
    }
}
