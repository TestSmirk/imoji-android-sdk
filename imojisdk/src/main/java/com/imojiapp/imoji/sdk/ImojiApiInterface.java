package com.imojiapp.imoji.sdk;

import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;

import java.util.List;

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


    @GET("/imoji/featured/fetch")
    void getFeaturedImojis(
            @Query("access_token") String accessToken,
            @Query("offset") int offset,
            @Query("numResults") String numResults,
            Callback<ImojiSearchResponse> cb);

    @GET("/imoji/featured/fetch")
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

    @GET("/imoji/fetchMultiple")
    FetchImojisResponse fetchImojis(
            @Query("ids") List<String> imojiIds);

    @GET("/imoji/categories/fetch")
    void getImojiCategories(
            @Query("access_token") String accessToken,
            Callback<GetCategoryResponse> cb);

    @GET("/imoji/categories/fetch")
    GetCategoryResponse getImojiCategories(
            @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("/oauth/token")
    GetAuthTokenResponse getAuthToken(@Header("Authorization") String authorizationHeader,
                                      @Field("grant_type") String grantType, @Field("refresh_token") String refreshToken);

    @FormUrlEncoded
    @POST("/oauth/external/getIdPayload")
    void requestExternalOauth(@Field("access_token") String accessToken,
                              @Field("clientId") String clientId, Callback<ExternalOauthPayloadResponse> cb);


}
