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

import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import io.imoji.sdk.response.ImojisResponse;

/**
 * Imoji Android SDK
 * <p/>
 * Created by nkhoshini on 2/26/16.
 */
public class ErrorTests extends AndroidTestCase {

    private static final UUID CLIENT_ID = UUID.fromString("748cddd4-460d-420a-bd42-fcba7f6c031b");
    private static final String API_TOKEN = "U2FsdGVkX1/yhkvIVfvMcPCALxJ1VHzTt8FPZdp1vj7GIb+fsdzOjyafu9MZRveo7ebjx1+SKdLUvz8aM6woAw==";
    private static final String SAMPLE_IMOJI_ID = "ac6e038f-3392-46a6-a1fb-573cd2fea1cb";

    @SuppressWarnings("FieldCanBeLocal")
    private Session sdkSession;

    protected void setUp() throws Exception {
        sdkSession = ImojiSDK.getInstance()
                .setCredentials(CLIENT_ID, API_TOKEN)
                .createSession(getContext());
    }

    public void testErrorCondition() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        ImojiSDK.getInstance().setCredentials(
                UUID.randomUUID(), "912837491237492384"
        );

        try {
            ImojiSDK.getInstance()
                    .createSession(getContext())
                    .searchImojisWithSentence("hi there!!")
                    .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                        @Override
                        protected void onPostExecute(ImojisResponse imojisResponse) {
                            // should not get here
                            assertFalse(true);
                        }

                        @Override
                        protected void onError(@NonNull Throwable error) {
                            latch.countDown();
                            super.onError(error);
                        }
                    });
        } catch (Exception e) {
            Log.d(BaseTests.class.getName(), "testErrorCondition: ");
        }

        latch.await();

        // reset client id for next set of tests
        ImojiSDK.getInstance().setCredentials(
                CLIENT_ID, API_TOKEN
        );
    }

}
