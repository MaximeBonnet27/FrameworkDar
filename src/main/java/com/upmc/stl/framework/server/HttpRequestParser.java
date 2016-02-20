package com.upmc.stl.framework.server;

import com.upmc.stl.framework.request.enums.EMethodType;
import com.upmc.stl.framework.request.enums.HttpRequestHeaderFields;
import com.upmc.stl.framework.request.exceptions.MethodeTypeException;
import com.upmc.stl.framework.request.implem.HttpRequest;
import com.upmc.stl.framework.request.implem.HttpRequestHeader;
import com.upmc.stl.framework.request.interfaces.IMethod;
import com.upmc.stl.framework.request.interfaces.IHttpRequest;

import java.io.*;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Set;

public abstract class HttpRequestParser {

    //TODO 19/02/16 ajouter exception parser et verifier taille des tableaux
    public static IHttpRequest parser(BufferedReader br) throws IOException, MethodeTypeException {
        IHttpRequest request = new HttpRequest();

        //Parse first line protocol
        IMethod method = request.getMethod();
        String[] protocolTokens = br.readLine().split(" ");
        method.setMethodType(EMethodType.getMethod(protocolTokens[0]));
        method.setURL(protocolTokens[1]);
        method.setProtocol(protocolTokens[2]);

        //Parse Header
        HttpRequestHeader header = request.getHeader();
        String line;
        while (!(line=br.readLine()).isEmpty()){
            String[] entries = line.split(":");
            entries[0]=entries[0].trim();
            entries[1]=entries[1].trim();
            header.addItems(entries[0], Arrays.asList(entries[1].split(";")));
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
