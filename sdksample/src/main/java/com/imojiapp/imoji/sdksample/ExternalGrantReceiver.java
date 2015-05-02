package com.imojiapp.imoji.sdksample;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sajjadtabib on 5/1/15.
 */
public class ExternalGrantReceiver extends com.imojiapp.imoji.sdk.ExternalGrantReceiver {
    private static final String LOG_TAG = ExternalGrantReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(LOG_TAG, "external grant receiver granted: " + mGranted);
    }
}
