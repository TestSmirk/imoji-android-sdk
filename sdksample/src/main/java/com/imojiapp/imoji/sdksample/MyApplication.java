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

        //local
//        ImojiApi.init(this, "2afbbed4-0995-4294-8c55-771f1337b443", "U2FsdGVkX1+r0w0pPBkatGtpM84tEMFy7i79ceJVCvBnveu1etlY0e4LPvJ8L9aKyTiKjDaYp+a1wfbg1VBaGA==", api);

        //production
        ImojiApi.init(this, "93c89ce0-d3ee-4697-bfd8-2e0e5fc72bb6", "U2FsdGVkX1+hI9aV1dXW1qY0gcjsbTrE53bPGbHTJ6JwByWThceSki5RiGTpy1e/IdBe/vU3qpZUTPZ34XPcAQ==", api);
    }
}
