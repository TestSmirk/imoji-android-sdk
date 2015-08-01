package com.imojiapp.imoji.sdk;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public class Imoji {

    private String id;
    private ArrayList<String> tags;
    private Object urls;
    private Image images;


    public String getImojiId() {
        return id;
    }

    public ArrayList<String> getTags() {
        return new ArrayList<>(tags);
    }

    String getThumbImageUrl() {
        return images.png.image150.url;
    }

    String getUrl() {
        return images.png.image1200.url;
    }

    String getWebpThumbImageUrl() {
        return images.webp.image150.url;
    }

    String getWebpFullImageUrl() {
        return images.webp.image1200.url;
    }

    static class Image {

        ImageType png;
        ImageType webp;

        static class ImageType {

            @SerializedName("150")
            Info image150;

            @SerializedName("320")
            Info image320;

            @SerializedName("512")
            Info image512;

            @SerializedName("960")
            Info image960;

            @SerializedName("1200")
            Info image1200;

            static class Info {

                String url;
                int width;
                int height;
                long fileSize;

            }

        }
    }

}



