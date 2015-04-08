package com.imojiapp.imoji.sdk;

import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by sajjadtabib on 4/6/15.
 */
interface ImojiApiInterface {
    /**
     * Note: you can pass a fieldmap to retrofit using @FieldMap Map<String, String> params
     */


    @GET("/featuredImojis")
    void getFeaturedImojis(
            @Query("apiKey") String apiKey,
            @Query("offset") int offset,
            @Query("numResults") String numResults,
            Callback<ImojiSearchResponse> cb);

    @GET("/featuredImojis")
    ImojiSearchResponse getFeaturedImojis(
            @Query("apiKey") String apiKey,
            @Query("offset") int offset,
            @Query("numResults") String numResults);

    @GET("/imojiSearch")
    void searchImojis(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("offset") int offset,
            @Query("numResults") String numResults,
            Callback<ImojiSearchResponse> cb);

    @GET("/imojiSearch")
    ImojiSearchResponse searchImojis(
            @Query("apiKey") String apiKey,
            @Query("query") String query,
            @Query("offset") int offset,
            @Query("numResults") String numResults);

    @GET("/imojis")
    FetchImojisResponse fetchImojis(@Query("ids") String imojiIds);

}
