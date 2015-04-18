package com.imojiapp.imoji.sdk;

import android.content.res.Resources;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by sajjadtabib on 4/6/15.
 */
class ImojiNetApiHandle {
    private static final String LOG_TAG = ImojiNetApiHandle.class.getSimpleName();
    private static ImojiApiInterface sApiService;

    private static ImojiApiInterface get() {
        if (sApiService == null) {

            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("x-client-version", "2.0.0"); //TODO: Fix
                    request.addHeader("x-client-model", "android");
                    request.addHeader("x-client-os-version", Build.VERSION.RELEASE);
                }
            };


            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(Config.BASE_URL)
                    .setRequestInterceptor(requestInterceptor);

//            Log.w(LOG_TAG, "PROXY ENABLED");
//            SocketAddress addr = new InetSocketAddress("10.0.1.6", 8888);
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
//            builder.setClient(new OkClient(new OkHttpClient().setProxy(proxy)));
            sApiService = builder.build().create(ImojiApiInterface.class);
        }


        return sApiService;
    }


    static void getFeaturedImojis(String apiToken, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>> callback) {
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }

        Log.d(LOG_TAG, "api token is: " + apiToken);

        ImojiNetApiHandle.get().getFeaturedImojis(apiToken, offset, count, new Callback<ImojiSearchResponse>() {
            @Override
            public void success(ImojiSearchResponse imojiSearchResponse, Response response) {
                if (imojiSearchResponse.isSuccess()) {
                    callback.onSuccess(imojiSearchResponse.results);
                } else {
                    Log.d(LOG_TAG, "failure: " + imojiSearchResponse.status);
                    callback.onFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                callback.onFailure();
            }
        });
    }

    static List<Imoji> getFeaturedImojis(String apiToken, int offset, int numResults) {
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }
        try {
            ImojiSearchResponse response = ImojiNetApiHandle.get().getFeaturedImojis(apiToken, offset, count);
            if (response != null && response.isSuccess()) {
                return response.results;
            }
        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }


    static void searchImojis(String apiToken, String query, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>> callback) {

        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }
        ImojiNetApiHandle.get().searchImojis(apiToken, query, offset, count, new Callback<ImojiSearchResponse>() {
            @Override
            public void success(final ImojiSearchResponse imojiSearchResponse, Response response) {
                if (imojiSearchResponse.isSuccess()) {
                    callback.onSuccess(imojiSearchResponse.results);
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                callback.onFailure();
            }
        });
    }

    static List<Imoji> searchImojis(String apiToken, String query, int offset, int numResults) {
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }

        try {
            ImojiSearchResponse response = ImojiNetApiHandle.get().searchImojis(apiToken, query, offset, count);
            if (response != null && response.isSuccess()) {
                return response.results;
            }

        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }

    static List<ImojiCategory> getImojiCategories(String apiToken) {
        try {
            GetCategoryResponse response = ImojiNetApiHandle.get().getImojiCategories(apiToken);
            if (response != null && response.isSuccess()) {
                return response.categories;
            }

        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }

    static void getImojiCategories(String apiToken, final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>> cb) {
        ImojiNetApiHandle.get().getImojiCategories(apiToken, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {

                if (getCategoryResponse.isSuccess()) {
                    cb.onSuccess(getCategoryResponse.categories);
                } else {
                    cb.onFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                cb.onFailure();
            }
        });
    }

    static GetAuthTokenResponse getAuthToken(String clientId, String clientSecret, String refreshToken) {
        try {
            return ImojiNetApiHandle.get().getAuthToken("Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(),  Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE), "client_credentials", refreshToken);
        } catch (RetrofitError error) {
            error.printStackTrace();
        }
        return null;
    }



}
