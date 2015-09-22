package com.imojiapp.imoji.sdk;

import com.imojiapp.imoji.sdk.networking.responses.CreateImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiAckResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by sajjadtabib on 5/28/15.
 */
abstract class ImojiNetworkingInterface {
    abstract void getFeaturedImojis(int offset, int numResults, Callback<List<Imoji>, String> callback);

    abstract void searchImojis(String query, int offset, int numResults, Callback<List<Imoji>, String> callback);

    abstract void searchImojis(Map<String, String> params, Callback<List<Imoji>, String> callback);

    abstract void getImojiCategories(Callback<List<ImojiCategory>, String> cb);

    abstract void getImojiCategories(String classification, Callback<List<ImojiCategory>, String> cb);

    abstract void getUserImojis(Callback<List<Imoji>, String> cb);

    abstract void getImojisById(List<String> ids, Callback<List<Imoji>, String> cb);

    abstract void addImojiToUserCollection(String imojiId, Callback<String, String> cb);

    abstract GetAuthTokenResponse getAuthToken(String clientId, String clientSecret, String refreshToken);

    abstract void requestExternalOauth(String clientId, Callback<ExternalOauthPayloadResponse, String> cb);

    abstract CreateImojiResponse createImoji(List<String> tags);

    abstract ImojiAckResponse ackImoji(String imojiId, boolean hasFull, boolean hasThumb);

}
