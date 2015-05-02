package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjadtabib on 4/16/15.
 */
public class GetAuthTokenResponse extends BasicResponse<GetAuthTokenResponse>{
    public String access_token;
    public String refresh_token;
    public String token_type;
    public long expires_in;

    @Override
    public GetAuthTokenResponse getPayload() {
        return this;
    }
}
