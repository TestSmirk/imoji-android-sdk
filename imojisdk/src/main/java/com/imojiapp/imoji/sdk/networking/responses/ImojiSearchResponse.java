package com.imojiapp.imoji.sdk.networking.responses;


import com.imojiapp.imoji.sdk.Imoji;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajjadtabib on 10/6/14.
 */
public class ImojiSearchResponse extends BasicResponse<List<Imoji>>{
    public ArrayList<Imoji> results;

    @Override
    public List<Imoji> getPayload() {
        return results;
    }
}
