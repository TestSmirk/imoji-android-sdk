package com.imojiapp.imoji.sdk.networking.responses;

import com.imojiapp.imoji.sdk.ImojiCategory;

import java.util.List;

/**
 * Created by sajjadtabib on 4/7/15.
 */
public class GetCategoryResponse extends BasicResponse<List<ImojiCategory>> {
    public List<ImojiCategory> categories;

    @Override
    public List<ImojiCategory> getPayload() {
        return categories;
    }
}
