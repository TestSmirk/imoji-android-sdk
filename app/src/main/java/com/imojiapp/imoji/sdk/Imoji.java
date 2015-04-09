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
    private String url;
    private String webpThumbImageUrl;
    private String webpFullImageUrl;


    public String getImojiId() {
        return (parentId != null) ? parentId : imojiId;
    }

    public ArrayList<String> getTags() {
        return new ArrayList<>(tags);
    }

    String getThumbImageUrl() {
        return thumbImageUrl;
    }

    String getUrl() {
        return url;
    }

    String getWebpThumbImageUrl() {
        return webpThumbImageUrl;
    }

    String getWebpFullImageUrl() {
        return webpFullImageUrl;
    }


    Imoji() {
    }
}
