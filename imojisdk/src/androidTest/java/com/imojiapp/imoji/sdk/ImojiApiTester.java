package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
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
//        ImojiApi api = new ImojiApi.Builder(getContext()).build();
//        ImojiApi.init(getContext(), "93c89ce0-d3ee-4697-bfd8-2e0e5fc72bb6", "U2FsdGVkX1+hI9aV1dXW1qY0gcjsbTrE53bPGbHTJ6JwByWThceSki5RiGTpy1e/IdBe/vU3qpZUTPZ34XPcAQ==", api);
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

        mApi.getFeatured(off, num, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(num, result.size());
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
        mApi.getFeatured(new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(60, result.size());
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
        mApi.search(query, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(60, result.size());
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
        mApi.search(query, off, num, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertNotNull(result);
                assertEquals(num, result.size());
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

        mApi.getImojiCategories(new Callback<List<ImojiCategory>, String>() {
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

    public void testFetchMultiple(){
        final List<Imoji> imojis = mApi.getFeatured();
        List<String> ids = new ArrayList<>();
        for (Imoji i : imojis) {
            ids.add(i.getImojiId());
        }


        mApi.getImojisById(ids, new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                assertEquals(imojis.size(), result.size());
                latch.countDown();
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


}