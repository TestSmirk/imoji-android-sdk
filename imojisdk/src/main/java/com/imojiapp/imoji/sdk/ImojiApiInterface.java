package com.imojiapp.imoji.sdk;

import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by sajjadtabib on 4/6/15.
 */
interface ImojiApiInterface {
    /**
     * Note: you can pass a fieldmap to retrofit using @FieldMap Map<String, String> params
     */


    @GET("/imoji/featured")
    void getFeaturedImojis(
            @Query("access_token") String accessToken,
            @Query("offset") int offset,
            @Query("numResults") String numResults,
            Callback<ImojiSearchResponse> cb);

    @GET("/imoji/featured")
    ImojiSearchResponse getFeaturedImojis(
            @Query("access_token") String accessToken,
            @Query("offset") int offset,
            @Query("numResults") String numResults);

    @GET("/imoji/search")
    void searchImojis(
            @Query("access_token") String accessToken,
            @Query("query") String query,
            @Query("offset") int offset,
            @Query("numResults") String numResults,
            Callback<ImojiSearchResponse> cb);

    @GET("/imoji/search")
    ImojiSearchResponse searchImojis(
            @Query("access_token") String accessToken,
            @Query("query") String query,
            @Query("offset") int offset,
            @Query("numResults") String numResults);

    @GET("/imojis")
    FetchImojisResponse fetchImojis(
            @Query("ids") String imojiIds);

    @GET("/imoji/categories")
    void getImojiCategories(
            @Query("access_token") String accessToken,
            Callback<GetCategoryResponse> cb);

    @GET("/imoji/categories")
    GetCategoryResponse getImojiCategories(
            @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("/token")
    GetAuthTokenResponse getAuthToken(@Header("Authorization") String authorizationHeader,
                                      @Field("grant_type") String grantType, @Field("refresh_token") String refreshToken);



}
