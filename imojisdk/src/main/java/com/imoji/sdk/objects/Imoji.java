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

package com.imoji.sdk.objects;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.imoji.sdk.RenderingOptions;

import java.util.List;
import java.util.Map;

/**
 * An Imoji is a reference to a sticker within the ImojiSDK.
 */
public class Imoji {

    public static class Metadata {
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

        return metadata != null  && metadata.height != null && metadata.width != null ?
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
    public Uri getStandardFullSizelUri() {
        return this.getStandardFullSizelUri(true);
    }

    /**
     * @param supportAnimation Whether or not to fetch an animated URL or not
     * @return Gets either a bordered PNG full size URL or GIF thumbnail URL if animation is
     * supported and requested
     */
    public Uri getStandardFullSizelUri(boolean supportAnimation) {
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
}
