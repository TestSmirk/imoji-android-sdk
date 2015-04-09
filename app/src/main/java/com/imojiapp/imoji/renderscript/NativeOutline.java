package com.imojiapp.imoji.renderscript;

import android.graphics.Bitmap;

/**
 * Created by thor on 30/01/15.
 */
public class NativeOutline {
    static {
        System.loadLibrary("outline");
    }

    public static native int perform(Bitmap bitmap, int outlineRadius, int outlineColor, int shadowRadius, int shadowOffsetX, int shadowOffsetY, int shadowColor);
}
