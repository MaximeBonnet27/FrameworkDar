package com.wasp.util.httpComponent.response.enums;

import com.wasp.util.httpComponent.response.exceptions.StatusException;

import java.util.Arrays;
import java.util.Optional;

@SuppressWarnings("unused")
public enum EStatus {
    /* INFORMATION */
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),
    PROCESSING(102, "Processing"),

    /* REDIRECTION */
    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    MOVED_TEMPORARILY(302, "Moved Temporarily"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    USE_PROXY(305, "Use Proxy"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    PERMANENT_REDIRECT(308, "Permanent Redirect"),
    TOO_MANY_REDIRECTS(310, "Too many Redirects"),

    /* SUCCESSFUL RESPONSES */
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MULTI_STATUS(207, "Multi-Status"),
    CONTENT_DIFFERENT(201, "Content Different"),
    IM_USED(226, "IM Used"),

    /* CLIENT ERROR */
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(406, "Proxy Authentication Required"),
    REQUEST_TIME_OUT(408, "Request Time-out"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411,"Length Required"),
    PRECONDITION_FAILED(412,"Precondition Failed"),
    REQUEST_ENTITY_TOO_LARGE(413,"Request Entity Too Large"),
    REQUEST_URI_TOO_LONG(413,"Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415,"Unsupported Media Type"),
    REQUESTED_RANGE_UNSATISFIABLE(416,"Requested range unsatisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    IM_A_TEAPOT(418,"I'm a teapot"),
    BAD_MAPPING(421,"Bad mapping"),
    MISDIRECTED_REQUEST(421,"Misdirected Request"),
    UNPROCESSABLE_ENTITY(422,"Unprocessable entity"),
    LOCKED(423,"Locked"),
    METHOD_FAILURE(424,"Method failure"),
    UNORDERED_COLLECTION(425,"Unordered Collection"),
    UPGRADE_REQUIRED(426,"Upgrade Required"),
    PRECONDITION_REQUIRED(428,"Precondition Required"),
    TOO_MANY_REQUEST(429,"Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431,"Request Header Fields Too Large"),
    RETRY_WITH(449,"Retry With"),
    BLOCKED_BY_WINDOWS_PARENTAL_CONTROLS(450,"Blocked by Windows Parental controls"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451,"Unavailable For Lebal Reasons"),
    UNRECOVERABLE_ERROR(456,"Unrecoverable Error"),
    CLIENT_HAS_CLOSED_CONNECTION(499,"client has closed connection"),

    /* SERVER ERROR */
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501,"not Implemented"),
    BAD_GATEWAY(502,"Bad Gateway"),
    PROXY_ERROR(502,"Proxy Error"),
    SERVICE_UNAVAILABLE(503,"Service Unavailable"),
    GATEWAY_TIME_OUT(504,"Gateway Time-out"),
    HTTP_Version_NOT_SUPPORTED(505,"HTTP Version not supported"),
    VARIANT_ALSO_NEGOCIATE(506,"Variant also negociate"),
    INSUFFICIENT_STORAGE(507,"Insufficient storage"),
    LOOP_DETECTED(508,"Loop detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509,"Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510,"Not extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511,"Network authentication required"),
    UNKNOWN(520,"Web server is returning an unknown error");

    public static EStatus getStatus(int code) throws StatusException {
        Optional<EStatus> statusOptional = Arrays.asList(EStatus.values()).stream()
                .filter(e -> e.getCode() == code)
                .findFirst();
        if(!statusOptional.isPresent())
            throw new StatusException("Unknown status code "+code);
        return statusOptional.get();
    }

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
