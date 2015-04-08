package com.imojiapp.imoji.sdk;

import java.util.ArrayList;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public class Imoji {

    private String parentId;
    private String imojiId;
    private ArrayList<String> tags;
    private String thumbImageUrl;
    private String fullImageUrl;


    public String getImojiId() {
        return (parentId != null) ? parentId : imojiId;
    }

    public ArrayList<String> getTags() {
        return new ArrayList<>(tags);
    }

    String getThumbImageUrl() {
        return thumbImageUrl;
    }

    String getFullImageUrl() {
        return fullImageUrl;
    }


    Imoji(String parentId, String imojiId, String thumbImageUrl, String fullImageUrl, ArrayList<String> tags) {
        this.parentId = parentId;
        this.imojiId = imojiId;
        this.tags = tags;
        this.thumbImageUrl = thumbImageUrl;
        this.fullImageUrl = fullImageUrl;
    }
}
