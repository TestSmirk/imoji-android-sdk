package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by sajjadtabib on 4/29/15.
 */
class SharedPreferenceManager {
    private static SharedPreferences sPrefs;

    static final String PREF_FILE = "imoji-store";

    static void init(Context context) {
        sPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    static void putString(String key, String value) {
        sPrefs.edit().putString(key, value).apply();
    }

    static String getString(String key, String defaultValue) {
        return sPrefs.getString(key, defaultValue);
    }

    static void putInt(String key, int value) {
        sPrefs.edit().putInt(key, value).apply();
    }

    static int getInt(String key, int defaultValue) {
        return sPrefs.getInt(key, defaultValue);
    }

    static void putLong(String key, long value){ sPrefs.edit().putLong(key, value).apply(); }

    static long getLong(String key, long defaultValue) {
        return sPrefs.getLong(key, defaultValue);
    }

    static void putStringSet(String key, Set<String> set) {
        sPrefs.edit().putStringSet(key, set).apply();
    }

    static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return sPrefs.getStringSet(key, defaultValue);
    }

}
