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
import android.util.Log;
import android.util.Xml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imoji.sdk.ApiTask;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.Session;
import com.imoji.sdk.StoragePolicy;
import com.imoji.sdk.objects.Artist;
import com.imoji.sdk.objects.Category;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.objects.json.ArtistDeserializer;
import com.imoji.sdk.objects.json.CategoryDeserializer;
import com.imoji.sdk.objects.json.CategoryResultsDeserializer;
import com.imoji.sdk.objects.json.GenericNetworkResponsDeserializer;
import com.imoji.sdk.objects.json.ImojiDeserializer;
import com.imoji.sdk.objects.json.ImojiResultsDeserializer;
import com.imoji.sdk.objects.json.ImojiUploadResponseDeserializer;
import com.imoji.sdk.objects.json.OAuthTokenDeserializer;
import com.imoji.sdk.response.ApiResponse;
import com.imoji.sdk.response.CategoriesResponse;
import com.imoji.sdk.response.GenericNetworkResponse;
import com.imoji.sdk.response.ImojiUploadResponse;
import com.imoji.sdk.response.ImojisResponse;
import com.imoji.sdk.response.OAuthTokenResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public abstract class NetworkSession implements Session {

    private static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(Artist.class, new ArtistDeserializer())
            .registerTypeAdapter(Category.class, new CategoryDeserializer())
            .registerTypeAdapter(CategoriesResponse.class, new CategoryResultsDeserializer())
            .registerTypeAdapter(GenericNetworkResponse.class, new GenericNetworkResponsDeserializer())
            .registerTypeAdapter(Imoji.class, new ImojiDeserializer())
            .registerTypeAdapter(ImojisResponse.class, new ImojiResultsDeserializer())
            .registerTypeAdapter(OAuthTokenResponse.class, new OAuthTokenDeserializer())
            .registerTypeAdapter(ImojiUploadResponse.class, new ImojiUploadResponseDeserializer())
            .create();

    @NonNull
    protected final StoragePolicy storagePolicy;

    protected NetworkSession(@NonNull StoragePolicy storagePolicy) {
        this.storagePolicy = storagePolicy;
    }

    protected <T extends ApiResponse> ApiTask<T> validatedGet(@NonNull String path,
                                                              @NonNull final Class<T> responseClass,
                                                              @Nullable Map<String, String> queryStrings,
                                                              @Nullable Map<String, String> headers) {
        return oauthValidatedQueryStringConnection(path, "GET", responseClass, checkedPairMap(queryStrings), checkedPairMap(headers));
    }

    protected <T extends ApiResponse> ApiTask<T> validatedDelete(@NonNull String path,
                                                                 @NonNull final Class<T> responseClass,
                                                                 @Nullable Map<String, String> queryStrings,
                                                                 @Nullable Map<String, String> headers) {
        return oauthValidatedQueryStringConnection(path, "DELETE", responseClass, checkedPairMap(queryStrings), checkedPairMap(headers));
    }

    protected <T extends ApiResponse> ApiTask<T> validatedPost(@NonNull String path,
                                                               @NonNull final Class<T> responseClass,
                                                               @Nullable Map<String, String> body,
                                                               @Nullable Map<String, String> headers) {
        return oauthValidatedFormEncodedConnection(path, "POST", responseClass, checkedPairMap(body), checkedPairMap(headers));
    }

    protected <T extends ApiResponse> ApiTask<T> validatedPut(@NonNull String path,
                                                              @NonNull final Class<T> responseClass,
                                                              @Nullable Map<String, String> body,
                                                              @Nullable Map<String, String> headers) {
        return oauthValidatedFormEncodedConnection(path, "PUT", responseClass, checkedPairMap(body), checkedPairMap(headers));
    }

    protected <T extends ApiResponse> ApiTask<T> makeGetRequest(@NonNull String path,
                                                                @NonNull final Class<T> responseClass,
                                                                @Nullable Map<String, String> queryStrings,
                                                                @Nullable Map<String, String> headers) {
        return queryStringConnection(path, "GET", responseClass, checkedPairMap(queryStrings), checkedPairMap(headers));
    }

    protected <T extends ApiResponse> ApiTask<T> makeDeleteRequest(@NonNull String path,
                                                                   @NonNull final Class<T> responseClass,
                                                                   @Nullable Map<String, String> queryStrings,
                                                                   @Nullable Map<String, String> headers) {
        return queryStringConnection(path, "DELETE", responseClass, checkedPairMap(queryStrings), checkedPairMap(headers));
    }

    protected <T extends ApiResponse> ApiTask<T> makePostRequest(@NonNull String path,
                                                                 @NonNull final Class<T> responseClass,
                                                                 @Nullable Map<String, String> body,
                                                                 @Nullable Map<String, String> headers) {
        return formEncodedConnection(path, "POST", responseClass, checkedPairMap(body), checkedPairMap(headers));
    }

    protected <T extends ApiResponse> ApiTask<T> makePutRequest(@NonNull String path,
                                                                @NonNull final Class<T> responseClass,
                                                                @Nullable Map<String, String> body,
                                                                @Nullable Map<String, String> headers) {
        return formEncodedConnection(path, "PUT", responseClass, checkedPairMap(body), checkedPairMap(headers));
    }

    protected ApiTask<GenericNetworkResponse> makePutDataRequest(@NonNull Uri uri,
                                                                 @NonNull byte [] body,
                                                                 @Nullable Map<String, String> headers) {
        return dataUploadFormEncodedConnection(uri, "PUT", body, checkedPairMap(headers));
    }

    protected String oauthCredentialsHeader() {
        return "Basic " +
                Base64.encodeToString((ImojiSDK.getInstance().getClientId().toString() + ":" + ImojiSDK.getInstance().getApiToken()).getBytes(),
                        Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE
                );
    }

    private ApiTask<OAuthTokenResponse> validateSession() {
        return new ApiTask<>(new Callable<OAuthTokenResponse>() {

            @Override
            public OAuthTokenResponse call() throws Exception {
                String accessToken = NetworkSession.this.storagePolicy.getString(ImojiSDKConstants.PREFERENCES_OAUTH_ACCESS_TOKEN_KEY, null);
                String refreshToken = NetworkSession.this.storagePolicy.getString(ImojiSDKConstants.PREFERENCES_OAUTH_REFRESH_TOKEN_KEY, null);
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
                OAuthTokenResponse oAuthTokenResponse;
                if (refreshTokenExpired && refreshToken != null) {
                    body.put("grant_type", "refresh_token");
                    body.put("refresh_token", refreshToken);
                    oAuthTokenResponse = makePostRequest("oauth/token", OAuthTokenResponse.class, body, headers).executeImmediately();
                } else {
                    // get a new one all together
                    body.put("grant_type", "client_credentials");
                    oAuthTokenResponse = makePostRequest("oauth/token", OAuthTokenResponse.class, body, headers).executeImmediately();
                }

                if (oAuthTokenResponse != null) {
                    NetworkSession.this.storagePolicy.putString(ImojiSDKConstants.PREFERENCES_OAUTH_ACCESS_TOKEN_KEY, oAuthTokenResponse.getAccessToken());
                    NetworkSession.this.storagePolicy.putString(ImojiSDKConstants.PREFERENCES_OAUTH_REFRESH_TOKEN_KEY, oAuthTokenResponse.getRefreshToken());
                    NetworkSession.this.storagePolicy.putLong(ImojiSDKConstants.PREFERENCES_OAUTH_EXPIRATION_KEY, oAuthTokenResponse.getExpiration());
                }

                return oAuthTokenResponse;
            }
        });
    }

    private <T extends ApiResponse> ApiTask<T> queryStringConnection(@NonNull final String path,
                                                                     @NonNull final String method,
                                                                     @NonNull final Class<T> responseClass,
                                                                     @NonNull final Map<String, String> queryStrings,
                                                                     @NonNull final Map<String, String> headers) {
        return new ApiTask<>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                HttpURLConnection connection = null;
                try {
                    Uri.Builder uriBuilder = ImojiSDKConstants.SERVER_URL.buildUpon().appendEncodedPath(path);

                    for (Map.Entry<String, String> query : queryStrings.entrySet()) {
                        uriBuilder.appendQueryParameter(query.getKey(), query.getValue());
                    }

                    URL url = new URL(uriBuilder.build().toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);

                    //set headers
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        connection.setRequestProperty(header.getKey(), header.getValue());
                    }

                    connection.connect();

                    return readJsonResponse(connection, responseClass);

                } catch (Throwable t) {
                    Log.e(NetworkSession.class.getName(), "Unable to perform network request", t);
                    throw t;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }

    private <T extends ApiResponse> ApiTask<T> formEncodedConnection(@NonNull final String path,
                                                                     @NonNull final String method,
                                                                     @NonNull final Class<T> responseClass,
                                                                     @NonNull final Map<String, String> body,
                                                                     @NonNull final Map<String, String> headers) {
        return new ApiTask<>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    Uri.Builder uriBuilder = ImojiSDKConstants.SERVER_URL.buildUpon().appendEncodedPath(path);
                    URL url = new URL(uriBuilder.build().toString());

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    //set headers
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        connection.setRequestProperty(header.getKey(), header.getValue());
                    }

                    Uri.Builder bodyBuilder = new Uri.Builder();
                    for (Map.Entry<String, String> query : body.entrySet()) {
                        bodyBuilder.appendQueryParameter(query.getKey(), query.getValue());
                    }
                    String query = bodyBuilder.build().getEncodedQuery();
                    outputStream = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(outputStream, Xml.Encoding.UTF_8.name())
                    );

                    writer.write(query);
                    writer.flush();
                    writer.close();

                    connection.connect();

                    return readJsonResponse(connection, responseClass);

                } catch (Throwable t) {
                    Log.e(NetworkSession.class.getName(), "Unable to perform network request", t);
                    throw t;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }

                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            }
        });
    }

    private ApiTask<GenericNetworkResponse> dataUploadFormEncodedConnection(@NonNull final Uri uri,
                                                                            @NonNull final String method,
                                                                            @NonNull final byte[] body,
                                                                            @NonNull final Map<String, String> headers) {
        return new ApiTask<>(new Callable<GenericNetworkResponse>() {
            @Override
            public GenericNetworkResponse call() throws Exception {
                HttpURLConnection connection = null;
                OutputStream outputStream = null;
                try {
                    URL url = new URL(uri.toString());

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod(method);

                    //set headers
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        connection.setRequestProperty(header.getKey(), header.getValue());
                    }

                    outputStream = connection.getOutputStream();
                    outputStream.write(body, 0, body.length);
                    outputStream.flush();
                    outputStream.close();

                    return new GenericNetworkResponse();

                } catch (Throwable t) {
                    Log.e(NetworkSession.class.getName(), "Unable to perform network request", t);
                    throw t;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }

    private <T extends ApiResponse> ApiTask<T> oauthValidatedQueryStringConnection(@NonNull final String path,
                                                                                   @NonNull final String method,
                                                                                   @NonNull final Class<T> responseClass,
                                                                                   @NonNull final Map<String, String> queryStrings,
                                                                                   @NonNull final Map<String, String> headers) {
        return new ApiTask<>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                OAuthTokenResponse oAuthTokenResponse;
                try {
                    oAuthTokenResponse = validateSession().executeImmediately();
                    Map<String, String> headersWithOauth = new HashMap<>(headers);
                    Map<String, String> queryStringsWithOauth = new HashMap<>(queryStrings);

                    headersWithOauth.put("Imoji-SDK-Version", ImojiSDKConstants.SERVER_SDK_VERSION);
                    headersWithOauth.put("User-Locale", Locale.getDefault().toString());
                    queryStringsWithOauth.put("access_token", oAuthTokenResponse.getAccessToken());

                    return queryStringConnection(path, method, responseClass, queryStringsWithOauth, headersWithOauth).executeImmediately();

                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private <T extends ApiResponse> ApiTask<T> oauthValidatedFormEncodedConnection(@NonNull final String path,
                                                                                   @NonNull final String method,
                                                                                   @NonNull final Class<T> responseClass,
                                                                                   @NonNull final Map<String, String> body,
                                                                                   @NonNull final Map<String, String> headers) {
        return new ApiTask<>(new Callable<T>() {
            @Override
            public T call() throws Exception {
                OAuthTokenResponse oAuthTokenResponse;
                try {
                    oAuthTokenResponse = validateSession().executeImmediately();
                    Map<String, String> headersWithOauth = new HashMap<>(headers);
                    Map<String, String> bodyWithOauth = new HashMap<>(body);

                    headersWithOauth.put("Imoji-SDK-Version", ImojiSDKConstants.SERVER_SDK_VERSION);
                    headersWithOauth.put("User-Locale", Locale.getDefault().toString());
                    bodyWithOauth.put("access_token", oAuthTokenResponse.getAccessToken());

                    return formEncodedConnection(path, method, responseClass, bodyWithOauth, headersWithOauth).executeImmediately();

                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private <T extends ApiResponse> T readJsonResponse(@NonNull HttpURLConnection connection,
                                                       @NonNull Class<T> responseClass) throws IOException {

        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringWriter stringWriter = new StringWriter();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringWriter.append(line);
        }

        return GSON_INSTANCE.fromJson(stringWriter.toString(), responseClass);
    }

    @NonNull
    private static Map<String, String> checkedPairMap(@Nullable Map<String, String> map) {
        return map != null ? map : Collections.<String, String>emptyMap();
    }
}