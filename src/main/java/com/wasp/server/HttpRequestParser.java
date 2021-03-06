package com.wasp.server;

import com.wasp.util.httpComponent.common.enums.HttpContentTypes;
import com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields;
import com.wasp.util.httpComponent.request.exceptions.MethodeTypeException;
import com.wasp.util.httpComponent.request.implem.HttpRequest;
import com.wasp.util.httpComponent.request.implem.HttpRequestHeader;
import com.wasp.util.httpComponent.request.implem.Url;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.request.interfaces.IMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public abstract class HttpRequestParser {

    public static IHttpRequest parse(BufferedReader br) throws IOException, MethodeTypeException {
        IHttpRequest request = new HttpRequest();

        //Parse first line protocol
        IMethod method = request.getMethod();
        String[] protocolTokens = br.readLine().split(" ");
        if(protocolTokens.length!=3)
            throw new IllegalArgumentException("first line is malformed");
        method.setMethodType(protocolTokens[0]);
        method.setUrl(new Url(protocolTokens[1]));
        method.setProtocol(protocolTokens[2]);

        //Parse Header
        HttpRequestHeader header = request.getHeader();
        String line;
        while (!(line=br.readLine()).isEmpty()){
            String[] entries = line.split(": ");
            if(entries.length!=2)
                throw new IllegalArgumentException("header is malformed");
            entries[0]=entries[0].trim();
            entries[1]=entries[1].trim();
            //TODO some headerItem need a specifique parser
            if(entries[0].equals("User-Agent"))
                header.addItems(entries[0], asList(entries[1]));
            if(entries[0].equals("Accept")){
                List<String> allContentTypes = HttpContentTypes.getAllContentTypes();
                allContentTypes.stream()
                        .filter(entries[1]::contains)
                        .forEach(contentType -> header.addItem(entries[0], contentType));
            }
            else
                header.addItems(entries[0], asList(entries[1].split(";")));
        }

        //Parser Content
        Set<String> content_length = request.getHeader().get(HttpRequestHeaderFields.CONTENT_LENGTH);
        int length=0;
        if (content_length != null && !content_length.isEmpty())
            length = Integer.parseInt(content_length.stream().findAny().get());
        if(length>0) {
            CharBuffer buffer = CharBuffer.allocate(length);
            br.read(buffer.array(),0,length);
            request.setContent(buffer.toString());
        }
        return request;
    }



}
