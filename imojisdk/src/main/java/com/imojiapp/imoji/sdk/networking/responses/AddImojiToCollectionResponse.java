package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjadtabib on 5/18/15.
 */
public class AddImojiToCollectionResponse extends BasicResponse<String> {
    @Override
    public String getPayload() {
        return status;
    }
}
