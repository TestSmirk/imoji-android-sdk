package com.imojiapp.imoji.sdk;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sajjadtabib on 10/2/15.
 */
class SimpleHttpClient {

    private static final String LOG_TAG = SimpleHttpClient.class.getSimpleName();
    public static final String NETWORK_ERROR = "NETWORK_ERROR";
    private static final int MAX_POOL_SIZE = 3;
    private static final int EXECUTOR_SHUTDOWN_TIMEOUT = 30000; //shutdown executor after 30s
    private ExecutorService mExecutorService;
    private final Handler mHandler = new Handler();


    public SimpleHttpClient() {
        createExecutorService();
    }

    private void createExecutorService() {
        mExecutorService = Executors.newFixedThreadPool(MAX_POOL_SIZE);
    }

    public void post(String endpoint, Map<String, String> params, Map<String, String> headers, SimpleHttpClientCallback cb) {
        mHandler.removeCallbacks(mShutdownRunnable);
        if (mExecutorService.isShutdown()) {
            createExecutorService();
        }
        mHandler.postDelayed(mShutdownRunnable, EXECUTOR_SHUTDOWN_TIMEOUT); //attempt to shutdown in 30s
        mExecutorService.execute(new PostTask(endpoint, params, headers, cb));

    }

    public String post(String endpoint, Map<String, String> params, Map<String, String> headers) {
        mHandler.removeCallbacks(mShutdownRunnable);
        mHandler.postDelayed(mShutdownRunnable, EXECUTOR_SHUTDOWN_TIMEOUT); //attempt to shutdown in 30s
        return new PostTask(endpoint, params, headers).execute();
    }

    public void get(String endpoint, Map<String, String> params, Map<String, String> headers, SimpleHttpClientCallback cb) {
        mHandler.removeCallbacks(mShutdownRunnable);
        if (mExecutorService.isShutdown()) {
            createExecutorService();
        }
        mHandler.postDelayed(mShutdownRunnable, EXECUTOR_SHUTDOWN_TIMEOUT); //attempt to shutdown in 30s
        mExecutorService.execute(new GetTask(endpoint, params, headers, cb));
    }

    public String get(String endpoint, Map<String, String> params, Map<String, String> headers) {
        mHandler.removeCallbacks(mShutdownRunnable);
        mHandler.postDelayed(mShutdownRunnable, EXECUTOR_SHUTDOWN_TIMEOUT); //attempt to shutdown in 30s
        return new GetTask(endpoint, params, headers, null).execute();
    }

    private Runnable mShutdownRunnable = new Runnable() {
        @Override
        public void run() {
            mExecutorService.shutdown();
        }
    };


    public interface SimpleHttpClientCallback {

        void onSuccess(String response);

        void onFailure(String failure);

    }

    private static abstract class NetworkTask implements Runnable {

        protected String mEndpoint;
        protected Map<String, String> mParams;
        protected Map<String, String> mHeaders;
        protected SimpleHttpClientCallback mSimpleHttpClientCallback;

        public NetworkTask(String endpoint, Map<String, String> params, Map<String, String> headers, SimpleHttpClientCallback simpleHttpClientCallback) {
            mEndpoint = endpoint;
            mParams = params;
            mHeaders = headers;
            mSimpleHttpClientCallback = simpleHttpClientCallback;
        }

        abstract String execute();

        protected String readResponse(HttpURLConnection connection) {
            if (connection == null) {
                if (mSimpleHttpClientCallback != null) {
                    mSimpleHttpClientCallback.onFailure(NETWORK_ERROR);
                }
                return null;
            }

            String response = null;

            try {

                int statusCode = connection.getResponseCode();
                if (statusCode < 200 || statusCode >= 300) {
                    if (mSimpleHttpClientCallback != null) {
                        mSimpleHttpClientCallback.onFailure(NETWORK_ERROR);
                    }

                    return null;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();

                response = sb.toString();
                if (mSimpleHttpClientCallback != null) {
                    mSimpleHttpClientCallback.onSuccess(response);
                }

            } catch (IOException e) {
                e.printStackTrace();
                if (mSimpleHttpClientCallback != null) {
                    mSimpleHttpClientCallback.onFailure(NETWORK_ERROR);
                }
            }finally {
                connection.disconnect();
            }

            return response;
        }
    }


    private static class GetTask extends NetworkTask {


        public GetTask(String endpoint, Map<String, String> params, Map<String, String> headers, SimpleHttpClientCallback simpleHttpClientCallback) {
            super(endpoint, params, headers, simpleHttpClientCallback);
        }

        public GetTask(String endpoint, Map<String, String> params, Map<String, String> headers) {
            super(endpoint, params, headers, null);
        }

        @Override
        public void run() {
            execute();
        }

        @Override
        String execute() {
            HttpURLConnection connection = HttpUtils.get(mEndpoint, mParams, mHeaders);
            return readResponse(connection);
        }
    }

    private static class PostTask extends NetworkTask {

        public PostTask(String endpoint, Map<String, String> params, Map<String, String> headers, SimpleHttpClientCallback simpleHttpClientCallback) {
            super(endpoint, params, headers, simpleHttpClientCallback);
        }

        public PostTask(String endpoint, Map<String, String> params, Map<String, String> headers) {
            super(endpoint, params, headers, null);
        }

        @Override
        public void run() {
            execute();
        }

        @Override
        String execute() {
            HttpURLConnection connection = HttpUtils.post(mEndpoint, mParams, mHeaders);
            return readResponse(connection);
        }
    }
}
