package com.imojiapp.imoji.sdk;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by sajjadtabib on 4/7/15.
 */
public class ImojiCategory implements Parcelable{

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

    public ImojiCategory() { //no arg for deserialization
    }

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

    //parcelable implementation
    protected ImojiCategory(Parcel in) {
        mImoji = in.readParcelable(Imoji.class.getClassLoader());
        imojiId = in.readString();
        title = in.readString();
        searchText = in.readString();
        images = in.readParcelable(Imoji.Image.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mImoji, flags);
        dest.writeString(imojiId);
        dest.writeString(title);
        dest.writeString(searchText);
        dest.writeParcelable(images, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImojiCategory> CREATOR = new Creator<ImojiCategory>() {
        @Override
        public ImojiCategory createFromParcel(Parcel in) {
            return new ImojiCategory(in);
        }

        @Override
        public ImojiCategory[] newArray(int size) {
            return new ImojiCategory[size];
        }
    };

}
