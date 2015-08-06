package com.imojiapp.imoji.sdksample;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.test.UiThreadTest;
import android.util.Log;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Handler;

/**
 * Created by sajjadtabib on 4/8/15.
 */
public class ImojiApiTester extends AndroidTestCase {

    String query;
    int off;
    int num;
    CountDownLatch latch;
    boolean finished;


    @Override
    protected void setUp() throws Exception {
        Log.d("api test", "setting up");
        query = "hi";
        off = 0;
        num = 17;
        latch = new CountDownLatch(1);
        finished = false;

    }

//    /**
//     * test get featured synchronously with offset and num results
//     *
//     */
//    public void testGetFeaturedWithOffNumSync() {
////        (int offset, int numResults
//
//        List<Imoji> imojis = mApi.getFeatured(off, num);
//        assertNotNull(imojis);
//        assertEquals(num, imojis.size());
//
//        off = 1;
//        num = 15;
//        imojis = mApi.getFeatured(off, num);
//        assertNotNull(imojis);
//        assertEquals(num, imojis.size());
//    }

//    /**
//     * get featured synchronously with default
//     */
//    public void testGetFeaturedWithDefaultSync() {
//        List<Imoji> imojis = mApi.getFeatured();
//        assertNotNull(imojis);
//        assertEquals(60, imojis.size());
//
//    }


//    public void testSearchWithDefaultSync() {
//        List<Imoji> imojis = mApi.search(query);
//        assertNotNull(imojis);
//        assertEquals(60, imojis.size());
//    }
//
//
//    public void testSearchWithOffNumSync() {
//        List<Imoji> imojis = mApi.search(query, off, num);
//        assertNotNull(imojis);
//        assertEquals(num, imojis.size());
//
//        off = 2;
//        imojis = mApi.search(query, off, num);
//        assertNotNull(imojis);
//        assertEquals(num, imojis.size());
//
//    }


    public void testGetFeaturedWithOffNumAsync() {

        ImojiApi.with(getContext()).getFeatured(off, num, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(true, result.size() > 0);
                latch.countDown();
            }

            @Override
            public void onFailure(String status) {
                fail();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void testGetFeaturedDefaultAsync() {
        ImojiApi.with(getContext()).getFeatured(new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(true, result.size() > 0);
                latch.countDown();
            }

            @Override
            public void onFailure(String status) {
                latch.countDown();
                fail();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void testSearchDefaultAsync() {
        ImojiApi.with(getContext()).search(query, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(true, result.size() > 0);
                latch.countDown();
            }

            @Override
            public void onFailure(String status) {
                fail();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void testSearchOffNumAsync() {
        ImojiApi.with(getContext()).search(query, off, num, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(true, result.size() > 0);
                latch.countDown();
            }

            @Override
            public void onFailure(String status) {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void testGetImojiCategoriesAsync() {

        ImojiApi.with(getContext()).getImojiCategories(new Callback<List<ImojiCategory>, String>() {
            @Override
            public void onSuccess(List<ImojiCategory> result) {
                assertNotNull(result);
                latch.countDown();
            }

            @Override
            public void onFailure(String status) {
                fail();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    public void testGetImojiCategoriesSync() {
//        List<ImojiCategory> categories = ImojiApi.with(getContext()).getImojiCategories();
//        assertNotNull(categories);
//        assertTrue(categories.size() > 0);
//
//    }

    public void testLoadThumb() {

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap b = bitmap;
                assertNotNull(b);
                assertTrue(b.getWidth() > 0);
                assertTrue(b.getHeight() > 0);
                latch.countDown();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                fail();
                latch.countDown();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        ImojiApi.with(getContext()).getFeatured(new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                Imoji i = result.get(0);
            }

            @Override
            public void onFailure(String result) {
                fail();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void testLoadFull() {

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap b = bitmap;
                assertNotNull(b);
                assertTrue(b.getWidth() > 0);
                assertTrue(b.getHeight() > 0);
                latch.countDown();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                fail();
                latch.countDown();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        ImojiApi.with(getContext()).getFeatured(new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                Log.d("test", "on success thread id: " + Thread.currentThread().getId());
                Imoji i = result.get(0);
            }

            @Override
            public void onFailure(String result) {
                fail();
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testFetchMultipleAsync() {

        ImojiApi.with(getContext()).getFeatured(new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                final List<Imoji> imojis = result;
                List<String> ids = new ArrayList<>();
                for (Imoji i : imojis) {
                    ids.add(i.getImojiId());
                }
                Log.d("test id: ", "test id: " + Thread.currentThread().getId());
                Log.d("test fetch multiple", "got " + ids.size());
                ImojiApi.with(getContext()).getImojisById(ids, new Callback<List<Imoji>, String>() {
                    @Override
                    public void onSuccess(List<Imoji> result) {
                        assertEquals(imojis.size(), result.size());
                        latch.countDown();
                    }

                    @Override
                    public void onFailure(String result) {
                        fail("failed to fetch imojis");
                        latch.countDown();
                    }
                });
            }

            @Override
            public void onFailure(String result) {
                fail("Failed to fetch featured");
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
