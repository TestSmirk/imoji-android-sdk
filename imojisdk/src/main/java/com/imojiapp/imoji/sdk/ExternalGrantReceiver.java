package com.imojiapp.imoji.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sajjad on 5/2/15.
 */
public abstract class ExternalGrantReceiver extends BroadcastReceiver {
    protected boolean mGranted;
    protected boolean mForUs;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferenceManager.init(context);
        mGranted = false;
        mForUs = false;

        if (intent.hasExtra(ExternalIntents.BundleKeys.EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY)) {
            String receivedToken = intent.getStringExtra(ExternalIntents.BundleKeys.EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY);
            String savedToken = SharedPreferenceManager.getString(ImojiApi.PrefKeys.EXTERNAL_TOKEN, null);

            if (receivedToken != null && savedToken != null && receivedToken.equals(savedToken)) {
                mForUs = true;
                //this is for us
                if (intent.hasExtra(ExternalIntents.BundleKeys.GRANTED)) {
                    mGranted = intent.getBooleanExtra(ExternalIntents.BundleKeys.GRANTED, false);
                }
            }
        }
    }
}