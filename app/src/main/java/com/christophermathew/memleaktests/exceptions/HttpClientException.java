package com.christophermathew.memleaktests.exceptions;

public class HttpClientException extends HttpStatusCodeException {
    public HttpClientException(int responseCode) {
        super(responseCode);
    }
}
