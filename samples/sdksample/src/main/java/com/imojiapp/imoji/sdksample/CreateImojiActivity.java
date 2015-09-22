package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;

import java.util.Arrays;
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
        List<String> tags = Arrays.asList(new String[]{"Let's do it!"});
        ImojiApi.with(this).createImoji(bitmap, tags, new Callback<Imoji, String>() {
            @Override
            public void onSuccess(Imoji result) {
                Log.d(LOG_TAG, "sweet, got an imoji");
                Log.d(LOG_TAG, "got imoji: " + result.getImojiId());
            }

            @Override
            public void onFailure(String result) {
                Log.d(LOG_TAG, "damn, :...(");

            }
        });
    }
}
