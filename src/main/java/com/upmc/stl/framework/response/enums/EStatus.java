package com.upmc.stl.framework.response.enums;

// TODO: 16/02/16 A remplir avec les codes
public enum EStatus {
    /* SUCCESSFUL RESPONSES */
    OK(200,"OK"),
    /* CLIENT ERROR */
    BAD_REQUEST(400,"Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404,"Not Found"),
    EXPECTATION_FAILED(417,"Expectation Failed"),
    /* SERVER ERROR */
    INTERNAL_SERVER_ERROR(500,"Internal Server Error"),;

    private final int code;
    private final String message;

    EStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code + " " + message;
    }
}
