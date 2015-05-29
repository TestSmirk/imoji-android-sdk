package com.imojiapp.imoji.sdk;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.imojiapp.imoji.sdk.networking.responses.BasicResponse;
import com.imojiapp.imoji.sdk.networking.responses.ErrorResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.koushikdutta.async.future.FutureCallback;

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

}
