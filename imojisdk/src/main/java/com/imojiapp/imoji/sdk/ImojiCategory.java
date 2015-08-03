package com.imojiapp.imoji.sdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by sajjadtabib on 4/7/15.
 */
public class ImojiCategory {

    public interface Classification {
        String NONE = "none";
        String TRENDING = "trending";
        String GENERIC = "generic";
    }

    private Imoji mImoji;

    private String imojiId;

    private String title;

    private String searchText;

    private Imoji.Image images;

    public String getTitle() {
        return title;
    }

    public String getSearchText(){ return searchText; }

    /**
     * @deprecated use {@link #getSearchText()}
     * @return the search text
     */
    @Deprecated
    public String getId() {
        return searchText;
    }

    public Imoji getImoji() {

        if (mImoji == null) {
            mImoji = new Imoji(imojiId, images, new ArrayList<>(Arrays.asList(new String[]{searchText})));
        }

        return mImoji;
    }

}
