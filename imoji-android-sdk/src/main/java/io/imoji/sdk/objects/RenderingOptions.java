/*
 * Imoji Android SDK
 * Created by nkhoshini
 *
 * Copyright (C) 2016 Imoji
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KID, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

package io.imoji.sdk.objects;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Represents various parameters to display Imoji sticker content
 */
public class RenderingOptions implements Parcelable {

    /**
     * Size of the sticker to display.
     */
    public enum Size {
        /**
         * A Thumbnail is maximum 150x150 pixels
         */
        Thumbnail,
        /**
         * A Full Resolution image is maximum 1200x1200 pixels
         */
        FullResolution,
        /**
         * Maximum 320x320 pixels
         */
        Resolution320,
        /**
         * Maximum 512x512 pixels
         */
        Resolution512
    }

    /**
     * Desired border style for the sticker. Note that animated content does not have a border
     */
    public enum BorderStyle {
        /**
         * Displays a white bordered sticker with a slight drop shadow
         */
        Sticker,
        /**
         * Displays a non-bordered sticker
         */
        None
    }

    /**
     * Various support formats the stickers can be encoded from. Typically WebP images will have
     * smaller download sizes than Png.
     * Refer to https://github.com/imojiengineering/bifrost for rendering WebP content if desired
     */
    public enum ImageFormat {
        Png,
        WebP,
        AnimatedGif,
        AnimatedWebp
    }

    @NonNull
    private final BorderStyle borderStyle;

    @NonNull
    private final ImageFormat imageFormat;

    @NonNull
    private final Size size;

    public RenderingOptions(@NonNull BorderStyle borderStyle, @NonNull ImageFormat imageFormat, @NonNull Size size) {
        this.borderStyle = borderStyle;
        this.imageFormat = imageFormat;
        this.size = size;
    }

    @NonNull
    public BorderStyle getBorderStyle() {
        return borderStyle;
    }

    @NonNull
    public ImageFormat getImageFormat() {
        return imageFormat;
    }

    @NonNull
    public Size getSize() {
        return size;
    }

    @NonNull
    public static RenderingOptions borderedPngThumbnail() {
        return new RenderingOptions(BorderStyle.Sticker, ImageFormat.Png, Size.Thumbnail);
    }

    @NonNull
    public static RenderingOptions borderedPngFullSize() {
        return new RenderingOptions(BorderStyle.Sticker, ImageFormat.Png, Size.FullResolution);
    }

    @NonNull
    public static RenderingOptions borderedWebThumbnail() {
        return new RenderingOptions(BorderStyle.Sticker, ImageFormat.WebP, Size.Thumbnail);
    }

    @NonNull
    public static RenderingOptions borderedWebFullSize() {
        return new RenderingOptions(BorderStyle.Sticker, ImageFormat.WebP, Size.FullResolution);
    }

    @NonNull
    public static RenderingOptions animatedGifThumbnail() {
        return new RenderingOptions(BorderStyle.None, ImageFormat.AnimatedGif, Size.Thumbnail);
    }

    @NonNull
    public static RenderingOptions animatedGifFullSize() {
        return new RenderingOptions(BorderStyle.None, ImageFormat.AnimatedGif, Size.FullResolution);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RenderingOptions that = (RenderingOptions) o;

        if (borderStyle != that.borderStyle) return false;
        if (imageFormat != that.imageFormat) return false;
        return size == that.size;

    }

    @Override
    public int hashCode() {
        int result = borderStyle.hashCode();
        result = 31 * result + imageFormat.hashCode();
        result = 31 * result + size.hashCode();
        return result;
    }

    /**
     * Parcelable Overrides
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.borderStyle.ordinal());
        dest.writeInt(this.imageFormat.ordinal());
        dest.writeInt(this.size.ordinal());
    }

    public static final Parcelable.Creator<RenderingOptions> CREATOR
            = new Parcelable.Creator<RenderingOptions>() {
        public RenderingOptions createFromParcel(Parcel in) {
            return new RenderingOptions(in);
        }

        public RenderingOptions[] newArray(int size) {
            return new RenderingOptions[size];
        }
    };

    private RenderingOptions(Parcel in) {
        this.borderStyle = BorderStyle.values()[in.readInt()];
        this.imageFormat = ImageFormat.values()[in.readInt()];
        this.size = Size.values()[in.readInt()];
    }
}
