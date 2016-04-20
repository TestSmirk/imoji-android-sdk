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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * Set of options for retrieving Categories for a given Session object
 * <p/>
 * Created by nkhoshini on 4/20/16.
 */
public class CategoryFetchOptions {

    /**
     * Type of category classification to retrieve
     */
    @NonNull
    private final Category.Classification classification;

    /**
     * When set, instructs the server to return categories relevant to the search phrase.
     */
    @Nullable
    private String contextualSearchPhrase;

    /**
     * Used in conjunction with contextualSearchPhrase to identify the locale of the phrase.
     */
    @Nullable
    private Locale contextualSearchLocale;

    /**
     * One or more license style references to filter on.
     * @see io.imoji.sdk.objects.Imoji.LicenseStyle
     */
    @Nullable
    private List<Imoji.LicenseStyle> licenseStyles;

    public CategoryFetchOptions(@NonNull Category.Classification classification) {
        this(classification, null, null);
    }

    public CategoryFetchOptions(@NonNull Category.Classification classification,
                                @Nullable String contextualSearchPhrase,
                                @Nullable Locale contextualSearchLocale) {
        this.classification = classification;
        this.contextualSearchPhrase = contextualSearchPhrase;
        this.contextualSearchLocale = contextualSearchLocale;
    }

    @NonNull
    public Category.Classification getClassification() {
        return classification;
    }

    @Nullable
    public String getContextualSearchPhrase() {
        return contextualSearchPhrase;
    }

    public void setContextualSearchPhrase(@Nullable String contextualSearchPhrase) {
        this.contextualSearchPhrase = contextualSearchPhrase;
    }

    @Nullable
    public Locale getContextualSearchLocale() {
        return contextualSearchLocale;
    }

    public void setContextualSearchLocale(@Nullable Locale contextualSearchLocale) {
        this.contextualSearchLocale = contextualSearchLocale;
    }

    @Nullable
    public List<Imoji.LicenseStyle> getLicenseStyles() {
        return licenseStyles;
    }

    public void setLicenseStyles(@Nullable List<Imoji.LicenseStyle> licenseStyles) {
        this.licenseStyles = licenseStyles;
    }
}
