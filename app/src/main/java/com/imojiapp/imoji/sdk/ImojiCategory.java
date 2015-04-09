package com.imojiapp.imoji.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sajjadtabib on 4/7/15.
 */
public class ImojiCategory {

    public String id;

    public String title;

    public Imoji imoji; //why is imoji an object and not a string???

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Imoji getImoji() {
        return imoji;
    }

}
