package com.imojiapp.imoji.sdksample;

import android.app.Application;

import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.ImojiApi.Builder;

/**
 * Created by sajjadtabib on 4/17/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ImojiApi api = new Builder(this).build();
        ImojiApi.init(this, "93c89ce0-d3ee-4697-bfd8-2e0e5fc72bb6", "U2FsdGVkX1+hI9aV1dXW1qY0gcjsbTrE53bPGbHTJ6JwByWThceSki5RiGTpy1e/IdBe/vU3qpZUTPZ34XPcAQ==", api);
    }
}
