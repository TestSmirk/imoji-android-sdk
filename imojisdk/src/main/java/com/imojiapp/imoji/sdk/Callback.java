package com.imojiapp.imoji.sdk;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public interface Callback<T, V> {

    void onSuccess(T result);

    void onFailure(V result);
}
