package com.imojiapp.imoji.sdk.networking.responses;


import com.imojiapp.imoji.sdk.ImojiInternal;

import java.util.ArrayList;

/**
 * Created by sajjadtabib on 10/6/14.
 */
public class ImojiSearchResponse extends BasicResponse{
    public ArrayList<ImojiInternal> results;
    public ArrayList<ImojiInternal> data;
}
