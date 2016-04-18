package com.christophermathew.memleaktests;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.christophermathew.memleaktests.exceptions.HttpClientException;
import com.christophermathew.memleaktests.exceptions.HttpServerException;
import com.christophermathew.memleaktests.exceptions.HttpStatusCodeException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import okhttp3.CertificatePinner;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RestClient {
    private final static String LOG_TAG = "RESTClient";

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    public final static int READ_TIMEOUT_MS    = 10000;
    public final static int CONNECT_TIMEOUT_MS = 15000;

    protected HttpUrl apiRoot;
    protected OkHttpClient client;

    public static String UserAgent;

    public RestClient(Context ctx, EnvironmentProvider environmentProvider) {
        this.apiRoot = GenerateRootUrl(ctx, environmentProvider);

        CertificatePinner cp = buildCertificatePinner(ctx);
        this.client = buildOkHttpClient().certificatePinner(cp).build();
    }

    public RestClient(OkHttpClient client, HttpUrl rootUrl) {
        this.apiRoot = rootUrl;
        this.client = client;
    }

    protected OkHttpClient.Builder buildOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    }

    protected CertificatePinner buildCertificatePinner(Context ctx) {
        //int headerId = ctx.getResources().getIdentifier(headerIdentifier, "string", ctx.getPackageName());

        CertificatePinner.Builder cpBuilder = new CertificatePinner.Builder();
        String host = ctx.getString(R.string.cert_hostname);

        String cert1 = ctx.getString(R.string.cert_pin_1);
        cpBuilder.add(host, cert1);

        String cert2 = ctx.getString(R.string.cert_pin_2);
        cpBuilder.add(host, cert2);

        return cpBuilder.build();
    }

    protected HttpUrl buildUrlWithParams(HttpUrl url, HashMap<String, String> searchParams) {
        HttpUrl searchUrl;
        if (searchParams == null) {
            searchUrl = appendQueryParams(url, getBaseQueryParams());
        } else {
            searchUrl = appendQueryParams(url, searchParams);
        }

        return searchUrl;
    }

    protected static class JsonObjectResponder implements Func1<RestResponse, JSONObject> {
        @Nullable
        @Override
        public JSONObject call(RestResponse response) {
            String body = response.getData();
            if (body == null) {
                return null;
            }

            try {
                return new JSONObject(body);
            } catch (JSONException e) {
                return null;
            }
        }
    }

    protected static class JsonArrayResponder implements Func1<RestResponse, JSONArray> {
        @Nullable
        @Override
        public JSONArray call(RestResponse response) {
            String body = response.getData();
            if (body == null) {
                return null;
            }

            try {
                return new JSONArray(body);
            } catch (JSONException e) {
                return null;
            }
        }
    }

    public static HttpUrl GenerateRootUrl(Context context, EnvironmentProvider env) {
        // choose host
        if (env.isProduction()) {
            return HttpUrl.parse(context.getString(R.string.production_api_host));
        } else if (env.isDevelopment()) {
            return HttpUrl.parse(context.getString(R.string.development_api_host));
        }

        return null;
    }

    /**
     * Method that calls POST to /drivers/login, to authenticate the driver, and
     * get their API key for subsequent API requests.
     */
    public Observable<Driver> login(String email, String password) {
        Map<String,String> put_data = new HashMap<>();
        put_data.put("email", email);
        put_data.put("password", password);

        JSONObject json = new JSONObject(put_data);
        Observable<RestResponse> obs = makePOSTRequest(getLoginURL(), null, json);
        return obs.map(new JsonObjectResponder()).map(new UserHandler());
    }

    /**
     * Unauthenticated action to send a recover password email
     */
    public Observable<RestResponse> forgotPassword(String email) {
        Map<String,String> post_data = new HashMap<>();
        post_data.put("email", email);
        final JSONObject json = new JSONObject(post_data);

        return makePOSTRequest(getForgotPasswordURL(), null, json);
    }

    protected Observable<RestResponse> makeGETRequest(HttpUrl url, HashMap<String, String> searchParams) {
        Log.v(LOG_TAG, "making GET request");

        HttpUrl searchUrl = buildUrlWithParams(url, searchParams);
        Log.v(LOG_TAG, "with URL " + searchUrl.toString());

        Request.Builder requestBuilder = new Request.Builder()
                .url(searchUrl)
                .get();

        Request request = requestBuilder.build();

        return makeHTTPRequest(request);
    }

    protected Observable<RestResponse> makePOSTRequest(HttpUrl url, HashMap<String, String> searchParams, @NonNull Object body) {
        Log.v(LOG_TAG, "making POST request");

        HttpUrl searchUrl = buildUrlWithParams(url, searchParams);
        Log.v(LOG_TAG, "with URL " + searchUrl.toString());

        RequestBody rb = RequestBody.create(MEDIA_TYPE, body.toString());
        Request.Builder requestBuilder = new Request.Builder()
                .url(searchUrl)
                .post(rb);

        Request request = requestBuilder.build();

        return makeHTTPRequest(request);
    }

    protected Observable<RestResponse> makePUTRequest(HttpUrl url, HashMap<String, String> searchParams, @NonNull Object body) {
        Log.v(LOG_TAG, "making PUT request");

        HttpUrl searchUrl = buildUrlWithParams(url, searchParams);
        Log.v(LOG_TAG, "with URL " + searchUrl.toString());

        RequestBody rb = RequestBody.create(MEDIA_TYPE, body.toString());
        Request.Builder requestBuilder = new Request.Builder()
                .url(searchUrl)
                .put(rb);

        Request request = requestBuilder.build();

        return makeHTTPRequest(request);
    }

    protected Observable<RestResponse> makeDELETERequest(HttpUrl url) {
        Log.v(LOG_TAG, "making DELETE request");

        HttpUrl searchUrl = buildUrlWithParams(url, null);
        Log.v(LOG_TAG, "with URL " + searchUrl.toString());

        Request.Builder requestBuilder = new Request.Builder()
                .url(searchUrl)
                .delete();

        Request request = requestBuilder.build();

        return makeHTTPRequest(request);
    }

    /**
     * Generalized method for making HTTP requests.
     */
    protected Observable<RestResponse> makeHTTPRequest(final Request request) {
        Observable<RestResponse> obs = Observable.create(new Observable.OnSubscribe<RestResponse>() {
            @Override
            public void call(Subscriber<? super RestResponse> subscriber) {
                Response response;
                try {
                    response = client.newCall(request).execute();
                } catch (IOException ex) {
                    subscriber.onError(ex);
                    subscriber.onCompleted();
                    return;
                }

                if (response.isSuccessful()) {
                    int responseCode = response.code();
                    try {
                        ResponseBody responseBody = response.body();
                        String responseString = responseBody.string();
                        responseBody.close();

                        RestResponse RestResponse = new RestResponse(responseString, responseCode);
                        subscriber.onNext(RestResponse);

                    } catch (IOException ex) {
                        subscriber.onError(ex);
                    }

                } else {
                    Throwable httpException;
                    int responseCode = response.code();

                    if (isClientError(responseCode)) {
                        httpException = new HttpClientException(responseCode);
                    } else if (isServerError(responseCode)) {
                        httpException = new HttpServerException(responseCode);
                    } else {
                        httpException = new HttpStatusCodeException(responseCode);
                    }
                    subscriber.onError(httpException);
                }
                subscriber.onCompleted();
            }
        });

        return obs.compose(this.<RestResponse>applyIOScheduler());
    }

    protected boolean isClientError(int responseCode) {
        return responseCode >= HttpURLConnection.HTTP_BAD_REQUEST &&
                responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR;
    }

    protected boolean isServerError(int responseCode) {
        return responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR;
    }

    private HttpUrl getLoginURL() {
        return getApiRoot().newBuilder()
                .addPathSegment("users")
                .addPathSegment("login")
                .build();
    }

    private HttpUrl getForgotPasswordURL() {
        return getApiRoot().newBuilder()
                .addPathSegment("users")
                .addPathSegment("forgot_password")
                .build();
    }

    protected HttpUrl appendQueryParams(HttpUrl uri, HashMap<String, String> params) {
        HttpUrl.Builder uriBuilder = uri.newBuilder();

        // Loop through our params and append them to the Uri.
        for (Map.Entry<String, String> param : params.entrySet()) {
            uriBuilder.setQueryParameter(param.getKey(), param.getValue());
        }

        return uriBuilder.build();
    }

    protected HashMap<String, String> getBaseQueryParams() {
        return new HashMap<>();
    }

    protected HashMap<String, String> generateQueryParams(HashMap<String, String> queryParams) {
        HashMap<String, String> params = getBaseQueryParams();
        params.putAll(queryParams);
        return params;
    }

    public HttpUrl getApiRoot() {
        return apiRoot;
    }

    public OkHttpClient getClient() {
        return client;
    }

    //================================================================================
    // Observables
    //================================================================================
    protected  <T> Observable.Transformer<T, T> applyIOScheduler() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io());
            }
        };
    }
}
