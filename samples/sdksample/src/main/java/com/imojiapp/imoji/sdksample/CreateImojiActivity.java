package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.response.CreateImojiResponse;

import java.util.Collections;
import java.util.List;

/**
 * Created by sajjadtabib on 9/22/15.
 */
public class CreateImojiActivity extends Activity {

private static final String LOG_TAG = CreateImojiActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.sample)).getBitmap();
        List<String> tags = Collections.singletonList("Let's do it!");

        ImojiSDK.getInstance()
                .createSession(getApplicationContext())
                .createImojiWithRawImage(
                        bitmap, bitmap, tags
                )
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CreateImojiResponse>() {
                    @Override
                    protected void onPostExecute(CreateImojiResponse createImojiResponse) {
                        Log.d(LOG_TAG, "sweet, got an imoji");
                        Imoji imoji = createImojiResponse.getImoji();
                        Log.d(LOG_TAG, "got imoji: " + imoji.getIdentifier() + "download URL is: " + imoji.getStandardThumbnailUri());
                    }
                });
    }
}
