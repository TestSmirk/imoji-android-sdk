package com.imojiapp.imoji.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sajjad on 5/2/15.
 */
public abstract class ExternalGrantReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = ExternalGrantReceiver.class.getSimpleName();

    protected boolean mGranted;
    private String mToken;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferenceManager.init(context);

        if (intent.hasExtra(ExternalIntents.BundleKeys.EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY)) {
            String receivedToken = intent.getStringExtra(ExternalIntents.BundleKeys.EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY);
            String savedToken = SharedPreferenceManager.getString(ImojiApi.PrefKeys.EXTERNAL_TOKEN, null);

            if (receivedToken != null && savedToken != null && receivedToken.equals(savedToken)) {
                //this is for us
                if (intent.hasExtra(ExternalIntents.BundleKeys.GRANTED)) {
                    mGranted = intent.getBooleanExtra(ExternalIntents.BundleKeys.GRANTED, false);
                }
            }
        }

        Log.d(LOG_TAG, "got broadcast with granted status: " + mGranted);

    }
}
