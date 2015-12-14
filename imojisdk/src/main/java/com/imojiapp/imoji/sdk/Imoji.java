package com.imojiapp.imoji.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * An Imoji object is a reference to a sticker within the ImojiSDK.
 * Consumers should not create this object directly, rather,
 * they should use ImojiApi to get them from the server.
 * Created by sajjadtabib on 4/6/15.
 */
public class Imoji implements Parcelable {

    public enum ImageSize {
        /**
         * When used, a compressed version of the Imoji is downloaded and rendered. This setting
         * is useful when the consumer wishes to load and display multiple imojis as fast as
         * possible. Sizes of the thumbnail Imoji's vary but do not exceed 150x150.
         */
        ImageSizeThumbnail,
        /**
         * When used, a high resolution image of the Imoji is downloaded and rendered.
         * This setting is useful when the consumer wishes to export the Imoji to another
         * application or to simply display a large version of it.
         */
        ImageSizeFullResolution,
        /**
         * Renders an Imoji image with a maximum dimension of 320x320. Ideal for high resolution
         * large displays in which the thumbnail size lacks the desired quality.
         */
        ImageSize320,
        /**
         * Renders an Imoji image with a maximum dimension of 512x512.
         */
        ImageSize512
    }

    public enum ImageFormat {
        /**
         * WebP photo format. Using this results in smaller file sizes with
         * minimal quality degradation. Requires JNI libraries for decoding.
         */
        Webp,
        /**
         * PNG photo format. Using this results is larger file sizes but can be decoded natively.
         */
        Png
    }

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

    /**
     * This method has been replaced with getImage
     */
    @Nullable
    @Deprecated
    public String getThumbUrl() {
        return this.getImageUrl(ImageFormat.Png, ImageSize.ImageSizeThumbnail);
    }

    /**
     * This method has been replaced with getImage
     */
    @Nullable
    @Deprecated
    public String getUrl() {
        return this.getImageUrl(ImageFormat.Png, ImageSize.ImageSizeFullResolution);
    }

    /**
     * This method has been replaced with getImage
     */
    @Nullable
    @Deprecated
    public String getWebpThumbUrl() {
        return this.getImageUrl(ImageFormat.Webp, ImageSize.ImageSizeThumbnail);
    }

    /**
     * This method has been replaced with getImage
     */
    @Nullable
    @Deprecated
    public String getWebpUrl() {
        return this.getImageUrl(ImageFormat.Webp, ImageSize.ImageSizeFullResolution);
    }

    /**
     * Returns an appropriate URL for the requested format and size
     */
    @Nullable
    public String getImageUrl(@NonNull ImageFormat imageFormat, @NonNull ImageSize imageSize) {
        if (this.images == null) {
            return null;
        }

        final Image.ImageType imageType;
        switch (imageFormat) {
            case Webp:
                imageType = this.images.webp;
                break;

            case Png:
                imageType = this.images.png;
                break;
            default:
                imageType = null;
                break;
        }

        if (imageType == null) {
            return null;
        }

        final Image.ImageType.Info url;
        switch (imageSize) {
            case ImageSizeThumbnail:
                url = imageType.image150;
                break;

            case ImageSizeFullResolution:
                url = imageType.image1200;
                break;

            case ImageSize320:
                url = imageType.image320;

                break;
            case ImageSize512:
                url = imageType.image512;
                break;

            default:
                url = null;
                break;
        }

        return url != null ? url.url : null;
    }

    static class Image implements Parcelable {

        ImageType png;
        ImageType webp;

        @SuppressWarnings("unused")
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



