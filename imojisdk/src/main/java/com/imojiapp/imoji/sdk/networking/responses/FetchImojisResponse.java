package com.imojiapp.imoji.sdk.networking.responses;

import com.imojiapp.imoji.sdk.Imoji;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by sajjadtabib on 11/26/14.
 */
public class FetchImojisResponse extends BasicResponse<List<Imoji>> {
    public LinkedHashMap<String, Imoji> results;

    @Override
    public List<Imoji> getPayload() {
        return null;
    }
}
