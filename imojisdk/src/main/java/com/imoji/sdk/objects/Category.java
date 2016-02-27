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

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * A category object represents an opaque grouping of Imoji's.
 */
public class Category {

    public enum  Classification {
        Trending,
        Generic,
        Artist,
        None
    }

    /**
     * Represents the attribution of the category
     */
    public static class Attribution {

        /**
         * A unique id for the category
         */
        @NonNull
        private final String identifier;


        /**
         * A unique id for the category
         */
        @NonNull
        private final Artist artist;

        /**
         * A punchout URL for the attribution
         */
        @NonNull
        private final Uri uri;

        public Attribution(@NonNull String identifier, @NonNull Artist artist, @NonNull Uri uri) {
            this.identifier = identifier;
            this.artist = artist;
            this.uri = uri;
        }

        @NonNull
        public String getIdentifier() {
            return identifier;
        }

        @NonNull
        public Artist getArtist() {
            return artist;
        }

        @NonNull
        public Uri getUri() {
            return uri;
        }
    }

    /**
     * A unique id for the category
     */
    @NonNull
    private final String identifier;

    /**
     * Description of the category.
     */
    @NonNull
    private final String title;

    /**
     * One or more Imoji objects representing the category.
     */
    @NonNull
    private final List<Imoji> previewImojis;

    /**
     * The attribution details associated with the category. This field can be null when the category
     * does not contain artist content.
     */
    @Nullable
    private final Attribution attribution;

    public Category(@NonNull String identifier,
                    @NonNull String title,
                    @NonNull List<Imoji> previewImojis,
                    @Nullable Attribution attribution) {
        this.identifier = identifier;
        this.title = title;
        this.previewImojis = Collections.unmodifiableList(previewImojis);
        this.attribution = attribution;
    }

    @NonNull
    public String getIdentifier() {
        return identifier;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public List<Imoji> getPreviewImojis() {
        return previewImojis;
    }

    @Nullable
    public Attribution getAttribution() {
        return attribution;
    }
}
