package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjadtabib on 4/29/15.
 */
public class ExternalOauthPayloadResponse extends BasicResponse<ExternalOauthPayloadResponse>{
    public String base64Key;
    public String payload;


    @Override
    public ExternalOauthPayloadResponse getPayload() {
        return this;
    }
}
