package com.wasp.util.httpComponent.response.implem;

import com.wasp.util.httpComponent.response.enums.EStatus;
import com.wasp.util.httpComponent.response.interfaces.IHttpResponse;

public class HttpResponse implements IHttpResponse {

    private String protocol;
    private EStatus status;
    private HttpResponseHeader header;
    private String content;

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
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return protocol + " " + status + "\r\n"
                + header // Le dernier header contient déjà le retour à la ligne
                + "\r\n" // Saut de ligne entre header et content
                + content;
    }
}
