package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.imojiapp.imoji.sdk.networking.responses.AddImojiToCollectionResponse;
import com.imojiapp.imoji.sdk.networking.responses.BasicResponse;
import com.imojiapp.imoji.sdk.networking.responses.ErrorResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetUserImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by sajjadtabib on 4/6/15.
 */
class ImojiNetApiHandle {
    private static final String LOG_TAG = ImojiNetApiHandle.class.getSimpleName();
    private static ImojiApiInterface sApiService;
    private static Context sContext;

    static void init(Context context) {
        sContext = context;
    }

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


    static void getFeaturedImojis(int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> callback) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }

        ImojiNetApiHandle.get().getFeaturedImojis(apiToken, offset, count, new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));
    }


    static void searchImojis(String query, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> callback) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }
        ImojiNetApiHandle.get().searchImojis(apiToken, query, offset, count, new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));
    }

    static void getImojiCategories(final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get().getImojiCategories(apiToken, ImojiCategory.Classification.NONE, new CallbackWrapper<GetCategoryResponse, List<ImojiCategory>>(cb));
    }

    static void getImojiCategories(String classification, final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get().getImojiCategories(apiToken, classification, new CallbackWrapper<GetCategoryResponse, List<ImojiCategory>>(cb));
    }

    static void getUserImojis(com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get().getUserImojis(apiToken, new CallbackWrapper<GetUserImojiResponse, List<Imoji>>(cb));
    }

    static void getImojisById(List<String> ids, com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get().fetchImojis(apiToken, TextUtils.join(",", ids), new CallbackWrapper<FetchImojisResponse, List<Imoji>>(cb));
    }

    static void addImojiToUserCollection(String imojiId, com.imojiapp.imoji.sdk.Callback<String, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get().addImojiToUserCollection(apiToken, imojiId, new CallbackWrapper<AddImojiToCollectionResponse, String>(cb){
            @Override
            public void success(AddImojiToCollectionResponse result, Response response) {
                super.success(result, response);
                if (result.isSuccess()) {
                    //broadcast a sync intent
                    Intent intent = new Intent();
                    intent.setAction(ExternalIntents.Actions.INTENT_REQUEST_SYNC);
                    intent.addCategory(ExternalIntents.Categories.EXTERNAL_CATEGORY);
                    sContext.sendBroadcast(intent);
                }
            }
        });
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

    static void requestExternalOauth(String clientId, com.imojiapp.imoji.sdk.Callback<ExternalOauthPayloadResponse, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get().requestExternalOauth(apiToken, clientId, new CallbackWrapper<ExternalOauthPayloadResponse, ExternalOauthPayloadResponse>(cb));
    }

    /* Synchronous Methods */
    static List<Imoji> getFeaturedImojis(int offset, int numResults) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
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

    static List<Imoji> searchImojis(String query, int offset, int numResults) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
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

    static List<ImojiCategory> getImojiCategories() {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
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

        private com.imojiapp.imoji.sdk.Callback<V, String> mCallback;

        public CallbackWrapper(com.imojiapp.imoji.sdk.Callback callback) {
            mCallback = callback;
        }

        @Override
        public void success(T result, Response response) {
            if (result.isSuccess()) {
                mCallback.onSuccess(result.getPayload());
            } else {
                mCallback.onFailure(result.status);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
            if (error.getBody() != null) {
                try {
                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    Type type = new TypeToken<ErrorResponse>() {
                    }.getType();
                    ErrorResponse response = Utils.gson().fromJson(json, type);
                    mCallback.onFailure(response.getPayload());
                } catch (JsonParseException e) {
                    e.printStackTrace();
                    mCallback.onFailure(Status.NETWORK_ERROR);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    mCallback.onFailure(Status.NETWORK_ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallback.onFailure(Status.NETWORK_ERROR);
                }

            } else {
                mCallback.onFailure(Status.NETWORK_ERROR);
            }
        }
    }

}
