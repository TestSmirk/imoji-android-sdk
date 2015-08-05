package com.imojiapp.imoji.sdk;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public class Imoji implements Parcelable{


    private String id;
    private ArrayList<String> tags;
    private Object urls;
    private Image images;


    public Imoji() { //no arg constructor for deserialization
    }

    Imoji(String id, Image images, ArrayList<String> tags) {

        this.id = id;
        this.images = images;
        this.tags = tags;
    }

    protected Imoji(Parcel in) {
        id = in.readString();
        tags = in.createStringArrayList();
        images = in.readParcelable(Image.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringList(tags);
        dest.writeParcelable(images, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Imoji> CREATOR = new Creator<Imoji>() {
        @Override
        public Imoji createFromParcel(Parcel in) {
            return new Imoji(in);
        }

        @Override
        public Imoji[] newArray(int size) {
            return new Imoji[size];
        }
    };

    public String getImojiId() {
        return id;
    }

    public ArrayList<String> getTags() {
        return new ArrayList<>(tags);
    }

    public String getThumbUrl() {
        return images.png.image150.url;
    }

    public String getUrl() {
        return images.png.image1200.url;
    }

    public String getWebpThumbUrl() {
        return images.webp.image150.url;
    }

    public String getWebpUrl() {
        return images.webp.image1200.url;
    }

    static class Image implements Parcelable{

        ImageType png;
        ImageType webp;

        public Image() { //no arg constructor for deserialization
        }

        static class ImageType implements Parcelable {

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

            public ImageType() { //no arg constructor for deserialization
            }


            static class Info implements Parcelable {

                String url;
                int width;
                int height;
                long fileSize;

                public Info() { //no arg constructor for JSON deserialization
                }

                protected Info(Parcel in) {
                    url = in.readString();
                    width = in.readInt();
                    height = in.readInt();
                    fileSize = in.readLong();
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(url);
                    dest.writeInt(width);
                    dest.writeInt(height);
                    dest.writeLong(fileSize);
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                public static final Creator<Info> CREATOR = new Creator<Info>() {
                    @Override
                    public Info createFromParcel(Parcel in) {
                        return new Info(in);
                    }

                    @Override
                    public Info[] newArray(int size) {
                        return new Info[size];
                    }
                };
            }

            protected ImageType(Parcel in) {
                image150 = in.readParcelable(Info.class.getClassLoader());
                image320 = in.readParcelable(Info.class.getClassLoader());
                image512 = in.readParcelable(Info.class.getClassLoader());
                image960 = in.readParcelable(Info.class.getClassLoader());
                image1200 = in.readParcelable(Info.class.getClassLoader());
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeParcelable(image150, flags);
                dest.writeParcelable(image320, flags);
                dest.writeParcelable(image512, flags);
                dest.writeParcelable(image960, flags);
                dest.writeParcelable(image1200, flags);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<ImageType> CREATOR = new Creator<ImageType>() {
                @Override
                public ImageType createFromParcel(Parcel in) {
                    return new ImageType(in);
                }

                @Override
                public ImageType[] newArray(int size) {
                    return new ImageType[size];
                }
            };
        }

        protected Image(Parcel in) {
            png = in.readParcelable(ImageType.class.getClassLoader());
            webp = in.readParcelable(ImageType.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(png, flags);
            dest.writeParcelable(webp, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };
    }

}



