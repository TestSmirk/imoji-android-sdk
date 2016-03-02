package com.imojiapp.messaging;


import io.imoji.sdk.objects.Imoji;

/**
 * Created by sajjadtabib on 10/19/15.
 */
public interface MessageInterface {
    void addText(String message);

    void addImoji(Imoji img);
}
