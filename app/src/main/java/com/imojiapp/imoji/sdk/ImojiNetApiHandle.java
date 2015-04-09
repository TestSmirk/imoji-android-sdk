package com.imojiapp.imoji.sdk;

import android.os.Build;
import android.util.Log;

import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sajjadtabib on 4/6/15.
 */
class ImojiNetApiHandle {
    private static final String LOG_TAG = ImojiNetApiHandle.class.getSimpleName();
    private static ImojiApiInterface sApiService;

    private static ImojiApiInterface get(){
        if(sApiService == null){

            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("x-client-version", "2.0.0"); //TODO: Fix
                    request.addHeader("x-client-model", "android");
                    request.addHeader("x-client-os-version", Build.VERSION.RELEASE);
                }
            };

            RestAdapter.Builder builder = new RestAdapter.Builder()
                    .setEndpoint(Config.BASE_URL)
                    .setRequestInterceptor(requestInterceptor);
            sApiService = builder.build().create(ImojiApiInterface.class);
        }


        return sApiService;
    }

    static void getFeaturedImojis(String apiToken, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>> callback){
        String count = null;
        if(numResults > 0){
            count = String.valueOf(numResults);
        }

        ImojiNetApiHandle.get().getFeaturedImojis(apiToken, offset, count, new Callback<ImojiSearchResponse>() {
            @Override
            public void success(ImojiSearchResponse imojiSearchResponse, Response response) {
                if (imojiSearchResponse.isSuccess()) {
                    List<Imoji> imojis = new ArrayList<>();
                    for (ImojiInternal imojiInternal : imojiSearchResponse.results) {
                        imojis.add(imojiInternal.getImoji());
                    }
                    callback.onSuccess(imojis);
                } else {
                    Log.d(LOG_TAG, "failure: " + imojiSearchResponse.status);
                    callback.onFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                callback.onFailure();
            }
        });
    }

    static List<Imoji> getFeaturedImojis(String apiToken, int offset, int numResults) {
        String count = null;
        if(numResults > 0){
            count = String.valueOf(numResults);
        }
        try {
            ImojiSearchResponse response = ImojiNetApiHandle.get().getFeaturedImojis(apiToken, offset, count);
            if (response != null && response.isSuccess()) {
                List<Imoji> imojis = new ArrayList<>();
                for (ImojiInternal imojiInternal : response.results) {
                    imojis.add(imojiInternal.getImoji());
                }
                return imojis;
            }
        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }



    static void searchImojis(String apiToken, String query, int offset, int numResults, final com.imojiapp.imoji.sdk.Callback<List<Imoji>> callback){

        String count = null;
        if(numResults > 0){
            count = String.valueOf(numResults);
        }
        ImojiNetApiHandle.get().searchImojis(apiToken, query, offset, count, new Callback<ImojiSearchResponse>() {
            @Override
            public void success(final ImojiSearchResponse imojiSearchResponse, Response response) {
                if (imojiSearchResponse.isSuccess()) {
                    List<Imoji> imojis = new ArrayList<>();
                    for (ImojiInternal i : imojiSearchResponse.results) {
                        imojis.add(i.getImoji());
                    }
                    callback.onSuccess(imojis);
                } else {
                    callback.onFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                callback.onFailure();
            }
        });
    }

    static List<Imoji> searchImojis(String apiToken, String query, int offset, int numResults){
        String count = null;
        if(numResults > 0){
            count = String.valueOf(numResults);
        }

        try {
            ImojiSearchResponse response = ImojiNetApiHandle.get().searchImojis(apiToken, query, offset, count);
            if (response != null && response.isSuccess()) {
                List<Imoji> imojis = new ArrayList<>();
                for (ImojiInternal imojiInternal : response.results) {
                    imojis.add(imojiInternal.getImoji());
                }
                return imojis;
            }

        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }

    static List<ImojiCategory> getImojiCategories(String apiToken) {
        try {
            GetCategoryResponse response = ImojiNetApiHandle.get().getImojiCategories(apiToken);
            if (response != null && response.isSuccess()) {
                return response.categories;
            }

        } catch (RetrofitError error) {
            error.printStackTrace();
        }

        return null;
    }

    static void getImojiCategories(String apiToken, final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>> cb) {
        ImojiNetApiHandle.get().getImojiCategories(apiToken, new Callback<GetCategoryResponse>() {
            @Override
            public void success(GetCategoryResponse getCategoryResponse, Response response) {
                try {
                    InputStream is = response.getBody().in();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (getCategoryResponse.isSuccess()) {
                    cb.onSuccess(getCategoryResponse.categories);
                } else {
                    cb.onFailure();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                cb.onFailure();
            }
        });
    }
}
