package com.christophermathew.memleaktests.exceptions;

public class HttpServerException extends HttpStatusCodeException {
    public HttpServerException(int responseCode) {
        super(responseCode);
    }
}
