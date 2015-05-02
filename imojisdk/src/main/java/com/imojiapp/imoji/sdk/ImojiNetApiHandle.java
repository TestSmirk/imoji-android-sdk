package com.imojiapp.imoji.sdk;

import android.os.Build;
import android.util.Base64;

import com.imojiapp.imoji.sdk.networking.responses.BasicResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetUserImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
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

        ImojiNetApiHandle.get().getFeaturedImojis(apiToken, offset, count, new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));
    }


    static void searchImojis(String apiToken, String query, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>> callback) {

        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }
        ImojiNetApiHandle.get().searchImojis(apiToken, query, offset, count, new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));
    }

    static void getImojiCategories(String apiToken, final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>> cb) {
        ImojiNetApiHandle.get().getImojiCategories(apiToken, new CallbackWrapper<GetCategoryResponse, List<ImojiCategory>>(cb));
    }

    static void getUserImojis(String apiToken, com.imojiapp.imoji.sdk.Callback<List<Imoji>> cb) {
        ImojiNetApiHandle.get().getUserImojis(apiToken, new CallbackWrapper<GetUserImojiResponse, List<Imoji>>(cb));
    }

    static GetAuthTokenResponse getAuthToken(String clientId, String clientSecret, String refreshToken) {
        String grantType = "client_credentials";
        if (refreshToken != null) {
            grantType = "refresh_token";
        }
        try {
            return ImojiNetApiHandle.get().getAuthToken("Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(), Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE), grantType, refreshToken);
        } catch (RetrofitError error) {
            error.printStackTrace();
        }
        return null;
    }

    static void requestExternalOauth(String apiToken, String clientId, com.imojiapp.imoji.sdk.Callback<ExternalOauthPayloadResponse> cb) {
        ImojiNetApiHandle.get().requestExternalOauth(apiToken, clientId, new CallbackWrapper<ExternalOauthPayloadResponse, ExternalOauthPayloadResponse>(cb));
    }

    /* Synchronous Methods */
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

    static class CallbackWrapper<T extends BasicResponse<V>, V> implements Callback<T> {

        private com.imojiapp.imoji.sdk.Callback<V> mCallback;

        public CallbackWrapper(com.imojiapp.imoji.sdk.Callback callback) {
            mCallback = callback;
        }

        @Override
        public void success(T result, Response response) {
            if (result.isSuccess()) {
                mCallback.onSuccess(result.getPayload());
            } else {
                mCallback.onFailure();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
            mCallback.onFailure();
        }
    }
}
