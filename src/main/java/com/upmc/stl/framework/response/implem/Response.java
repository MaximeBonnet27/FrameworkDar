package com.upmc.stl.framework.response.implem;

import com.upmc.stl.framework.common.enums.EProtocol;
import com.upmc.stl.framework.response.enums.EStatus;
import com.upmc.stl.framework.response.interfaces.IResponse;
import com.upmc.stl.framework.response.interfaces.IResponseHeader;

public class Response implements IResponse {

    private EProtocol protocol;
    private EStatus status;
    private IResponseHeader header;
    private String content;

    public Response() {
        header = new ResponseHeader();
    }

    public Response(EProtocol protocol, EStatus status, IResponseHeader header, String content) {
        this.protocol = protocol;
        this.status = status;
        this.header = header;
        this.content = content;
    }

    @Override
    public EProtocol getProtocol() {
        return protocol;
    }

    @Override
    public void setProtocol(EProtocol protocol) {
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
    public IResponseHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(IResponseHeader header) {
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
        return protocol + " " + status + "\n"
                + header + "\n"
                + content;
    }
}
