package com.imojiapp.imoji.sdk;

/**
 * Created by sajjadtabib on 4/7/15.
 */
public class ImojiCategory {

    public interface Classification {
        String NONE = "none";
        String TRENDING = "trending";
        String GENERIC = "generic";
    } 

    private String id;

    private String title;

    private Imoji imoji; //why is imoji an object and not a string???

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
