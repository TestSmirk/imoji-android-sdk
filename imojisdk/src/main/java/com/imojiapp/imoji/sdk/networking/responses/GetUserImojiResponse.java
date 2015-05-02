package com.imojiapp.imoji.sdk.networking.responses;

import com.imojiapp.imoji.sdk.Imoji;

import java.util.List;

/**
 * Created by sajjad on 5/2/15.
 */
public class GetUserImojiResponse extends BasicResponse<List<Imoji>> {

    public List<Imoji> results;

    @Override
    public List<Imoji> getPayload() {
        return results;
    }
}
