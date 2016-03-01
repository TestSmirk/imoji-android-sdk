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

package com.imoji.sdk;

import android.support.annotation.NonNull;

public class RenderingOptions {
    public enum Size {
        Thumbnail,
        FullResolution,
        Resolution320,
        Resolution512
    }

    public enum BorderStyle {
        Sticker,
        None
    }

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
}
