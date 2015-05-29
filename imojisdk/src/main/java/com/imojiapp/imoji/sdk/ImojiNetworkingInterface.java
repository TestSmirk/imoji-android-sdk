package com.imojiapp.imoji.sdk;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.imojiapp.imoji.sdk.networking.responses.BasicResponse;
import com.imojiapp.imoji.sdk.networking.responses.ErrorResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by sajjadtabib on 5/28/15.
 */
abstract class ImojiNetworkingInterface {
    abstract void getFeaturedImojis(int offset, int numResults, Callback<List<Imoji>, String> callback);

    abstract void searchImojis(String query, int offset, int numResults, Callback<List<Imoji>, String> callback);

    abstract void getImojiCategories(Callback<List<ImojiCategory>, String> cb);

    abstract void getImojiCategories(String classification, Callback<List<ImojiCategory>, String> cb);

    abstract void getUserImojis(Callback<List<Imoji>, String> cb);

    abstract void getImojisById(List<String> ids, Callback<List<Imoji>, String> cb);

    abstract void addImojiToUserCollection(String imojiId, Callback<String, String> cb);

    abstract GetAuthTokenResponse getAuthToken(String clientId, String clientSecret, String refreshToken);

    abstract void requestExternalOauth(String clientId, Callback<ExternalOauthPayloadResponse, String> cb);

    public static class CallbackWrapper<T extends BasicResponse<V>, V> implements retrofit.Callback<T> {

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
