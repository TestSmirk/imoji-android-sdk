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

package io.imoji.sdk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.util.Base64;

import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import io.imoji.sdk.objects.Artist;
import io.imoji.sdk.objects.Category;
import io.imoji.sdk.objects.CategoryFetchOptions;
import io.imoji.sdk.objects.CollectionType;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.response.CategoriesResponse;
import io.imoji.sdk.response.CreateImojiResponse;
import io.imoji.sdk.response.GenericApiResponse;
import io.imoji.sdk.response.ImojiAttributionsResponse;
import io.imoji.sdk.response.ImojisResponse;

/**
 * Imoji Android SDK
 * <p/>
 * Created by nkhoshini on 2/26/16.
 */
public class BaseTests extends AndroidTestCase {

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
        sdkSession.getImojiCategories(new CategoryFetchOptions(Category.Classification.Artist)).executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
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

                Imoji previewImoji = category.getPreviewImoji();
                assertNotNull(previewImoji.getIdentifier());
                assertNotNull(previewImoji.getTags());
                assertNotNull(previewImoji.getStandardThumbnailUri());

                Artist artist = category.getAttribution().getArtist();
                assertNotNull(artist.getIdentifier());
                assertNotNull(artist.getDescription());
                assertNotNull(artist.getName());
                assertNotNull(artist.getProfileImoji());
                assertNotNull(artist.getProfileImoji().getIdentifier());
                assertNotNull(artist.getProfileImoji().getStandardThumbnailUri());

