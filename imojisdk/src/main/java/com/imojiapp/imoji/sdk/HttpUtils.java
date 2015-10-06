package com.imojiapp.imoji.sdk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by sajjadtabib on 10/2/15.
 */
public class HttpUtils {


    static HttpURLConnection get(String endpoint, Map<String, String> params, Map<String, String> headers) {
        try {
            StringBuilder sb = new StringBuilder(endpoint);
            String bodyParams = getUrlEncodedQueryString(params);
            if (!bodyParams.isEmpty()) {
                sb.append("?").append(bodyParams);
            }

            endpoint = sb.toString();


            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //set headers
            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }

            connection.setRequestMethod("GET");
            connection.setFixedLengthStreamingMode(0);
            connection.connect();

            return connection;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    static HttpURLConnection post(String endpoint, Map<String, String> params, Map<String, String> headers) {

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    connection.setRequestProperty(key, headers.get(key));
                }
            }

            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String postBody = getUrlEncodedQueryString(params);
            connection.setFixedLengthStreamingMode(postBody.getBytes().length);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(postBody, 0, postBody.length());
            writer.flush();
            writer.close();

            return connection;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static String getUrlEncodedQueryString(Map<String, String> bodyParams) throws UnsupportedEncodingException {

        if (bodyParams == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String key : bodyParams.keySet()) {
            String value = bodyParams.get(key);
            if (value != null) {
                sb.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(bodyParams.get(key), "UTF-8")).append("&");
            }
        }

        if (sb.length() > 0) { //remove last '&'
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }
}
