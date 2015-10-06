package com.imojiapp.imoji.sdk;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.imojiapp.imoji.sdk.networking.responses.AddImojiToCollectionResponse;
import com.imojiapp.imoji.sdk.networking.responses.BasicResponse;
import com.imojiapp.imoji.sdk.networking.responses.CreateImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetUserImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiAckResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;
import com.imojiapp.imoji.sdk.networking.responses.ReportAbusiveResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sajjadtabib on 10/2/15.
 */
public class ImojiNetworkingClientImpl extends ImojiNetworkingInterface {

    private static Gson sGson = new Gson();
    private final Handler mHandler = new Handler();
    private SimpleHttpClient mHttpClient;

    public ImojiNetworkingClientImpl() {
        mHttpClient = new SimpleHttpClient();
    }

    @Override
    void getFeaturedImojis(int offset, int numResults, final Callback<List<Imoji>, String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.OFFSET, String.valueOf(offset));
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.NUMRESULTS, String.valueOf(numResults));

        mHttpClient.get(Api.Endpoints.IMOJI_FEATURED_FETCH, params, getDefaultHeaders(), new ApiHttpClientCallback<>(callback, mHandler, FetchImojisResponse.class));
    }

    @Override
    void searchImojis(String query, int offset, int numResults, Callback<List<Imoji>, String> callback) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.QUERY, query);
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.OFFSET, String.valueOf(offset));
        params.put(Api.Params.NUMRESULTS, String.valueOf(numResults));

        mHttpClient.get(Api.Endpoints.IMOJI_SEARCH, params, getDefaultHeaders(), new ApiHttpClientCallback<>(callback, mHandler, ImojiSearchResponse.class));
    }

    @Override
    void searchImojis(Map<String, String> params, Callback<List<Imoji>, String> callback) {
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        mHttpClient.get(Api.Endpoints.IMOJI_SEARCH, params, getDefaultHeaders(), new ApiHttpClientCallback<>(callback, mHandler, ImojiSearchResponse.class));
    }

    @Override
    void getImojiCategories(Callback<List<ImojiCategory>, String> cb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        mHttpClient.get(Api.Endpoints.IMOJI_CATEGORIES_FETCH, params, getDefaultHeaders(), new ApiHttpClientCallback<>(cb, mHandler, GetCategoryResponse.class));
    }

    @Override
    void getImojiCategories(String classification, Callback<List<ImojiCategory>, String> cb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.CLASSIFICATION, classification);

        mHttpClient.get(Api.Endpoints.IMOJI_CATEGORIES_FETCH, params, getDefaultHeaders(), new ApiHttpClientCallback<>(cb, mHandler, GetCategoryResponse.class));
    }

    @Override
    void getUserImojis(Callback<List<Imoji>, String> cb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());

        mHttpClient.get(Api.Endpoints.USER_IMOJI_FETCH, params, getDefaultHeaders(), new ApiHttpClientCallback<>(cb, mHandler, GetUserImojiResponse.class));
    }

    @Override
    void getImojisById(List<String> ids, Callback<List<Imoji>, String> cb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.IDS, TextUtils.join(",", ids));

        mHttpClient.post(Api.Endpoints.IMOJI_FETCHMULTIPLE, params, getDefaultHeaders(), new ApiHttpClientCallback<>(cb, mHandler, FetchImojisResponse.class));

    }

    @Override
    FetchImojisResponse getImojisById(List<String> ids) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.IDS, TextUtils.join(",", ids));

        String jsonResponse = mHttpClient.post(Api.Endpoints.IMOJI_FETCHMULTIPLE, params, getDefaultHeaders());
        if (jsonResponse == null) {
            return null;
        }

        try {
            return sGson.fromJson(jsonResponse, FetchImojisResponse.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    void addImojiToUserCollection(String imojiId, Callback<String, String> cb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.IMOJIID, imojiId);

        mHttpClient.post(Api.Endpoints.USER_IMOJI_COLLECTION_ADD, params, getDefaultHeaders(), new ApiHttpClientCallback<>(cb, mHandler, AddImojiToCollectionResponse.class));
    }

    @Override
    GetAuthTokenResponse getAuthToken(String clientId, String clientSecret, String refreshToken) {
        String grantType = Constants.CLIENT_CREDENTIALS;
        if (refreshToken != null) {
            grantType = Constants.REFRESH_TOKEN;
        }

        Map<String, String> headers = getDefaultHeaders();
        headers.put(Api.Headers.AUTHORIZATION, "Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(), Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE));

        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.GRANT_TYPE, grantType);
        params.put(Api.Params.REFRESH_TOKEN, refreshToken);

        String jsonResponse = mHttpClient.post(Api.Endpoints.OAUTH_TOKEN, params, headers);
        if (jsonResponse == null) {
            return null;
        }

        try {
            return sGson.fromJson(jsonResponse, GetAuthTokenResponse.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    void requestExternalOauth(String clientId, Callback<ExternalOauthPayloadResponse, String> cb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.CLIENTID, clientId);

        mHttpClient.post(Api.Endpoints.OAUTH_EXTERNAL_GETIDPAYLOAD, params, getDefaultHeaders(), new ApiHttpClientCallback<>(cb, mHandler, ExternalOauthPayloadResponse.class));

    }

    @Override
    CreateImojiResponse createImoji(List<String> tags) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        if (tags != null && !tags.isEmpty()) {
            params.put(Api.Params.TAGS, TextUtils.join(",", tags));
        }

        String jsonResponse = mHttpClient.post(Api.Endpoints.IMOJI_CREATE, params, getDefaultHeaders());
        if (jsonResponse == null) {
            return null;
        }

        try {
            return sGson.fromJson(jsonResponse, CreateImojiResponse.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    ImojiAckResponse ackImoji(String imojiId, boolean hasFull, boolean hasThumb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.IMOJIID, imojiId);
        params.put(Api.Params.HAS_FULL_IMAGE, hasFull ? "1" : "0");
        params.put(Api.Params.HAS_THUMB_IMAGE, hasThumb ? "1" : "0");

        String jsonResponse = mHttpClient.post(Api.Endpoints.IMOJI_ACK, params, getDefaultHeaders());
        if (jsonResponse == null) {
            return null;
        }

        try {
            return sGson.fromJson(jsonResponse, ImojiAckResponse.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    void reportAbusiveImoji(String imojiId, Callback<String, String> cb) {
        Map<String, String> params = new HashMap<>();
        params.put(Api.Params.ACCESS_TOKEN, getApiToken());
        params.put(Api.Params.IMOJIID, imojiId);

        mHttpClient.post(Api.Endpoints.REPORT_ABUSIVE_IMOJI, params, getDefaultHeaders(), new ApiHttpClientCallback<>(cb, mHandler, ReportAbusiveResponse.class));
    }



    private Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Api.Headers.SDK_VERSION, "2.0.0");
        headers.put(Api.Headers.CLIENT_OS_VERSION, Build.VERSION.RELEASE);
        headers.put(Api.Headers.CLIENT_MODEL, "android");
        return headers;
    }


    private static class ApiHttpClientCallback<T extends BasicResponse<V>, V> implements SimpleHttpClient.SimpleHttpClientCallback {


        private Callback<V, String> mCallback;
        private Handler mHandler;
        private Class<T> mClazz;

        public ApiHttpClientCallback(Callback<V, String> cb, Handler handler, Class<T> clazz) {
            mCallback = cb;
            mHandler = handler;
            mClazz = clazz;
        }

        @Override
        public void onSuccess(String response) {
            DeserializationTask<T, V> task = new DeserializationTask<>(response, mCallback, mClazz);
            if (Build.VERSION.SDK_INT >= 11) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }
        }

        @Override
        public void onFailure(final String failure) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onFailure(failure);
                }
            });
        }
    }


    private static class DeserializationTask<T extends BasicResponse<V>, V> extends AsyncTask<T, Void, T> {

        private String mJsonString;
        private Callback<V, String> mCallback;
        private Class<T> mClazz;

        public DeserializationTask(String json, Callback<V, String> cb, Class<T> clazz) {
            mJsonString = json;
            mCallback = cb;
            mClazz = clazz;
        }

        @Override
        protected T doInBackground(T... params) {
            try {
                return sGson.fromJson(mJsonString, mClazz);
            } catch (JsonParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(T t) {
            if (t == null) {
                mCallback.onFailure(BasicResponse.Status.NETWORK_ERROR);
            } else if (!t.isSuccess()) {
                mCallback.onFailure(t.status);
            } else {
                mCallback.onSuccess(t.getPayload());
            }
        }
    }
}