                latch.countDown();

            }
        });

        latch.await();
    }


    public void testContextualCategoryFetch() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        sdkSession.getImojiCategories(new CategoryFetchOptions(Category.Classification.Trending, "Taylor swift is the best!", Locale.US))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
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

                        Imoji previewImoji = category.getPreviewImoji();
                        assertNotNull(previewImoji.getIdentifier());
                        assertNotNull(previewImoji.getTags());
                        assertNotNull(previewImoji.getStandardThumbnailUri());

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

        sdkSession.reportImojiAsAbusive(imojiReference.get().getIdentifier(), "Android Testing")
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<GenericApiResponse>() {
                    @Override
                    protected void onPostExecute(GenericApiResponse apiResponse) {
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

        sdkSession.markImojiUsage(imojiReference.get().getIdentifier(), "com.imoji.android.testing")
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<GenericApiResponse>() {
                    @Override
                    protected void onPostExecute(GenericApiResponse apiResponse) {
                        secondaryLatch.countDown();
                    }
                });

        secondaryLatch.await();
    }

    public void testImojiAttribution() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        sdkSession.fetchAttributionByImojiIdentifiers(Collections.singletonList(SAMPLE_IMOJI_ID))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojiAttributionsResponse>() {
                    @Override
                    protected void onPostExecute(ImojiAttributionsResponse attributionResponse) {
                        assertNotNull(attributionResponse);
                        assertNotNull(attributionResponse.getAttributionMap());
                        assertNotSame(attributionResponse.getAttributionMap().size(), 0);

                        Category.Attribution attribution =
                                attributionResponse.getAttributionMap().values().iterator().next();

                        assertNotNull(attribution);
                        assertNotNull(attribution.getRelatedTags());

                        latch.countDown();
                    }
                });

        latch.await();
    }

    public void testImojiAsParcelable() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        sdkSession
                .fetchImojisByIdentifiers(Collections.singletonList(SAMPLE_IMOJI_ID))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        validateImojiResponse(imojisResponse);
                        Imoji imoji = imojisResponse.getImojis().iterator().next();

                        Parcel parcel = Parcel.obtain();

                        imoji.writeToParcel(parcel, 0);
                        parcel.setDataPosition(0);
                        Imoji fromParcel = Imoji.CREATOR.createFromParcel(parcel);

                        assertEquals(fromParcel, imoji);

                        latch.countDown();
                    }
                });

        latch.await();
    }

    public void testEmptyTaggedImojiAsParcelable() throws Exception {
        Imoji imoji = new Imoji(UUID.randomUUID().toString(),
                Collections.<String>emptyList(),
                Collections.<RenderingOptions, Imoji.Metadata>emptyMap(),
                Imoji.LicenseStyle.NonCommercial
        );

        Parcel parcel = Parcel.obtain();

        imoji.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Imoji fromParcel = Imoji.CREATOR.createFromParcel(parcel);

        assertEquals(fromParcel, imoji);
    }

    public void testCategoriesWithLicenseStyles() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        CategoryFetchOptions fetchOptions = new CategoryFetchOptions(Category.Classification.Artist);
        fetchOptions.setLicenseStyles(Collections.singletonList(Imoji.LicenseStyle.CommercialPrint));


        sdkSession.getImojiCategories(fetchOptions).executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
            @Override
            protected void onPostExecute(CategoriesResponse categoriesResponse) {
                assertNotNull(categoriesResponse);
                assertNotNull(categoriesResponse.getCategories());
                assertNotSame(categoriesResponse.getCategories().size(), 0);

                Category category = categoriesResponse.getCategories().iterator().next();

                assertNotNull(category);
                assertNotNull(category.getIdentifier());
                assertNotNull(category.getAttribution());
                assertEquals(category.getAttribution().getLicenseStyle(), Imoji.LicenseStyle.CommercialPrint);

                latch.countDown();

            }
        });

        latch.await();
    }

    public void testCollections() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        sdkSession.addImojiToUserCollection(SAMPLE_IMOJI_ID).executeAsyncTask(
                new ApiTask.WrappedAsyncTask<GenericApiResponse>() {
                    @Override
                    protected void onPostExecute(GenericApiResponse genericApiResponse) {
                        latch.countDown();
                    }

                    @Override
                    protected void onError(@NonNull Throwable error) {
                        throw new RuntimeException(error);
                    }
                }
        );

        latch.await();
        final CountDownLatch secondLatch = new CountDownLatch(1);

        sdkSession.getCollectedImojis(CollectionType.Liked).executeAsyncTask(
                new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        validateImojiResponse(imojisResponse);
                        secondLatch.countDown();
                    }
                }
        );

        secondLatch.await();
    }

    public void testCreate() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Imoji> imojiReference = new AtomicReference<>(null);
        byte[] imageBytes = Base64.decode(Resources.TEST_IMAGE, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        Assert.assertNotNull(bitmap);

        sdkSession
                .createImojiWithRawImage(bitmap, bitmap, Collections.<String>emptyList())
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CreateImojiResponse>() {
                                      @Override
                                      protected void onPostExecute(CreateImojiResponse createImojiResponse) {
                                          Assert.assertNotNull(createImojiResponse);
                                          Assert.assertNotNull(createImojiResponse.getImoji());
                                          Assert.assertNotNull(createImojiResponse.getImoji().getIdentifier());

                                          imojiReference.set(createImojiResponse.getImoji());
                                          latch.countDown();
                                      }
                                  }
                );

        latch.await();
        Imoji imoji = imojiReference.get();
        Assert.assertNotNull(imoji);

        URL url = new URL(imoji.getStandardFullSizeUri().toString());
        Assert.assertNotNull(downloadDataFromUrl(url));

        final CountDownLatch secondLatch = new CountDownLatch(1);

        sdkSession.removeImoji(imoji).executeAsyncTask(new ApiTask.WrappedAsyncTask<GenericApiResponse>() {
            @Override
            protected void onPostExecute(GenericApiResponse genericApiResponse) {
                Assert.assertNotNull(genericApiResponse);
                secondLatch.countDown();
            }
        });

        secondLatch.await();
    }

    private void validateImojiResponse(ImojisResponse imojisResponse) {
        assertNotNull(imojisResponse);
        assertNotNull(imojisResponse.getImojis());
        assertNotSame(imojisResponse.getImojis().size(), 0);

        Imoji first = imojisResponse.getImojis().iterator().next();

        assertNotNull(first);
        assertNotNull(first.getIdentifier());
        assertNotNull(first.getTags());
        assertNotNull(first.getStandardThumbnailUri());
    }

    private static char[] downloadDataFromUrl(@NonNull URL url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            boolean succeeded = connection.getResponseCode() == HttpURLConnection.HTTP_OK ||
                    connection.getResponseCode() == HttpURLConnection.HTTP_CREATED ||
                    connection.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED;
            BufferedReader inputReader;
            if (succeeded) {
                inputReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                throw new IOException("Unable to download image, received status of " + connection.getResponseCode());
            }

            CharArrayWriter byteArrayOutputStream = new CharArrayWriter();
            char [] buf = new char[4096];
            int read, offset = 0;
            while ((read = inputReader.read(buf, offset, 4096)) != -1) {
                byteArrayOutputStream.write(buf, offset, read);
            }

            return buf;

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }
}
