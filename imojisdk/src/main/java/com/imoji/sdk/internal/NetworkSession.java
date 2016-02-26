/*
 * Imoji Android SDK
 * Created by nkhoshini
 *
 * Copyright (C) 2016 Imoji
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KID, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

package com.imoji.sdk.internal;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.Session;
import com.imoji.sdk.StoragePolicy;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.objects.json.ArtistDeserializer;
import com.imoji.sdk.objects.json.CategoryDeserializer;
import com.imoji.sdk.objects.json.CategoryResultsDeserializer;
import com.imoji.sdk.objects.json.GenericNetworkResponsDeserializer;
import com.imoji.sdk.objects.json.ImojiDeserializer;
import com.imoji.sdk.objects.json.ImojiResultsDeserializer;
import com.imoji.sdk.objects.json.OAuthTokenDeserializer;
import com.imoji.sdk.response.NetworkResponse;
import com.imoji.sdk.response.OAuthTokenResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class NetworkSession implements Session {

    /**
     * HTTP Executor settings borrowed from Bolts-Android's AndroidExecutors.newCachedThreadPool
     * https://github.com/BoltsFramework/Bolts-Android/blob/master/bolts-tasks/src/main/java/bolts/AndroidExecutors.java
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;

    private static final long KEEP_ALIVE_TIME = 1L;

    private static final ExecutorService HTTPExecutor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>()
    );

    private static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(Imoji.class, new ArtistDeserializer())
            .registerTypeAdapter(Imoji.class, new CategoryDeserializer())
            .registerTypeAdapter(Imoji.class, new CategoryResultsDeserializer())
            .registerTypeAdapter(Imoji.class, new GenericNetworkResponsDeserializer())
            .registerTypeAdapter(Imoji.class, new ImojiDeserializer())
            .registerTypeAdapter(Imoji.class, new ImojiResultsDeserializer())
            .registerTypeAdapter(Imoji.class, new OAuthTokenDeserializer())
            .create();

    @NonNull
    protected final StoragePolicy storagePolicy;

    protected NetworkSession(@NonNull StoragePolicy storagePolicy) {
        this.storagePolicy = storagePolicy;
    }

    protected <T extends NetworkResponse> Future<T> validatedGet(@NonNull String path,
                                                                 @NonNull final Class<T> responseClass,
                                                                 @Nullable Map<String, String> queryStrings,
                                                                 @Nullable Map<String, String> headers) {
        return oauthValidedatedFormEncodedConnection(path, "GET", responseClass, queryStrings, headers);
    }

    protected <T extends NetworkResponse> Future<T> validatedDelete(@NonNull String path,
                                                                    @NonNull final Class<T> responseClass,
                                                                    @Nullable Map<String, String> queryStrings,
                                                                    @Nullable Map<String, String> headers) {
        return oauthValidedatedFormEncodedConnection(path, "DELETE", responseClass, queryStrings, headers);
    }

    protected <T extends NetworkResponse> Future<T> validatedPost(@NonNull String path,
                                                                  @NonNull final Class<T> responseClass,
                                                                  @Nullable Map<String, String> body,
                                                                  @Nullable Map<String, String> headers) {
        return oauthValidedatedFormEncodedConnection(path, "POST", responseClass, body, headers);
    }

    protected <T extends NetworkResponse> Future<T> validatedPut(@NonNull String path,
                                                                 @NonNull final Class<T> responseClass,
                                                                 @Nullable Map<String, String> body,
                                                                 @Nullable Map<String, String> headers) {
        return oauthValidedatedFormEncodedConnection(path, "PUT", responseClass, body, headers);
    }

    protected <T extends NetworkResponse> Future<T> GET(@NonNull String path,
                                                        @NonNull final Class<T> responseClass,
                                                        @Nullable Map<String, String> queryStrings,
                                                        @Nullable Map<String, String> headers) {
        return queryStringConnection(path, "GET", responseClass, queryStrings, headers);
    }

    protected <T extends NetworkResponse> Future<T> DELETE(@NonNull String path,
                                                           @NonNull final Class<T> responseClass,
                                                           @Nullable Map<String, String> queryString,
                                                           @Nullable Map<String, String> headers) {
        return queryStringConnection(path, "DELETE", responseClass, queryString, headers);
    }

    protected <T extends NetworkResponse> Future<T> POST(@NonNull String path,
                                                         @NonNull final Class<T> responseClass,
                                                         @Nullable Map<String, String> body,
                                                         @Nullable Map<String, String> headers) {
        return formEncodedConnection(path, "POST", responseClass, body, headers);
    }

    protected <T extends NetworkResponse> Future<T> PUT(@NonNull String path,
                                                        @NonNull final Class<T> responseClass,
                                                        @Nullable Map<String, String> body,
                                                        @Nullable Map<String, String> headers) {
        return formEncodedConnection(path, "PUT", responseClass, body, headers);
    }

    protected String oauthCredentialsHeader() {
        return "Basic " +
                Base64.encodeToString((ImojiSDK.getInstance().getClientId().toString() + ":" + ImojiSDK.getInstance().getApiToken()).getBytes(),
                        Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE
                );
    }

    private Future<OAuthTokenResponse> validateSession() {
        return HTTPExecutor.submit(new Callable<OAuthTokenResponse>() {

            @Override
            public OAuthTokenResponse call() throws Exception {
                String accessToken = NetworkSession.this.storagePolicy.getString(ImojiSDKConstants.PREFERENCES_OAUTH_EXPIRATION_KEY, null);
                String refreshToken = NetworkSession.this.storagePolicy.getString(ImojiSDKConstants.PREFERENCES_OAUTH_EXPIRATION_KEY, null);
                long expiration = NetworkSession.this.storagePolicy.getLong(ImojiSDKConstants.PREFERENCES_OAUTH_EXPIRATION_KEY, 0);
                boolean refreshTokenExpired = new Date(expiration).before(new Date());
                if (accessToken != null && refreshToken != null && !refreshTokenExpired) {
                    return new OAuthTokenResponse(accessToken, expiration, refreshToken);
                }

                Map<String, String> headers = new HashMap<>(2);
                Map<String, String> body = new HashMap<>(1);

                headers.put("Imoji-SDK-Version", ImojiSDKConstants.SERVER_SDK_VERSION);
                headers.put("Authorization", oauthCredentialsHeader());

                // refresh token expired, use the refresh_token grant type to get a new one
                if (refreshTokenExpired && refreshToken != null) {
                    body.put("grant_type", "refresh_token");
                    body.put("refresh_token", refreshToken);
                    return POST("oauth/token", OAuthTokenResponse.class, body, headers).get();
                }

                // get a new one all together
                body.put("grant_type", "client_credentials");
                return POST("oauth/token", OAuthTokenResponse.class, body, headers).get();
            }
        });
    }

    private <T extends NetworkResponse> Future<T> queryStringConnection(@NonNull final String path,
                                                                        @NonNull final String method,
                                                                        @NonNull final Class<T> responseClass,
                                                                        @Nullable final Map<String, String> queryStrings,
                                                                        @Nullable final Map<String, String> headers) {
        return HTTPExecutor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                HttpURLConnection connection = null;
                try {
                    Uri.Builder uriBuilder = ImojiSDKConstants.SERVER_URL.buildUpon().appendEncodedPath(path);

                    if (queryStrings != null) {
                        for (Map.Entry<String, String> query : queryStrings.entrySet()) {
                            uriBuilder.appendQueryParameter(query.getKey(), query.getValue());
                        }
                    }

                    URL url = new URL(uriBuilder.build().toString());
                    connection = (HttpURLConnection) url.openConnection();

                    //set headers
                    if (headers != null) {
                        for (Map.Entry<String, String> header : headers.entrySet()) {
                            connection.setRequestProperty(header.getKey(), header.getValue());
                        }
                    }

                    connection.setRequestMethod(method);
                    connection.setFixedLengthStreamingMode(0);
                    connection.connect();

                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    T result = GSON_INSTANCE.fromJson(bufferedReader, responseClass);

                    result.setStatusCode(connection.getResponseCode());

                    return result;

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }

    private <T extends NetworkResponse> Future<T> formEncodedConnection(@NonNull final String path,
                                                                        @NonNull final String method,
                                                                        @NonNull final Class<T> responseClass,
                                                                        @Nullable final Map<String, String> body,
                                                                        @Nullable final Map<String, String> headers) {
        return HTTPExecutor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                HttpURLConnection connection = null;
                try {
                    Uri.Builder uriBuilder = ImojiSDKConstants.SERVER_URL.buildUpon().appendEncodedPath(path);
                    URL url = new URL(uriBuilder.build().toString());

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    Uri.Builder bodyBuilder = new Uri.Builder();
                    if (body != null) {
                        for (Map.Entry<String, String> query : body.entrySet()) {
                            bodyBuilder.appendQueryParameter(query.getKey(), query.getValue());
                        }
                    }
                    String query = bodyBuilder.build().getEncodedQuery();
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, Xml.Encoding.UTF_8.name()));

                    writer.write(query);
                    writer.flush();
                    writer.close();

                    //set headers
                    if (headers != null) {
                        for (Map.Entry<String, String> header : headers.entrySet()) {
                            connection.setRequestProperty(header.getKey(), header.getValue());
                        }
                    }

                    os.close();

                    connection.connect();

                    BufferedReader bufferedReader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    T result = GSON_INSTANCE.fromJson(bufferedReader, responseClass);

                    result.setStatusCode(connection.getResponseCode());

                    return result;

                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }

    private <T extends NetworkResponse> Future<T> oauthValidatedQueryStringConnection(@NonNull final String path,
                                                                                      @NonNull final String method,
                                                                                      @NonNull final Class<T> responseClass,
                                                                                      @Nullable final Map<String, String> queryStrings,
                                                                                      @Nullable final Map<String, String> headers) {
        OAuthTokenResponse oAuthTokenResponse;
        try {
            oAuthTokenResponse = validateSession().get();
            Map<String, String> headersWithOauth = new HashMap<>();
            Map<String, String> queryStringsWithOauth = new HashMap<>();
            if (headers != null) {
                headersWithOauth.putAll(headers);
            }
            if (queryStrings != null) {
                queryStringsWithOauth.putAll(queryStrings);
            }

            headersWithOauth.put("Imoji-SDK-Version", ImojiSDKConstants.SERVER_SDK_VERSION);
            queryStringsWithOauth.put("access_token", oAuthTokenResponse.getAccessToken());

            return queryStringConnection(path, method, responseClass, queryStringsWithOauth, headersWithOauth);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends NetworkResponse> Future<T> oauthValidedatedFormEncodedConnection(@NonNull final String path,
                                                                                        @NonNull final String method,
                                                                                        @NonNull final Class<T> responseClass,
                                                                                        @Nullable final Map<String, String> body,
                                                                                        @Nullable final Map<String, String> headers) {
        OAuthTokenResponse oAuthTokenResponse;
        try {
            oAuthTokenResponse = validateSession().get();
            Map<String, String> headersWithOauth = new HashMap<>();
            if (headers != null) {
                headersWithOauth.putAll(headers);
            }
            headersWithOauth.put("Imoji-SDK-Version", ImojiSDKConstants.SERVER_SDK_VERSION);
            headersWithOauth.put("access_token", oAuthTokenResponse.getAccessToken());

            return formEncodedConnection(path, method, responseClass, body, headersWithOauth);

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
