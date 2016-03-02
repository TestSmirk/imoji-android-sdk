package io.imoji.photointegration;

import android.app.Application;

import io.imoji.sdk.ImojiSDK;

import java.util.UUID;

/**
 * Created by sajjadtabib on 9/2/15.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ImojiSDK.getInstance().setCredentials(
                UUID.fromString("748cddd4-460d-420a-bd42-fcba7f6c031b"),
                "U2FsdGVkX1/yhkvIVfvMcPCALxJ1VHzTt8FPZdp1vj7GIb+fsdzOjyafu9MZRveo7ebjx1+SKdLUvz8aM6woAw=="
        );

    }
}
