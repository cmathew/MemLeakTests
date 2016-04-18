package com.christophermathew.memleaktests.exceptions;

public class HttpStatusCodeException extends Throwable {
    protected int statusCode;

    public HttpStatusCodeException(int statusCode) {
        super(String.format("Http Response code %d", statusCode));

        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
