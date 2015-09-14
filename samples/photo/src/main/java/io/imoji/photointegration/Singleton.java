package io.imoji.photointegration;

import android.os.AsyncTask;

/**
 * Created by sajjadtabib on 9/8/15.
 */
public class Singleton {

    public static void backgroundJob() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                return null;
            }
        }.execute();
    }
}
