/*
 * Imoji Android SDK
 * Created by nkhoshini on 2/25/16.
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

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * A category object represents an opaque grouping of imojis.
 */
public class Category {

    public enum  Classification {
        Trending,
        Generic,
        Artist,
        None
    }

    public class Attribution {
        private final String identifier;
        private final Artist artist;
        private final URL url;

        public Attribution(String identifier, Artist artist, URL url) {
            this.identifier = identifier;
            this.artist = artist;
            this.url = url;
        }

        public String getIdentifier() {
            return identifier;
        }

        public Artist getArtist() {
            return artist;
        }

        public URL getUrl() {
            return url;
        }
    }

    @NonNull
    private final String identifier;

    @NonNull
    private final String title;

    @NonNull
    private final List<Imoji> previewImojis;

    private final int order;

    private final int priority;

    @Nullable
    private final Attribution attribution;

    public Category(@NonNull String identifier,
                    @NonNull String title,
                    @NonNull List<Imoji> previewImojis,
                    int order,
                    int priority,
                    @Nullable Attribution attribution) {
        this.identifier = identifier;
        this.title = title;
        this.previewImojis = Collections.unmodifiableList(previewImojis);
        this.order = order;
        this.priority = priority;
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

    public int getOrder() {
        return order;
    }

    public int getPriority() {
        return priority;
    }

    @Nullable
    public Attribution getAttribution() {
        return attribution;
    }
}
