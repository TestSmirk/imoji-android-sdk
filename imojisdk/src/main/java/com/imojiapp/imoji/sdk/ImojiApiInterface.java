package com.imojiapp.imoji.sdk;

import com.imojiapp.imoji.sdk.networking.responses.AddImojiToCollectionResponse;
import com.imojiapp.imoji.sdk.networking.responses.CreateImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetCategoryResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetUserImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiAckResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiSearchResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;

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
    void searchImojis(
            @QueryMap Map<String, String> params,
            Callback<ImojiSearchResponse> cb);

    @FormUrlEncoded
    @POST("/imoji/fetchMultiple")
    void fetchImojis(
            @Field("access_token") String accessToken,
            @Field("ids") String imojiIds,
            Callback<FetchImojisResponse> cb);

    @GET("/imoji/search")
    ImojiSearchResponse searchImojis(
            @Query("access_token") String accessToken,
            @Query("query") String query,
            @Query("offset") int offset,
            @Query("numResults") String numResults);


    @GET("/imoji/categories/fetch")
    void getImojiCategories(
            @Query("access_token") String accessToken,
            @Query("classification") String classification,
            Callback<GetCategoryResponse> cb);

    @GET("/imoji/categories/fetch")
    GetCategoryResponse getImojiCategories(
            @Query("access_token") String accessToken);

    @GET("/user/imoji/fetch")
    GetUserImojiResponse getUserImojis(
            @Query("access_token") String accessToken);

    @GET("/user/imoji/fetch")
    void getUserImojis(
            @Query("access_token") String accessToken,
            Callback<GetUserImojiResponse> cb);

    @FormUrlEncoded
    @POST("/user/imoji/collection/add")
    void addImojiToUserCollection(
            @Field("access_token") String accessToken,
            @Field("imojiId") String imojiId,
            Callback<AddImojiToCollectionResponse> cb);

    @FormUrlEncoded
    @POST("/oauth/token")
    GetAuthTokenResponse getAuthToken(
            @Header("Authorization") String authorizationHeader,
            @Field("grant_type") String grantType,
            @Field("refresh_token") String refreshToken);

    @FormUrlEncoded
    @POST("/oauth/external/getIdPayload")
    void requestExternalOauth(
            @Field("access_token") String accessToken,
            @Field("clientId") String clientId,
            Callback<ExternalOauthPayloadResponse> cb);


    @FormUrlEncoded
    @POST("/imoji/create")
    CreateImojiResponse createImoji(@Field("access_token") String access_token,
                                    @Field("tags") String tags);


    @FormUrlEncoded
    @POST("/imoji/ackImageUpload")
    ImojiAckResponse ackImojiImage(@Field("access_token") String access_token,
                                   @Field("imojiId") String imojiId,
                                   @Field("hasFullImage") int hasFullImage,
                                   @Field("hasThumbnailImage") int hasThumb);


}
