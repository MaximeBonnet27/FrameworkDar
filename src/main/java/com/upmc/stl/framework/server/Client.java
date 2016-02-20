package com.upmc.stl.framework.server;

import com.upmc.stl.framework.request.exceptions.MethodeTypeException;
import com.upmc.stl.framework.request.interfaces.IHttpRequest;
import com.upmc.stl.framework.response.enums.HttpResponseHeaderFields;
import com.upmc.stl.framework.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static final Logger logger= Logger.getLogger(Client.class);
    private final Socket socket;
    private IHttpRequest request;

    public Client(Socket socket) {
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
