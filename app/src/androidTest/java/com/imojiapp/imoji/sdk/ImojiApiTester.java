package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sajjadtabib on 4/8/15.
 */
public class ImojiApiTester extends AndroidTestCase {

    ImojiApi mApi;
    String query;
    int off;
    int num;
    CountDownLatch latch;

    @Override
    protected void setUp() throws Exception {

        ImojiApi.init(getContext(), "test_api_key");
        mApi = ImojiApi.with(getContext());
        query = "hi";
        off = 0;
        num = 17;
        latch   = new CountDownLatch(1);

    }

    /**
     * test get featured synchronously with offset and num results
     *
     */
    public void testGetFeaturedWithOffNumSync() {
//        (int offset, int numResults

        List<Imoji> imojis = mApi.getFeatured(off, num);
        assertNotNull(imojis);
        assertEquals(num, imojis.size());

        off = 1;
        num = 15;
        imojis = mApi.getFeatured(off, num);
        assertNotNull(imojis);
        assertEquals(num, imojis.size());
    }

    /**
     * get featured synchronously with default
     */
    public void testGetFeaturedWithDefaultSync() {
        List<Imoji> imojis = mApi.getFeatured();
        assertNotNull(imojis);
        assertEquals(60, imojis.size());

    }

    
    public void testSearchWithDefaultSync() {
        List<Imoji> imojis = mApi.search(query);
        assertNotNull(imojis);
        assertEquals(60, imojis.size());
    }

    
    public void testSearchWithOffNumSync() {
        List<Imoji> imojis = mApi.search(query, off, num);
        assertNotNull(imojis);
        assertEquals(num, imojis.size());

        off = 2;
        imojis = mApi.search(query, off, num);
        assertNotNull(imojis);
        assertEquals(num, imojis.size());

    }

    
    public void testGetFeaturedWithOffNumAsync() {


        mApi.getFeatured(off, num, new Callback<List<Imoji>>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(num, result.size());
                latch.countDown();
            }

            @Override
            public void onFailure() {
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
        mApi.getFeatured(new Callback<List<Imoji>>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(60, result.size());
                latch.countDown();
            }

            @Override
            public void onFailure() {
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
        mApi.search(query, new Callback<List<Imoji>>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(60, result.size());
                latch.countDown();
            }

            @Override
            public void onFailure() {
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
        mApi.search(query, off, num, new Callback<List<Imoji>>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(num, result.size());
                latch.countDown();
            }

            @Override
            public void onFailure() {
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

        mApi.getImojiCategories(new Callback<List<ImojiCategory>>() {
            @Override
            public void onSuccess(List<ImojiCategory> result) {
                assertNotNull(result);
                latch.countDown();
            }

            @Override
            public void onFailure() {
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

    
    public void testGetImojiCategoriesSync() {
        List<ImojiCategory> categories = mApi.getImojiCategories();
        assertNotNull(categories);
        assertTrue(categories.size() > 0);
        
    }
    
    public void testLoadThumb() {
        List<Imoji> imojis = mApi.getFeatured();
        Imoji i = imojis.get(0);
        try {
            Bitmap b = mApi.loadThumb(i, null).get();
            assertNotNull(b);
            assertTrue(b.getWidth() > 0);
            assertTrue(b.getHeight() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
    public void testLoadFull() {
        List<Imoji> imojis = mApi.getFeatured();
        Imoji i = imojis.get(0);
        try {
            Bitmap b = mApi.loadFull(i, null).get();
            assertNotNull(b);
            assertTrue(b.getWidth() > 0);
            assertTrue(b.getHeight() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
