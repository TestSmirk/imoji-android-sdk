package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjadtabib on 10/1/15.
 */
public class ReportAbusiveResponse extends BasicResponse<String> {
    @Override
    public String getPayload() {
        return status;
    }
}
