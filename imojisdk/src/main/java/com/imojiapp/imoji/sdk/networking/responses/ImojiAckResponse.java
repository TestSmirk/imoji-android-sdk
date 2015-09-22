package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjad on 9/4/14.
 */
public class ImojiAckResponse extends BasicResponse<String> {

    @Override
    public String getPayload() {
        return status;
    }
}
