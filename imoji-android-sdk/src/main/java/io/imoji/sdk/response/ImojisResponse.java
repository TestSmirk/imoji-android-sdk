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

package io.imoji.sdk.response;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.Imoji;

import java.util.List;

public class ImojisResponse implements ApiResponse {

    @NonNull
    private final List<Imoji> imojis;

    @Nullable
    private final String followupSearchTerm;

    @NonNull
    private final List<Category> relatedCategories;

    public ImojisResponse(@NonNull List<Imoji> imojis,
                          @Nullable String followupSearchTerm,
                          @NonNull List<Category> relatedCategories) {
        this.imojis = imojis;
        this.followupSearchTerm = followupSearchTerm;
        this.relatedCategories = relatedCategories;
    }

    /**
     * @return An array of matched Imoji's
     */
    @NonNull
    public List<Imoji> getImojis() {
        return imojis;
    }

    /**
     * @return A search term that is related to the one used to generated the result set.
     * Can be used to display infinite scrolling of results of related results.
     */
    @Nullable
    public String getFollowupSearchTerm() {
        return followupSearchTerm;
    }

    /**
     * @return An array of categories related to the search term used to generate the result set.
     */
    @NonNull
    public List<Category> getRelatedCategories() {
        return relatedCategories;
    }
}
