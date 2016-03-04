package com.wasp.util.httpComponent.request.implem;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;

public final class HttpSession  extends HashMap<String,Object>{
    private final String id;
    private final ZonedDateTime creationDate;
    private ZonedDateTime expireDate;

    public HttpSession(String id) {
        super();
        this.id = id;
        this.creationDate= ZonedDateTime.now(ZoneId.of("GMT"));
        //TODO dans fichier de wasp-conf
        expireDate=creationDate.plusDays(30);
    }

    public String getId() {
        return id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public ZonedDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(ZonedDateTime expireDate) {
        this.expireDate = expireDate;
    }
}
