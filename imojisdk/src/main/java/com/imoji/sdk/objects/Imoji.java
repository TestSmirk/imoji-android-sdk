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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Size;

import com.imoji.sdk.RenderingOptions;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * An Imoji is a reference to a sticker within the ImojiSDK.
 */
public class Imoji {

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
    private final Map<RenderingOptions, URL> urls;

    /**
     * A map representation of all the dimensions of the images with RenderingOptions
     * as the key.
     */
    @NonNull
    private final Map<RenderingOptions, Size> imageDimensions;

    /**
     * A map representation of all the file sizes of the images with RenderingOptions
     * as the key.
     */
    @NonNull
    private final Map<RenderingOptions, Integer> fileSizes;

    /**
     * Whether or not the Imoji has support for animation or not.
     */
    public boolean supportsAnimation() {
        return false;
    }

    /**
     * Gets a download URL for an Imoji given the requested rendering options
     *
     * @param renderingOptions The Rendering options to use
     * @return A URL for the Imoji with the supplied RenderingOptions
     */
    @Nullable
    public URL urlForRenderingOption(RenderingOptions renderingOptions) {
        return urls.get(renderingOptions);
    }

    /**
     * Gets the image dimensions for the Imoji and supplied RenderingOptions
     *
     * @param renderingOptions The Rendering options to use
     * @return The image dimensions if any for the Imoji and rendering options
     */
    @Nullable
    public Size imageDimensionsForRenderingOptions(RenderingOptions renderingOptions) {
        return imageDimensions.get(renderingOptions);
    }

    /**
     * Gets the download size for the Imoji and supplied RenderingOptions
     *
     * @param renderingOptions The Rendering options to use
     * @return The download size if any for the Imoji and rendering options. Returns 0 if not found.
     */
    public int fileSizeForRenderingOptions(RenderingOptions renderingOptions) {
        Integer size = fileSizes.get(renderingOptions);
        return size != null ? size : 0;
    }

    public Imoji(@NonNull String identifier,
                 @NonNull List<String> tags,
                 @NonNull Map<RenderingOptions, URL> urls,
                 @NonNull Map<RenderingOptions, Size> imageDimensions,
                 @NonNull Map<RenderingOptions, Integer> fileSizes) {
        this.identifier = identifier;
        this.tags = tags;
        this.urls = urls;
        this.imageDimensions = imageDimensions;
        this.fileSizes = fileSizes;
    }
}
