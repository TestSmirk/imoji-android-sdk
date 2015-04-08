package com.imojiapp.imoji.sdk;

import com.imojiapp.imoji.sdk.Imoji;

import java.util.ArrayList;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public class ImojiInternal {

        protected String parentId;

        protected String imojiId;

        
        public ArrayList<String> tags;


        public int counter;


        public String author;


        public long created;


        public long sendCounter;


        public String url;


        public String thumbImageUrl;


        public boolean isTransacting;


        public String state;


        public long updatedAt;


        public boolean isSyncable = true;


        public String localThumbFilename;


        public int thumbWidth;


        public int thumbHeight;


        public int originalWidth;


        public int originalHeight;


        public String localFilename;


        public String originalWebUrl;


        public int fullImageMaxWidth;


        public int fullImageMaxHeight;


        public int thumbResizeWidth;


        public int thumbResizeHeight;


        public String webpFullImageUrl;


        public String webpThumbImageUrl;

    public Imoji getImoji() {
        return new Imoji(parentId, imojiId, thumbImageUrl, url, tags);
    }
}
