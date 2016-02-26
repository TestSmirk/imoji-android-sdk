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

import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.Session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class NetworkSession implements Session {

    public class NetworkResponse implements Serializable {

        private final int statusCode;

        private final String response;

        public boolean succeeded() {
            return statusCode >= 200 && statusCode < 300;
        }

        public NetworkResponse(int statusCode, String response) {
            this.statusCode = statusCode;
            this.response = response;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getResponse() {
            return response;
        }
    }

    public class NetworkException extends Throwable {

        public NetworkException(String detailMessage) {
            super(detailMessage);
        }

        public NetworkException(String detailMessage, Throwable cause) {
            super(detailMessage, cause);
        }
    }

    private static final ExecutorService HTTPExecutor = Executors.newSingleThreadExecutor();

    public Future<NetworkResponse> GET(@NonNull String path,
                                       @Nullable Map<String, String> queryString,
                                       @Nullable Map<String, String> headers) throws NetworkException {
        try {
            return connect(queryStringConnection(path, "GET", queryString, headers));
        } catch (IOException e) {
            Log.e(NetworkSession.class.getCanonicalName(), "Unable to create network request", e);
            throw new NetworkException("Unable to create network request", e);
        }
    }

    public Future<NetworkResponse> DELETE(@NonNull String path,
                                          @Nullable Map<String, String> queryString,
                                          @Nullable Map<String, String> headers) throws NetworkException {
        try {
            return connect(queryStringConnection(path, "DELETE", queryString, headers));
        } catch (IOException e) {
            Log.e(NetworkSession.class.getCanonicalName(), "Unable to create network request", e);
            throw new NetworkException("Unable to create network request", e);
        }
    }

    public Future<NetworkResponse> POST(@NonNull String path,
                                        @Nullable Map<String, String> body,
                                        @Nullable Map<String, String> headers) throws NetworkException {
        try {
            return connect(queryStringConnection(path, "POST", body, headers));
        } catch (IOException e) {
            Log.e(NetworkSession.class.getCanonicalName(), "Unable to create network request", e);
            throw new NetworkException("Unable to create network request", e);
        }
    }

    public Future<NetworkResponse> PUT(@NonNull String path,
                                       @Nullable Map<String, String> body,
                                       @Nullable Map<String, String> headers) throws NetworkException {
        try {
            return connect(formEncodedStringConnection(path, "PUT", body, headers));
        } catch (IOException e) {
            Log.e(NetworkSession.class.getCanonicalName(), "Unable to create network request", e);
            throw new NetworkException("Unable to create network request", e);
        }
    }

    @NonNull
    protected Future<NetworkResponse> connect(@NonNull final HttpURLConnection connection) {
        return HTTPExecutor.submit(new Callable<NetworkResponse>() {
            @Override
            public NetworkResponse call() throws Exception {
                try {

                    int statusCode = connection.getResponseCode();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringWriter writer = new StringWriter();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.append(line);
                    }

                    reader.close();

                    return new NetworkResponse(statusCode, writer.toString());

                } finally {
                    connection.disconnect();
                }
            }
        });
    }

    protected String oauthCredentialsHeader() {
        return "Basic " +
                Base64.encodeToString((ImojiSDK.getInstance().getClientId().toString() + ":" + ImojiSDK.getInstance().getApiToken()).getBytes(),
                        Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE
                );
    }

    private HttpURLConnection queryStringConnection(@NonNull String path,
                                                    @NonNull String method,
                                                    @Nullable Map<String, String> queryStrings,
                                                    @Nullable Map<String, String> headers) throws IOException {
        Uri.Builder uriBuilder = ImojiSDKConstants.SERVER_URL.buildUpon().appendEncodedPath(path);

        if (queryStrings != null) {
            for (Map.Entry<String, String> query : queryStrings.entrySet()) {
                uriBuilder.appendQueryParameter(query.getKey(), query.getValue());
            }
        }

        URL url = new URL(uriBuilder.build().toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //set headers
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                connection.setRequestProperty(header.getKey(), header.getValue());
            }
        }

        connection.setRequestMethod(method);
        connection.setFixedLengthStreamingMode(0);
        connection.connect();

        return connection;
    }

    private HttpURLConnection formEncodedStringConnection(@NonNull String path,
                                                          @NonNull String method,
                                                          @Nullable Map<String, String> body,
                                                          @Nullable Map<String, String> headers) throws IOException {
        Uri.Builder uriBuilder = ImojiSDKConstants.SERVER_URL.buildUpon().appendEncodedPath(path);
        URL url = new URL(uriBuilder.build().toString());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

        return connection;
    }
}
