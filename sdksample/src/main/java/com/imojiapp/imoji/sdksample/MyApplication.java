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
        ImojiApi.init(this, "748cddd4-460d-420a-bd42-fcba7f6c031b", "U2FsdGVkX1/yhkvIVfvMcPCALxJ1VHzTt8FPZdp1vj7GIb+fsdzOjyafu9MZRveo7ebjx1+SKdLUvz8aM6woAw==", api);
    }
}
