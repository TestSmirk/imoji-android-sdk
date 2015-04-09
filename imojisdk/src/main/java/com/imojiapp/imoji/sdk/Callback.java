package com.imojiapp.imoji.sdk;

import retrofit.client.Response;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public interface Callback<T> {

    void onSuccess(T result);

    void onFailure();
}
