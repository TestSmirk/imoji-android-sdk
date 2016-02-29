/*
 * Imoji Android SDK
 * Created by nkhoshini
 *
 * Copyright (C) 2016 Imoji
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KID, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 */

package com.imoji.sdk;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.imoji.sdk.objects.Category;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.CategoriesResponse;
import com.imoji.sdk.response.CreateImojiResponse;
import com.imoji.sdk.response.ImojisResponse;
import com.imoji.sdk.response.ApiResponse;
import com.imoji.sdk.response.RenderResponse;

import java.util.List;
import java.util.concurrent.Future;

public interface Session {

    /**
     * Fetches top level imoji categories given a classification type.
     * @param classification Type of category classification to retrieve
     * @return An operation reference that can be used to cancel the request.
     */
    @NonNull
    Future<CategoriesResponse> getImojiCategories(@NonNull Category.Classification classification);

    /**
     * Searches the imojis database with a given search term. The resultSetResponseCallback block is called once the results are available.
     * Imoji contents are downloaded individually and imojiResponseCallback is called once the thumbnail of that imoji has been downloaded.
     * @param term Search term to find imojis with. If nil or empty, the server will typically returned the featured set of imojis (this is subject to change).
     * @param offset The result offset from a previous search. This may be nil.
     * @param numberOfResults Number of results to fetch. This can be nil.
     */
    @NonNull
    Future<ImojisResponse> searchImojis(@NonNull String term, @Nullable Integer offset, @Nullable Integer numberOfResults);

    /**
     * Gets a random set of featured imojis. The resultSetResponseCallback block is called once the results are available.
     * Imoji contents are downloaded individually and imojiResponseCallback is called once the thumbnail of that imoji has been downloaded.
     * @param numberOfResults Number of results to fetch. This can be nil.
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<ImojisResponse> getFeaturedImojis(@Nullable Integer numberOfResults);

    /**
     * Gets corresponding Imoji's for one or more imoji identifiers as NSString's
     * Imoji contents are downloaded individually and fetchedResponseCallback is called once the thumbnail of that imoji has been downloaded.
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<ImojisResponse> fetchImojisByIdentifiers(@NonNull List<String> identifiers);


    /**
     * Searches the imojis database with a complete sentence. The service performs keyword parsing to find best matched imojis.
     * @param sentence Full sentence to parse.
     * @param numberOfResults Number of results to fetch. This can be nil.
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<ImojisResponse> searchImojisWithSentence(@NonNull String sentence, @Nullable Integer numberOfResults);


    /**
     * Renders an imoji object into a image with the specified rendering options.
     * The imoji image is scaled to fit the specified target size. This may make a server call depending on the availability.
     * of the imoji with the session storage policy.
     * @param imoji The imoji to render.
     * @param options Set of options to render the imoji with.
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<RenderResponse> renderImoji(@NonNull Imoji imoji, @NonNull RenderingOptions options);

    /**
     * Gets imojis associated to the synchronized user account. 
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<ImojisResponse> getImojisForAuthenticatedUser();

    /**
     * Adds a given Imoji to a users collection which is also synchronized with their account.
     * The sessionState must be IMImojiSessionStateConnectedSynchronized in order to receive user imojis.
     * @param imoji The Imoji object to save to the users collection
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<ApiResponse> addImojiToUserCollection(@NonNull Imoji imoji);


    /**
     * Adds an Imoji sticker to the database
     * @param rawImage The Imoji sticker image
     * @param borderedImage The bordered version of the Imoji sticker image
     * @param tags An array of NSString tags or nil if there are none
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<CreateImojiResponse> createImojiWithRawImage(@NonNull Bitmap rawImage,
                                                        @NonNull Bitmap borderedImage,
                                                        @Nullable List<String> tags);

    /**
     * Removes an Imoji sticker that was created by the user with createImojiWithImage:tags:callback:
     * @param imoji The added Imoji object
     * @return A cancellable future task for the request
     */
    @NonNull
    Future<ApiResponse> removeImoji(@NonNull Imoji imoji);

    /**
     * Reports an Imoji sticker as abusive. You may expose this method in your application in order for users to have the ability to flag
     * content as not appropriate. Reported Imojis are not removed instantly but are reviewed internally before removal.
     * @param imoji The Imoji object to report
     * @param reason Optional text describing the reason why the content is being reported
     * @return An operation reference that can be used to cancel the request
     */
    @NonNull
    Future<ApiResponse> reportImojiAsAbusive(@NonNull Imoji imoji, @Nullable String reason);

    /**
     * Marks an Imoji sticker as being used for sharing. For example, if a user copied a sticker
     * in a keyboard application, that would qualify as the Imoji being used.
     * @param imoji The Imoji object to register for usage
     * @param originIdentifier Optional arbitrary identifier which developers can supply describing the action that
     * triggered the usage. String must be less than or equal to 40 characters.
     * @return An operation reference that can be used to cancel the request
     */
    @NonNull
    Future<ApiResponse> markImojiUsage(@NonNull Imoji imoji, @Nullable String originIdentifier);

}
