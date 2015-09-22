package com.imojiapp.imoji.sdk;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Created by sajjadtabib on 9/22/15.
 */
public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    static boolean upload(URL url, byte[] data, Map<String, String> requestProperties) {

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            if (requestProperties != null) {
                for (String key : requestProperties.keySet()) {
                    connection.setRequestProperty(key, requestProperties.get(key));
                }
            }

            OutputStream out = connection.getOutputStream();
            out.write(data, 0, data.length);
            out.close();

            int responseCode = connection.getResponseCode();

            Log.d(LOG_TAG, "Response code " + responseCode);

            return true; //only if response code was success

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
