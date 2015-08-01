package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.Until;
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

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by sajjadtabib on 4/6/15.
 */
class ImojiNetApiHandle extends ImojiNetworkingInterface {
    private static ImojiApiInterface sApiService;
    private Context mContext;

    public ImojiNetApiHandle(Context mContext) {
        this.mContext = mContext;
    }

    private static ImojiApiInterface get(final Context context) {
        if (sApiService == null) {

            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {

                    request.addHeader("imoji-sdk-version", "2.0.0");
                    request.addHeader("x-client-model", "android");
                    request.addHeader("x-client-os-version", Build.VERSION.RELEASE);
                }
            };

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(Config.BASE_URL)
                    .setRequestInterceptor(requestInterceptor);

            sApiService = builder.build().create(ImojiApiInterface.class);
        }

        return sApiService;
    }


    @Override
    void getFeaturedImojis(int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> callback) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }

        ImojiNetApiHandle.get(mContext).getFeaturedImojis(apiToken, offset, count, new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));
    }


    @Override
    void searchImojis(String query, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> callback) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }
        ImojiNetApiHandle.get(mContext).searchImojis(apiToken, query, offset, count, new CallbackWrapper<ImojiSearchResponse, List<Imoji>>(callback));
    }

    @Override
    void getImojiCategories(final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get(mContext).getImojiCategories(apiToken, ImojiCategory.Classification.NONE, new CallbackWrapper<GetCategoryResponse, List<ImojiCategory>>(cb));
    }

    @Override
    void getImojiCategories(String classification, final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get(mContext).getImojiCategories(apiToken, classification, new CallbackWrapper<GetCategoryResponse, List<ImojiCategory>>(cb));
    }

    @Override
    void getUserImojis(com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get(mContext).getUserImojis(apiToken, new CallbackWrapper<GetUserImojiResponse, List<Imoji>>(cb));
    }

    @Override
    void getImojisById(List<String> ids, com.imojiapp.imoji.sdk.Callback<List<Imoji>, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get(mContext).fetchImojis(apiToken, TextUtils.join(",", ids), new CallbackWrapper<FetchImojisResponse, List<Imoji>>(cb));
    }

    @Override
    void addImojiToUserCollection(String imojiId, com.imojiapp.imoji.sdk.Callback<String, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get(mContext).addImojiToUserCollection(apiToken, imojiId, new CallbackWrapper<AddImojiToCollectionResponse, String>(cb) {
            @Override
            public void success(AddImojiToCollectionResponse result, Response response) {
                super.success(result, response);
                if (result.isSuccess()) {
                    //broadcast a sync intent
                    Intent intent = new Intent();
                    intent.setAction(ExternalIntents.Actions.INTENT_REQUEST_SYNC);
                    intent.addCategory(ExternalIntents.Categories.EXTERNAL_CATEGORY);
                    mContext.sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    GetAuthTokenResponse getAuthToken(String clientId, String clientSecret, String refreshToken) {
        String grantType = Constants.CLIENT_CREDENTIALS;
        if (refreshToken != null) {
            grantType = Constants.REFRESH_TOKEN;
        }
        try {
            return ImojiNetApiHandle.get(mContext).getAuthToken("Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(), Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE), grantType, refreshToken);
        } catch (RetrofitError error) {
            error.printStackTrace();
        }
        return null;
    }

    @Override
    void requestExternalOauth(String clientId, com.imojiapp.imoji.sdk.Callback<ExternalOauthPayloadResponse, String> cb) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        ImojiNetApiHandle.get(mContext).requestExternalOauth(apiToken, clientId, new CallbackWrapper<ExternalOauthPayloadResponse, ExternalOauthPayloadResponse>(cb));
    }

    /* Synchronous Methods */
    List<Imoji> getFeaturedImojis(int offset, int numResults) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }
        try {
            ImojiSearchResponse response = ImojiNetApiHandle.get(mContext).getFeaturedImojis(apiToken, offset, count);
            if (response != null && response.isSuccess()) {
                return response.results;
            }
        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }

    List<Imoji> searchImojis(String query, int offset, int numResults) {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        String count = null;
        if (numResults > 0) {
            count = String.valueOf(numResults);
        }

        try {
            ImojiSearchResponse response = ImojiNetApiHandle.get(mContext).searchImojis(apiToken, query, offset, count);
            if (response != null && response.isSuccess()) {
                return response.results;
            }

        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }

    List<ImojiCategory> getImojiCategories() {
        String apiToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        try {
            GetCategoryResponse response = ImojiNetApiHandle.get(mContext).getImojiCategories(apiToken);
            if (response != null && response.isSuccess()) {
                return response.categories;
            }

        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }

    private static class CallbackWrapper<T extends BasicResponse<V>, V> implements retrofit.Callback<T> {

        private Callback<V, String> mCallback;

        public CallbackWrapper(Callback callback) {
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
            try {
                if (error.getBody() != null) {
                    String json = new String(((TypedByteArray) error.getResponse().getBody()).getBytes());
                    Type type = new TypeToken<ErrorResponse>() {
                    }.getType();
                    ErrorResponse response = Utils.gson().fromJson(json, type);

                    mCallback.onFailure(response.getPayload());
                } else {
                    mCallback.onFailure(Status.NETWORK_ERROR);
                }
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

        }

    }

}
