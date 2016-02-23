package com.wasp.server;

import com.wasp.util.httpComponent.request.exceptions.MethodeTypeException;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class HttpClient {
    private static final Logger logger= Logger.getLogger(HttpClient.class);
    private final Socket socket;
    private IHttpRequest request;

    public HttpClient(Socket socket) {
        this.socket = socket;
        this.request=null;

    }

    public IHttpRequest getHttpRequest() throws IOException, MethodeTypeException {
        if(request==null)
            this.request=HttpRequestParser.parser(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        return request;
    }

    public void sendHttpResponse(IHttpResponse response){
        if(request!=null){
            response.setProtocol(request.getMethod().getProtocol());
            int content_length=0;
            if(response.getContent()!=null)
                content_length=response.getContent().length();
            response.getHeader().addItem(HttpResponseHeaderFields.CONTENT_LENGTH, String.valueOf(content_length));

            //todo ajouter dans entete genre date etc
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            bos.write(response.toString().getBytes());
            bos.flush();
            bos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void shutdown(){
        try {
            socket.close();
        } catch (IOException e) {
            logger.fatal(e.getMessage());
        }
    }
}