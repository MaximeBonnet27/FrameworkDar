package com.wasp.server;

import com.wasp.util.httpComponent.request.exceptions.MethodeTypeException;
import com.wasp.util.httpComponent.request.implem.HttpSession;
import com.wasp.util.httpComponent.request.interfaces.IHttpRequest;
import com.wasp.util.httpComponent.response.enums.HttpResponseHeaderFields;
import com.wasp.util.httpComponent.response.implem.HttpCookie;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;
import org.apache.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Set;

import static com.wasp.util.httpComponent.common.enums.HttpContentTypes.getAllContentTypes;
import static com.wasp.util.httpComponent.request.enums.HttpRequestHeaderFields.ACCEPT;

public class HttpClient {
    private static final Logger logger= Logger.getLogger(HttpClient.class);
    //to move in a BD with a batch to remove all expired sessions
    private static final HashMap<String,HttpSession> sessions=new HashMap<>();

    private final Socket socket;
    private IHttpRequest request;

    public HttpClient(Socket socket) {
        this.socket = socket;
        this.request=null;

    }

    public IHttpRequest getHttpRequest() throws IOException, MethodeTypeException {
        if(request==null)
            this.request=HttpRequestParser.parse(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        if(this.request.getHeader().get(ACCEPT).contains("*/*"))
            this.request.getHeader().get(ACCEPT).addAll(getAllContentTypes());
        this.request.setHttpSession(getSession());
        return request;
    }

    private HttpSession getSession() {
        String clientKey = getClientKey();
        if(!sessions.containsKey(clientKey))
            sessions.put(clientKey,new HttpSession(clientKey));
        HttpSession httpSession = sessions.get(clientKey);
        if(httpSession.getExpireDate().compareTo(httpSession.getCreationDate())<1)
            sessions.put(clientKey,new HttpSession(clientKey));
        return httpSession;
    }

    public void sendHttpResponse(IHttpResponse response){
        if(request!=null){
            response.setProtocol(request.getMethod().getProtocol());
            int content_length=0;
            if(response.getContent()!=null)
                content_length=response.getContent().length();
            response.getHeader().addItem(HttpResponseHeaderFields.CONTENT_LENGTH, String.valueOf(content_length));
            response.getHeader().addItem(HttpResponseHeaderFields.DATE, getHttpDate());

            HttpCookie cookie = new HttpCookie();
            cookie.add("sessionKey", getClientKey());
            response.setCookie(cookie);

            //todo ajouter dans entete
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            bos.write(response.toString().getBytes());
            bos.flush();
            bos.close();
            //logger.info("send "+response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getHttpDate(){
        return java.time.format.DateTimeFormatter
                .RFC_1123_DATE_TIME
                .format(getGmt());
    }

    private ZonedDateTime getGmt() {
        return ZonedDateTime.now(ZoneId.of("GMT"));
    }


    private String getClientKey(){
        Set<String> strings = request.getHeader().get("User-Agent");
        return strings.toArray(new String[strings.size()])[0].replaceAll("[ ;]", "")+socket.getInetAddress().toString();
    }

    public void shutdown(){
        try {
            socket.close();
        } catch (IOException e) {
            logger.fatal(e.getMessage());
        }
    }
}
