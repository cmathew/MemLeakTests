package com.christophermathew.memleaktests;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/* Adds a custom User-Agent and Content-Type */
public class HeaderInterceptor implements Interceptor {
    public final static String CONTENT_TYPE = "application/json";

    protected String userAgent;

    public HeaderInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request requestWithHeaders = originalRequest.newBuilder()
                .addHeader("User-Agent", userAgent)
                .addHeader("Content-Type", CONTENT_TYPE)
                .addHeader("Accept", CONTENT_TYPE)
                .build();

        return chain.proceed(requestWithHeaders);
    }
}
