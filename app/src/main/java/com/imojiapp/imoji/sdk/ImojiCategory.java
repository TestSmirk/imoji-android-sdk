package com.imojiapp.imoji.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajjadtabib on 4/7/15.
 */
public class ImojiCategory {

    String id;

    String title;

    @SerializedName("imoji")
    String imoji_id;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImojiId() {
        return imoji_id;
    }

    transient Imoji imoji;

}
