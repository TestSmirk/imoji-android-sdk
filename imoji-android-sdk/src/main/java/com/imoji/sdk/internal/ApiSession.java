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

package com.imoji.sdk.internal;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.imoji.sdk.ApiTask;
import com.imoji.sdk.StoragePolicy;
import com.imoji.sdk.objects.Category;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.ApiResponse;
import com.imoji.sdk.response.CategoriesResponse;
import com.imoji.sdk.response.CreateImojiResponse;
import com.imoji.sdk.response.ImojisResponse;
import com.imoji.sdk.response.ImojiUploadResponse;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ApiSession extends NetworkSession {

    public ApiSession(@NonNull StoragePolicy storagePolicy) {
        super(storagePolicy);
    }

    @NonNull
    @Override
    public ApiTask<CategoriesResponse> getImojiCategories(@NonNull Category.Classification classification) {
        return validatedGet(
                ImojiSDKConstants.Paths.CATEGORIES_FETCH,
                CategoriesResponse.class,
                Collections.singletonMap("classification", classification.name().toLowerCase()),
                null
        );
    }

    @NonNull
    @Override
    public ApiTask<ImojisResponse> searchImojis(@NonNull String term) {
        return this.searchImojis(term, null, null);
    }

    @NonNull
    @Override
    public ApiTask<ImojisResponse> searchImojis(@NonNull String term,
                                                @Nullable Integer offset,
                                                @Nullable Integer numberOfResults) {
        final HashMap<String, String> params = new HashMap<>(3);

        params.put("query", term);
        if (offset != null) {
            params.put("offset", offset.toString());
        }

        if (numberOfResults != null) {
            params.put("numResults", numberOfResults.toString());
        }

        return validatedGet(ImojiSDKConstants.Paths.SEARCH, ImojisResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<ImojisResponse> getFeaturedImojis() {
        return this.getFeaturedImojis(null);
    }

    @NonNull
    @Override
    public ApiTask<ImojisResponse> getFeaturedImojis(@Nullable Integer numberOfResults) {
        final HashMap<String, String> params = new HashMap<>(1);

        if (numberOfResults != null) {
            params.put("numResults", numberOfResults.toString());
        }

        return validatedGet(ImojiSDKConstants.Paths.FEATURED, ImojisResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<ImojisResponse> fetchImojisByIdentifiers(@NonNull List<String> identifiers) {
        final String ids = TextUtils.join(",", identifiers);
        return validatedPost(ImojiSDKConstants.Paths.FETCH_IMOJIS_BY_ID, ImojisResponse.class, Collections.singletonMap("ids", ids), null);
    }

    @NonNull
    @Override
    public ApiTask<ImojisResponse> searchImojisWithSentence(@NonNull String sentence) {
        return this.searchImojisWithSentence(sentence, null);
    }

    @NonNull
    @Override
    public ApiTask<ImojisResponse> searchImojisWithSentence(@NonNull String sentence, @Nullable Integer numberOfResults) {
        final HashMap<String, String> params = new HashMap<>(2);

        params.put("sentence", sentence);

        if (numberOfResults != null) {
            params.put("numResults", numberOfResults.toString());
        }

        return validatedGet(ImojiSDKConstants.Paths.SEARCH, ImojisResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<CreateImojiResponse> createImojiWithRawImage(@NonNull final Bitmap rawImage,
                                                                @NonNull Bitmap borderedImage,
                                                                @Nullable final List<String> tags) {
        return new ApiTask<>(new Callable<CreateImojiResponse>() {
            @Override
            public CreateImojiResponse call() throws Exception {
                Map<String, String> params = new HashMap<>(1);

                if (tags != null) {
                    params.put("tags", TextUtils.join(",", tags));
                }

                ImojiUploadResponse imojiUploadResponse =
                        validatedPost(
                                ImojiSDKConstants.Paths.CREATE_IMOJI,
                                ImojiUploadResponse.class,
                                params,
                                null
                        ).executeImmediately();

                Uri uploadUri = imojiUploadResponse.getUploadUri();

                byte[] resizedImage = BitmapUtils.getPngDataWithMaxBoundaries(
                        rawImage, imojiUploadResponse.getMaxWidth(), imojiUploadResponse.getMaxHeight()
                );

                makePutDataRequest(
                        uploadUri,
                        resizedImage,
                        Collections.singletonMap("Content-Type", "image/png")
                ).executeImmediately();

                String imojiId = imojiUploadResponse.getImojiId();
                ImojisResponse imojisResponse =
                        fetchImojisByIdentifiers(Collections.singletonList(imojiId)).executeImmediately();

                if (imojisResponse.getImojis().isEmpty()) {
                    throw new IllegalStateException("Could not fetch Imoji with identifier " + imojiId + " after creation");
                }

                return new CreateImojiResponse(imojisResponse.getImojis().iterator().next());
            }
        });
    }

    @NonNull
    @Override
    public ApiTask<ApiResponse> removeImoji(@NonNull Imoji imoji) {
        return validatedDelete(ImojiSDKConstants.Paths.REMOVE_IMOJI, ApiResponse.class, Collections.singletonMap("imojiId", imoji.getIdentifier()), null);
    }

    @NonNull
    @Override
    public ApiTask<ApiResponse> reportImojiAsAbusive(@NonNull Imoji imoji,
                                                     @Nullable String reason) {
        final HashMap<String, String> params = new HashMap<>(2);

        params.put("imojiId", imoji.getIdentifier());
        params.put("reason", reason);

        return validatedPost(ImojiSDKConstants.Paths.REPORT_IMOJI, ApiResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<ApiResponse> markImojiUsage(@NonNull Imoji imoji,
                                               @Nullable String originIdentifier) {
        final HashMap<String, String> params = new HashMap<>(2);

        params.put("imojiId", imoji.getIdentifier());

        if (originIdentifier != null) {
            params.put("originIdentifier", originIdentifier);
        }

        return validatedGet(ImojiSDKConstants.Paths.IMOJI_USAGE, ApiResponse.class, params, null);
    }

    private static class BitmapUtils {

        public static int[] getSizeWithinBounds(int width, int height, int boundsWidth, int boundsHeight, boolean expandToFitBounds) {
            int[] size = new int[2];

            //if we fit within the bounds then don't scale
            if (!expandToFitBounds && (width <= boundsWidth && height <= boundsHeight)) {
                size[0] = width;
                size[1] = height;
                return size;
            }

            //get the aspect ratio of the original size
            float originalAspectRatio = (float) width / (float) height;
            float boundsAspectRatio = (float) boundsWidth / (float) boundsHeight;

            if (originalAspectRatio > boundsAspectRatio) {
                size[0] = boundsWidth;
                size[1] = (int) ((float) boundsWidth / originalAspectRatio);
            } else {
                size[1] = boundsHeight;
                size[0] = (int) ((float) boundsHeight * originalAspectRatio);
            }

            return size;
        }

        public static byte[] getPngDataWithMaxBoundaries(Bitmap bitmap, int maxWidth, int maxHeight) {
            if (bitmap.getWidth() > maxWidth && bitmap.getHeight() > maxHeight) {
                int[] size = getSizeWithinBounds(bitmap.getWidth(), bitmap.getHeight(), maxWidth, maxHeight, false);

                //resize image
                bitmap = Bitmap.createScaledBitmap(bitmap, size[0], size[1], false);
            }

            //estimate the size
            int initialArraySize = bitmap.getWidth() * bitmap.getHeight() / 10;

            ByteArrayOutputStream out = new ByteArrayOutputStream(initialArraySize);

            //compress the bitmap to png
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            return out.toByteArray();
        }
    }
}
