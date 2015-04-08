package com.imojiapp.imoji.sdk.networking.responses;

import com.imojiapp.imoji.sdk.ImojiInternal;

import java.util.LinkedHashMap;

/**
 * Created by sajjadtabib on 11/26/14.
 */
public class FetchImojisResponse extends BasicResponse {
    public LinkedHashMap<String, ImojiInternal> results;

}
