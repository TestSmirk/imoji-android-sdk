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

package io.imoji.sdk.internal;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.StoragePolicy;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.CategoryFetchOptions;
import io.imoji.sdk.objects.CollectionType;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.response.CategoriesResponse;
import io.imoji.sdk.response.CreateImojiResponse;
import io.imoji.sdk.response.GenericApiResponse;
import io.imoji.sdk.response.ImojiAttributionsResponse;
import io.imoji.sdk.response.ImojiUploadResponse;
import io.imoji.sdk.response.ImojisResponse;

public class ApiSession extends NetworkSession {

    public ApiSession(@NonNull StoragePolicy storagePolicy) {
        super(storagePolicy);
    }

    @NonNull
    @Override
    public ApiTask<CategoriesResponse> getImojiCategories(@NonNull Category.Classification classification) {
        return this.getImojiCategories(new CategoryFetchOptions(classification));
    }

    @NonNull
    @Override
    public ApiTask<CategoriesResponse> getImojiCategories(@NonNull CategoryFetchOptions fetchOptions) {
        Map<String, String> params = new HashMap<>();
        params.put("classification", fetchOptions.getClassification().name().toLowerCase());

        if (fetchOptions.getContextualSearchPhrase() != null) {
            params.put("contextualSearchPhrase", fetchOptions.getContextualSearchPhrase());
            if (fetchOptions.getContextualSearchLocale() != null) {
                params.put("locale", fetchOptions.getContextualSearchLocale().toString());
            }
        }

        if (fetchOptions.getLicenseStyles() != null && !fetchOptions.getLicenseStyles().isEmpty()) {
            int val = 0;
            for (Imoji.LicenseStyle license  : fetchOptions.getLicenseStyles()) {
                val |= license.getValue();
            }
            params.put("licenseStyles", Integer.toString(val));
        }

        return validatedGet(
                ImojiSDKConstants.Paths.CATEGORIES_FETCH,
                CategoriesResponse.class,
                params,
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
    public ApiTask<ImojisResponse> getCollectedImojis(@Nullable CollectionType collectionType) {
        final HashMap<String, String> params = new HashMap<>(1);

        if (collectionType != null) {
            switch (collectionType) {
                case Recents:
                    params.put("collectionType", "recents");
                    break;

                case Created:
                    params.put("collectionType", "created");
                    break;

                case Liked:
                    params.put("collectionType", "liked");
                    break;
            }
        }

        return validatedGet(ImojiSDKConstants.Paths.COLLECTION_FETCH, ImojisResponse.class, params, null);
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
    public ApiTask<GenericApiResponse> removeImoji(@NonNull Imoji imoji) {
        return validatedDelete(ImojiSDKConstants.Paths.REMOVE_IMOJI, GenericApiResponse.class, Collections.singletonMap("imojiId", imoji.getIdentifier()), null);
    }

    @NonNull
    @Override
    public ApiTask<GenericApiResponse> reportImojiAsAbusive(@NonNull Imoji imoji,
                                                            @Nullable String reason) {
        return this.reportImojiAsAbusive(imoji.getIdentifier(), reason);
    }

    @NonNull
    @Override
    public ApiTask<GenericApiResponse> reportImojiAsAbusive(@NonNull String imojiId, @Nullable String reason) {
        final HashMap<String, String> params = new HashMap<>(2);

        params.put("imojiId", imojiId);
        params.put("reason", reason);

        return validatedPost(ImojiSDKConstants.Paths.REPORT_IMOJI, GenericApiResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<GenericApiResponse> markImojiUsage(@NonNull Imoji imoji,
                                                      @Nullable String originIdentifier) {
        return this.markImojiUsage(imoji.getIdentifier(), originIdentifier);
    }

    @NonNull
    @Override
    public ApiTask<GenericApiResponse> markImojiUsage(@NonNull String imojiId, @Nullable String originIdentifier) {
        final HashMap<String, String> params = new HashMap<>(2);

        params.put("imojiId", imojiId);

        if (originIdentifier != null) {
            params.put("originIdentifier", originIdentifier);
        }

        return validatedGet(ImojiSDKConstants.Paths.IMOJI_USAGE, GenericApiResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<GenericApiResponse> addImojiToUserCollection(@NonNull String imojiId) {
        final HashMap<String, String> params = new HashMap<>(2);
        params.put("imojiId", imojiId);

        return validatedPost(ImojiSDKConstants.Paths.COLLECTION_ADD, GenericApiResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<ImojiAttributionsResponse> fetchAttributionByImojiIdentifiers(@NonNull List<String> identifiers) {
        final HashMap<String, String> params = new HashMap<>(2);
        final String ids = TextUtils.join(",", identifiers);

        params.put("imojiIds", ids);

        return validatedGet(ImojiSDKConstants.Paths.IMOJI_ATTRIBUTION, ImojiAttributionsResponse.class, params, null);
    }

    @NonNull
    @Override
    public ApiTask<GenericApiResponse> setUserDemographicsData(@Nullable String gender,
                                                               @Nullable Double latitude,
                                                               @Nullable Double longitude,
                                                               @Nullable Date dateOfBirth) {
        final HashMap<String, String> params = new HashMap<>(5);

        if (gender != null) {
            if ("male".equals(gender) || "female".equals(gender)) {
                params.put("gender", gender);
            } else {
                Log.w(ApiSession.class.getCanonicalName(), "Unknown value '" + gender + "' sent for gender. Possible values are 'male' or 'female'");
            }
        }

        if (latitude != null && longitude != null) {
            params.put("latitude", latitude.toString());
            params.put("longitude", longitude.toString());
        }

        if (dateOfBirth != null) {
            params.put("dateOfBirth", new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(dateOfBirth));
        }

        if (params.isEmpty()) {
            return emptyGenericApiTask();
        }

        return validatedPost(ImojiSDKConstants.Paths.SET_USER_DEMOGRAPHICS, GenericApiResponse.class, params, null);
    }

    private static class BitmapUtils {

        static int[] getSizeWithinBounds(int width, int height, int boundsWidth, int boundsHeight, boolean expandToFitBounds) {
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

        static byte[] getPngDataWithMaxBoundaries(Bitmap bitmap, int maxWidth, int maxHeight) {
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

    private static ApiTask<GenericApiResponse> emptyGenericApiTask(){
        return new ApiTask<>(new Callable<GenericApiResponse>() {
            @Override
            public GenericApiResponse call() throws Exception {
                return new GenericApiResponse();
            }
        });
    }
}
