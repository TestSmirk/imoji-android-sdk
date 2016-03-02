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

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An Imoji is a reference to a sticker within the ImojiSDK.
 */
public class Imoji implements Parcelable {

    public static class Metadata implements Parcelable {
        @Nullable
        private final Integer width;

        @Nullable
        private final Integer height;

        @Nullable
        private final Integer fileSize;

        @NonNull
        private final Uri uri;

        public Metadata(@NonNull Uri uri,
                        @Nullable Integer width,
                        @Nullable Integer height,
                        @Nullable Integer fileSize) {
            this.uri = uri;
            this.width = width;
            this.height = height;
            this.fileSize = fileSize;
        }

        @Nullable
        public Integer getWidth() {
            return width;
        }

        @Nullable
        public Integer getHeight() {
            return height;
        }

        @Nullable
        public Integer getFileSize() {
            return fileSize;
        }

        @NonNull
        public Uri getUri() {
            return uri;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Metadata metadata = (Metadata) o;

            return uri.equals(metadata.uri);

        }

        @Override
        public int hashCode() {
            return uri.hashCode();
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
            dest.writeInt(width != null ? width : 0);
            dest.writeInt(height != null ? height : 0);
            dest.writeInt(fileSize != null ? fileSize : 0);
            dest.writeString(this.uri.toString());
        }

        public static final Parcelable.Creator<Metadata> CREATOR
                = new Parcelable.Creator<Metadata>() {
            public Metadata createFromParcel(Parcel in) {
                return new Metadata(in);
            }

            public Metadata[] newArray(int size) {
                return new Metadata[size];
            }
        };

        private Metadata(Parcel in) {
            int val = in.readInt();
            this.width = val != 0 ? val : null;
            val = in.readInt();
            this.height = val != 0 ? val : null;
            val = in.readInt();
            this.fileSize = val != 0 ? val : null;
            this.uri = Uri.parse(in.readString());
        }
    }

    /**
     * A unique identifier for the imoji.
     */
    @NonNull
    private final String identifier;

    /**
     * One or more tags.
     */
    @NonNull
    private final List<String> tags;

    /**
     * A map of all the URL's of the Imoji images with RenderingOptions as the key.
     */
    @NonNull
    private final Map<RenderingOptions, Metadata> metadataMap;

    /**
     * Whether or not the Imoji has support for animation or not.
     */
    public boolean supportsAnimation() {
        return false;
    }

    @NonNull
    public String getIdentifier() {
        return identifier;
    }

    @NonNull
    public List<String> getTags() {
        return tags;
    }

    /**
     * Gets a download Uri for an Imoji given the requested rendering options
     *
     * @param renderingOptions The Rendering options to use
     * @return A Uri for the Imoji with the supplied RenderingOptions
     */
    @Nullable
    public Uri urlForRenderingOption(RenderingOptions renderingOptions) {
        Metadata metadata = metadataMap.get(renderingOptions);
        return metadata != null ? metadata.uri : null;
    }

    /**
     * Gets the image dimensions for the Imoji and supplied RenderingOptions
     *
     * @param renderingOptions The Rendering options to use
     * @return The image dimensions if any for the Imoji and rendering options
     */
    @Nullable
    public Pair<Integer, Integer> imageDimensionsForRenderingOptions(RenderingOptions renderingOptions) {
        Metadata metadata = metadataMap.get(renderingOptions);

        return metadata != null && metadata.height != null && metadata.width != null ?
                new Pair<>(metadata.width, metadata.height) : null;
    }

    /**
     * Gets the download size for the Imoji and supplied RenderingOptions
     *
     * @param renderingOptions The Rendering options to use
     * @return The download size if any for the Imoji and rendering options. Returns 0 if not found.
     */
    public int fileSizeForRenderingOptions(RenderingOptions renderingOptions) {
        Metadata metadata = metadataMap.get(renderingOptions);
        return metadata != null && metadata.fileSize != null ? metadata.fileSize : 0;
    }

    /**
     * @return If the Imoji has animated GIF content available
     */
    public boolean hasAnimationCapability() {
        return metadataMap.get(RenderingOptions.animatedGifThumbnail()) != null;
    }

    /**
     * @return Gets either a bordered PNG thumbnail URL or GIF thumbnail URL if animation is
     * supported
     */
    public Uri getStandardThumbnailUri() {
        return this.getStandardThumbnailUri(true);
    }

    /**
     * @param supportAnimation Whether or not to fetch an animated URL or not
     * @return Gets either a bordered PNG thumbnail URL or GIF thumbnail URL if animation is
     * supported and requested
     */
    public Uri getStandardThumbnailUri(boolean supportAnimation) {
        if (supportAnimation && hasAnimationCapability()) {
            return this.urlForRenderingOption(RenderingOptions.animatedGifThumbnail());
        }

        return this.urlForRenderingOption(RenderingOptions.borderedPngThumbnail());
    }

    /**
     * @return Gets either a bordered PNG full size URL or GIF thumbnail URL if animation is
     * supported
     */
    public Uri getStandardFullSizeUri() {
        return this.getStandardFullSizeUri(true);
    }

    /**
     * @param supportAnimation Whether or not to fetch an animated URL or not
     * @return Gets either a bordered PNG full size URL or GIF thumbnail URL if animation is
     * supported and requested
     */
    public Uri getStandardFullSizeUri(boolean supportAnimation) {
        if (supportAnimation && hasAnimationCapability()) {
            return this.urlForRenderingOption(RenderingOptions.animatedGifFullSize());
        }

        return this.urlForRenderingOption(RenderingOptions.borderedPngFullSize());
    }

    public Imoji(@NonNull String identifier,
                 @NonNull List<String> tags,
                 @NonNull Map<RenderingOptions, Metadata> metadataMap) {
        this.identifier = identifier;
        this.tags = tags;
        this.metadataMap = metadataMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Imoji imoji = (Imoji) o;

        if (!identifier.equals(imoji.identifier)) return false;
        if (!tags.equals(imoji.tags)) return false;
        return metadataMap.equals(imoji.metadataMap);

    }

    @Override
    public int hashCode() {
        int result = identifier.hashCode();
        result = 31 * result + tags.hashCode();
        result = 31 * result + metadataMap.hashCode();
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
        dest.writeString(this.identifier);
        dest.writeStringList(this.tags);
        dest.writeInt(metadataMap.size());
        for (Map.Entry<RenderingOptions, Metadata> entry : metadataMap.entrySet()) {
            dest.writeParcelable(entry.getKey(), flags);
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    public static final Parcelable.Creator<Imoji> CREATOR
            = new Parcelable.Creator<Imoji>() {
        public Imoji createFromParcel(Parcel in) {
            return new Imoji(in);
        }

        public Imoji[] newArray(int size) {
            return new Imoji[size];
        }
    };

    private Imoji(Parcel in) {
        this.identifier = in.readString();
        this.tags = in.createStringArrayList();
        int entryCount = in.readInt();
        if (entryCount > 0) {
            this.metadataMap = new HashMap<>(entryCount);
            for (int i = 0; i < entryCount; i++) {
                this.metadataMap.put(
                        in.<RenderingOptions>readParcelable(RenderingOptions.class.getClassLoader()),
                        in.<Metadata>readParcelable(Metadata.class.getClassLoader())
                );
            }
        } else {
            this.metadataMap = Collections.emptyMap();
        }
    }
}
