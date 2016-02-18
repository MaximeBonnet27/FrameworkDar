package com.upmc.stl.framework.response.implem;

import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.interfaces.IResponse;

public class Response implements IResponse {

    private String protocol;
    private EStatus status;
    private ResponseHeader header;
    private String content;

    public Response() {
        header = new ResponseHeader();
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
    public ResponseHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(ResponseHeader header) {
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
