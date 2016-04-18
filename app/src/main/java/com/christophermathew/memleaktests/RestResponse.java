package com.christophermathew.memleaktests;

public class RestResponse {
    private String data;
    private int code;

    public RestResponse(String data, int code) {
        this.data = data;
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }
}
