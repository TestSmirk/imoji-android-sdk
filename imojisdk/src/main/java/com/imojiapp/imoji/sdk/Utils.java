package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * Created by sajjadtabib on 4/28/15.
 */
class Utils {

    static Intent getPlayStoreIntent(String referrer) {
        Intent playStoreIntent = new Intent();
        playStoreIntent.setAction(Intent.ACTION_VIEW);
        playStoreIntent.setData(Uri.parse("market://details?id=" + Constants.IMOJI_APP_PACKAGE + "&referrer=" + referrer));
        return playStoreIntent;
    }

    static boolean isImojiAppInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setAction(ImojiAppConstants.IntentActions.INTENT_CREATE_IMOJI_ACTION);
        intent.putExtra(ImojiAppConstants.BundleKeys.LANDING_PAGE_BUNDLE_ARG_KEY, ImojiAppConstants.BundleValues.CAMERA_PAGE);
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);

        if (resolveInfo == null) {
            return false;
        }else {
            return true;
        }
    }
}
