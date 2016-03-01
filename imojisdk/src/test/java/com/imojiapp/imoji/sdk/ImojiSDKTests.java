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

package com.imojiapp.imoji.sdk;

import android.test.AndroidTestCase;

import com.imoji.sdk.ApiTask;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.RenderingOptions;
import com.imoji.sdk.Session;
import com.imoji.sdk.objects.Artist;
import com.imoji.sdk.objects.Category;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.ApiResponse;
import com.imoji.sdk.response.CategoriesResponse;
import com.imoji.sdk.response.ImojisResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Imoji Android SDK
 * <p>
 * Created by nkhoshini on 2/26/16.
 */
public class ImojiSDKTests extends AndroidTestCase {

    private static final UUID CLIENT_ID = UUID.fromString("748cddd4-460d-420a-bd42-fcba7f6c031b");
    private static final String API_TOKEN = "U2FsdGVkX1/yhkvIVfvMcPCALxJ1VHzTt8FPZdp1vj7GIb+fsdzOjyafu9MZRveo7ebjx1+SKdLUvz8aM6woAw==";
    private static final String SAMPLE_IMOJI_ID = "ac6e038f-3392-46a6-a1fb-573cd2fea1cb";

    private Session sdkSession;

    protected void setUp() throws Exception {
        sdkSession = ImojiSDK.getInstance()
                .setCredentials(CLIENT_ID, API_TOKEN)
                .createSession(getContext());
    }

    public void testSearch() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        sdkSession.searchImojis("haha").executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
            @Override
            protected void onPostExecute(ImojisResponse imojisResponse) {
                validateImojiResponse(imojisResponse);
                latch.countDown();
            }
        });

        latch.await();
    }

    public void testFeatured() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        sdkSession.getFeaturedImojis(40).executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
            @Override
            protected void onPostExecute(ImojisResponse imojisResponse) {
                validateImojiResponse(imojisResponse);
                latch.countDown();
            }
        });

        latch.await();
    }

    public void testSentenceSearch() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        sdkSession.searchImojisWithSentence("this is great!").executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
            @Override
            protected void onPostExecute(ImojisResponse imojisResponse) {
                validateImojiResponse(imojisResponse);
                latch.countDown();
            }
        });

        latch.await();
    }

    public void testArtistCategories() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        sdkSession.getImojiCategories(Category.Classification.Artist).executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
            @Override
            protected void onPostExecute(CategoriesResponse categoriesResponse) {
                assertNotNull(categoriesResponse);
                assertNotNull(categoriesResponse.getCategories());
                assertNotSame(categoriesResponse.getCategories().size(), 0);

                Category category = categoriesResponse.getCategories().iterator().next();

                assertNotNull(category);
                assertNotNull(category.getIdentifier());
                assertNotNull(category.getPreviewImojis());
                assertNotSame(category.getPreviewImojis().size(), 0);
                assertNotNull(category.getTitle());
                assertNotNull(category.getAttribution());
                assertNotNull(category.getAttribution().getIdentifier());
                assertNotNull(category.getAttribution().getArtist());
                assertNotNull(category.getAttribution().getUri());

                Imoji previewImoji = category.getPreviewImojis().iterator().next();
                assertNotNull(previewImoji.getIdentifier());
                assertNotNull(previewImoji.getTags());
                assertNotNull(previewImoji.urlForRenderingOption(RenderingOptions.borderedPngThumbnail()));

                Artist artist = category.getAttribution().getArtist();
                assertNotNull(artist.getIdentifier());
                assertNotNull(artist.getDescription());
                assertNotNull(artist.getName());
                assertNotNull(artist.getProfileImoji());
                assertNotNull(artist.getProfileImoji().getIdentifier());
                assertNotNull(artist.getProfileImoji().urlForRenderingOption(RenderingOptions.borderedPngThumbnail()));

                latch.countDown();

            }
        });

        latch.await();
    }

    public void testFetchByIds() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final List<String> identifiers = new ArrayList<>();
        sdkSession.searchImojis("haha").executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
            @Override
            protected void onPostExecute(ImojisResponse imojisResponse) {
                validateImojiResponse(imojisResponse);

                // add a couple of id's
                Iterator<Imoji> imojis = imojisResponse.getImojis().iterator();
                identifiers.add(imojis.next().getIdentifier());
                identifiers.add(imojis.next().getIdentifier());

                latch.countDown();
            }
        });

        latch.await();

        final CountDownLatch secondaryLatch = new CountDownLatch(1);

        sdkSession.fetchImojisByIdentifiers(identifiers).executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
            @Override
            protected void onPostExecute(ImojisResponse imojisResponse) {
                validateImojiResponse(imojisResponse);

                assertSame(imojisResponse.getImojis().size(), identifiers.size());
                Iterator<Imoji> imojis = imojisResponse.getImojis().iterator();
                assertTrue(identifiers.contains(imojis.next().getIdentifier()));
                assertTrue(identifiers.contains(imojis.next().getIdentifier()));

                secondaryLatch.countDown();

            }
        });

        secondaryLatch.await();
    }

    public void testReportAsAbusive() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Imoji> imojiReference = new AtomicReference<>();

        sdkSession.fetchImojisByIdentifiers(Collections.singletonList(SAMPLE_IMOJI_ID))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        validateImojiResponse(imojisResponse);

                        imojiReference.set(imojisResponse.getImojis().iterator().next());
                        latch.countDown();
                    }
                });

        latch.await();

        final CountDownLatch secondaryLatch = new CountDownLatch(1);

        sdkSession.reportImojiAsAbusive(imojiReference.get(), "Android Testing")
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ApiResponse>() {
                    @Override
                    protected void onPostExecute(ApiResponse apiResponse) {
                        secondaryLatch.countDown();
                    }
                });

        secondaryLatch.await();
    }

    public void testMarkImojiUsage() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Imoji> imojiReference = new AtomicReference<>();

        sdkSession.fetchImojisByIdentifiers(Collections.singletonList(SAMPLE_IMOJI_ID))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        validateImojiResponse(imojisResponse);

                        imojiReference.set(imojisResponse.getImojis().iterator().next());
                        latch.countDown();
                    }
                });

        latch.await();

        final CountDownLatch secondaryLatch = new CountDownLatch(1);

        sdkSession.markImojiUsage(imojiReference.get(), "com.imoji.android.testing")
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ApiResponse>() {
                    @Override
                    protected void onPostExecute(ApiResponse apiResponse) {
                        secondaryLatch.countDown();
                    }
                });

        secondaryLatch.await();
    }

    private void validateImojiResponse(ImojisResponse imojisResponse) {
        assertNotNull(imojisResponse);
        assertNotNull(imojisResponse.getImojis());
        assertNotSame(imojisResponse.getImojis().size(), 0);

        Imoji first = imojisResponse.getImojis().iterator().next();

        assertNotNull(first);
        assertNotNull(first.getIdentifier());
        assertNotNull(first.getTags());
        assertNotNull(first.urlForRenderingOption(RenderingOptions.borderedPngThumbnail()));
    }
}
