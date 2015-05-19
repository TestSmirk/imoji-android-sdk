package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjadtabib on 5/18/15.
 */
public class ErrorResponse extends BasicResponse<String> {
    @Override
    public String getPayload() {
        return status;
    }
}
