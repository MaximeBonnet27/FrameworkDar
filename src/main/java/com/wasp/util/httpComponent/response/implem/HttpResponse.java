package com.wasp.util.httpComponent.response.implem;

import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;

import java.util.List;

public class HttpResponse implements IHttpResponse {

    private String protocol;
    private EStatus status;
    private HttpResponseHeader header;
    private byte[] content;
    private Object entity;

    public HttpResponse() {
        header = new HttpResponseHeader();
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public EStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(EStatus status) {
        this.status = status;
    }

    @Override
    public HttpResponseHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(HttpResponseHeader header) {
        this.header = header;
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content.getBytes();
    }

    @Override
    public List<HttpCookie> getCookies() {
        return  getHeader().getCookies();
    }

    @Override
    public void setCookie(HttpCookie cookie) {
        getHeader().addCookie(cookie);
    }

    @Override
    public Object getEntity() {
        return this.entity;
    }

    @Override
    public void setEntity(Object o) {
        this.entity=o;
    }

    @Override
    public void setContent(byte[] content) {
        this.content=content;
    }

    @Override
    public byte[] toByte() {
         byte[] firstBytes=(protocol + " " + status + "\r\n"
                 + header).getBytes();

        int contentLength=0;
        if(content!=null)
            contentLength=content.length;
        byte[] bytes = new byte[firstBytes.length + contentLength];

        int currentIndex=0;
        for(int i=0;i<firstBytes.length;i++){
            bytes[currentIndex]=firstBytes[i];
            currentIndex++;
        }
        for(int i=0;i<contentLength;i++){
            bytes[currentIndex]=content[i];
            currentIndex++;
        }
        return bytes;
    }

    @Override
    public String toString() {
        return protocol + " " + status + "\r\n"
                + header;// Le dernier header contient déjà le retour à la ligne
    }


}
