package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by sajjadtabib on 4/28/15.
 */
class Utils {

    private static Gson sGson;
    static Gson gson() {
        if (sGson == null) {
            synchronized (Gson.class) {
                if (sGson == null) {
                    sGson = new Gson();
                }
            }
        }
        return  sGson;
    }

    static Intent getPlayStoreIntent(String referrer) {
        Intent playStoreIntent = new Intent();
        playStoreIntent.setAction(Intent.ACTION_VIEW);
        playStoreIntent.setData(Uri.parse("market://details?id=" + Config.IMOJI_APP_PACKAGE + "&referrer=" + referrer));
        return playStoreIntent;
    }

    static boolean isImojiAppInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent.setAction(ExternalIntents.Actions.INTENT_CREATE_IMOJI_ACTION);
        intent.putExtra(ExternalIntents.BundleKeys.LANDING_PAGE_BUNDLE_ARG_KEY, ExternalIntents.BundleValues.CAMERA_PAGE);
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);

        if (resolveInfo == null) {
            return false;
        }else {
            return true;
        }
    }

    static boolean canHandleUserOauth(Context context) {
        Intent intent = new Intent();
        intent.setAction(ExternalIntents.Actions.INTENT_REQUEST_ACCESS);
        intent.addCategory(ExternalIntents.Categories.EXTERNAL_CATEGORY);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryBroadcastReceivers(intent, 0);
        if (resolveInfoList != null && !resolveInfoList.isEmpty()) {
            for (ResolveInfo info : resolveInfoList) {
                if (info.activityInfo != null && info.activityInfo.packageName.contains(Config.IMOJI_APP_PACKAGE)) {
                    return true;
                }
            }
        }

        return false;
    }
}
