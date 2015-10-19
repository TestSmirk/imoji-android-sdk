package com.imojiapp.triggers;

import android.graphics.Bitmap;

import com.imojiapp.imoji.sdk.Imoji;

/**
 * Created by sajjadtabib on 10/19/15.
 */
public interface MessageInterface {
    void addText(String message);

    void addImoji(Imoji img);
}
