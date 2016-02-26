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

package com.imoji.sdk.internal;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.imoji.sdk.RenderingOptions;
import com.imoji.sdk.objects.Category;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.CategoriesResponse;
import com.imoji.sdk.response.CreateImojiResponse;
import com.imoji.sdk.response.ImojisResponse;
import com.imoji.sdk.response.RenderResponse;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiSession extends NetworkSession {

    private static final int NUMBER_OF_EXECUTOR_TASKS = 5;
    private static final ExecutorService DESERIALIZATION_EXECUTOR =
            Executors.newFixedThreadPool(NUMBER_OF_EXECUTOR_TASKS);

    public Future<CategoriesResponse> getImojiCategories(@NonNull Category.Classification classification) {
        return null;
    }

    @NonNull
    @Override
    public Future<ImojisResponse> searchImojis(@NonNull String term,
                                               @Nullable Integer offset,
                                               @Nullable Integer numberOfResults) {
        final HashMap<String, String> params = new HashMap<>(3);
        final HashMap<String, String> headers = new HashMap<>(3);

        params.put("query", term);
        if (offset != null) {
            params.put("offset", offset.toString());
        }

        if (numberOfResults != null) {
            params.put("offset", numberOfResults.toString());
        }

        headers.put("Imoji-SDK-Version", ImojiSDKConstants.SERVER_SDK_VERSION);

        return DESERIALIZATION_EXECUTOR.submit(new Callable<ImojisResponse>() {
            @Override
            public ImojisResponse call() throws Exception {
                try {
                    NetworkResponse response = GET(
                            "/imoji/search", params, headers
                    ).get();

                } catch (NetworkException e) {
                    return null;
                }

                return null;
            }
        });
    }

    @NonNull
    @Override
    public Future<ImojisResponse> getFeaturedImojis(@Nullable Integer numberOfResults) {
        return null;
    }

    @Override
    public Future<ImojisResponse> fetchImojisByIdentifiers(@Nullable List<String> numberOfResults) {
        return null;
    }

    @Override
    public Future<ImojisResponse> searchImojisWithSentence(@NonNull String sentence, @Nullable Integer numberOfResults) {
        return null;
    }

    @Override
    public Future<RenderResponse> renderImoji(@NonNull Imoji imoji, @NonNull RenderingOptions options) {
        return null;
    }

    @Override
    public Future<ImojisResponse> getImojisForAuthenticatedUser() {
        return null;
    }

    @Override
    public Future<Boolean> addImojiToUserCollection(@NonNull Imoji imoji) {
        return null;
    }

    @Override
    public Future<CreateImojiResponse> createImojiWithRawImage(@NonNull Bitmap rawImage, @NonNull Bitmap borderedImage, @Nullable List<String> tags) {
        return null;
    }

    @Override
    public Future<Boolean> removeImoji(@NonNull Imoji imoji) {
        return null;
    }

    @Override
    public Future<Boolean> reportImojiAsAbusive(@NonNull Imoji imoji, @Nullable String reason) {
        return null;
    }

    @Override
    public Future<Boolean> markImojiUsage(@NonNull Imoji imoji, @Nullable String originIdentifier) {
        return null;
    }
}
