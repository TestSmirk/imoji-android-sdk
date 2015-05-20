package com.imojiapp.imoji.sdk;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public abstract class ImojiApi {


    static final int DEFAULT_OFFSET = 0;
    static final int DEFAULT_RESULTS = 60;

    protected static volatile ImojiApi sInstance;
    protected int mDefaultNumResults;
    protected Context mContext;
    protected Picasso mPicasso;

    /**
     * @param offset     The offset into the results
     * @param numResults The maximum number of results to return
     * @return A list of featured imojis or null if there was an error
     */
    abstract List<Imoji> getFeatured(int offset, int numResults);

    /**
     * @return A list of featured imojis that uses the default offset 0
     * and default numResults of 60, or null to indicate there was
     * an error
     */
    abstract List<Imoji> getFeatured();

    /**
     * @param query the search string to query imojis
     * @return A list of featured imojis or null if there was an error
     */
    abstract List<Imoji> search(String query);

    /**
     * A synchronous call the searches for imojis based on a query string
     *
     * @param query      the search string to query imojis
     * @param offset     the offset in the search results
     * @param numResults the maximum number of results to return in the call
     * @return A list of imojis that match the query string or null if there was
     * an error
     */
    abstract List<Imoji> search(String query, int offset, int numResults);

    /**
     * Asynchronous call that fetches featured imojis
     *
     * @param offset     offset into the featured imoji results
     * @param numResults the maximum number of results to return
     * @param cb         a callback, called on the main thread, to notify the status of fetching featured imojis
     */
    public abstract void getFeatured(int offset, int numResults, Callback<List<Imoji>, String> cb);

    /**
     * Asynchronous call that fetches featured imojis using default offset of
     * 0 and a default numResult
     *
     * @param cb a callback, called on the main thread, to notify the status of fetching featured imojis
     */
    public abstract void getFeatured(Callback<List<Imoji>, String> cb);

    /**
     * Asynchronous call that searches for imojis given a query
     *
     * @param query the search string to query imojis
     * @param cb    a callback, called on the main thread, to notify the status of searching imojis
     */
    public abstract void search(String query, Callback<List<Imoji>, String> cb);

    /**
     * @param query      the search string to query imojis
     * @param offset     offset into the searched imoji results
     * @param numResults the maximum number of results to return
     * @param cb         a callback, called on the main thread, to notify the status of searching imojis
     */
    public abstract void search(String query, int offset, int numResults, Callback<List<Imoji>, String> cb);

    /**
     * Synchronously retreives a list of imoji categories that can then be used
     * to search for popular imojis
     *
     * @return A list of popular categories
     */
    abstract List<ImojiCategory> getImojiCategories();

    /**
     * Asynchronously retrieves a list of server generated imoji categories
     *
     * @param cb a callback, called on the main thread, to notify when categories are fetched or whether
     *           it failed
     */
    public abstract void getImojiCategories(Callback<List<ImojiCategory>, String> cb);

    /**
     * @return a list of the user's collection imojis
     */
    abstract List<Imoji> getUserImojis();

    /**
     * This method requires that your client has been granted
     * user level access via initiateUserOauth
     * @return a list of the user's collection imojis
     */
    public abstract void getUserImojis(Callback<List<Imoji>, String> cb);


    /**
     * Helper class to load imoji thumbs given an Imoji object.
     * For more information on how to use the RequestCreator,
     * take a look at Square's Picasso Library http://square.github.io/picasso/
     *
     * @param imoji   an imoji object
     * @param options outline options used to render an imoji
     * @return a Picasso RequestCreator object used to load bitmaps
     */
    public abstract RequestCreator loadThumb(Imoji imoji, OutlineOptions options);

    /**
     * Helper class to load full imojis given an Imoji object.
     * For more information on how to use the RequestCreator,
     * take a look at Square's Picasso Library http://square.github.io/picasso/
     *
     * @param imoji   an imoji object
     * @param options outline options used to render an imoji
     * @return a Picasso RequestCreator object used to load bitmaps
     */
    public abstract RequestCreator loadFull(Imoji imoji, OutlineOptions options);


    /**
     * Takes user to the imojiapp so that they can create an imoji
     * If the app does not exist, then the user is taken to the
     * Google Play store to download the app.
     * In order to retrieve user generated content, you will have to call
     * initiateUserOauth to gain user privileges.
     */
    public abstract void createImoji();

    /**
     * Initiates flow to give your client access to a user's personal imojis.
     * You must register a broadcast receiver that extends
     * @see com.imojiapp.imoji.sdk.ExternalGrantReceiver with an intent-filter action set to
     * com.imojiapp.imoji.oauth.external.GRANT and an intent-filter category of
     * com.imojiapp.imoji.category.EXTERNAL_CATEGORY
     * If access is granted, the broadcast receiver will have the mGranted field set to true.
     * Note that if the device does not have 'imoji' installed, the user will get redirected
     * to the play store so they can download imojiapp
     * @param statusCallback callback with information regarding the result of the call
     */
    public abstract void initiateUserOauth(Callback<String, String> statusCallback);

    /**
     * Given a list of imoji ids, the corresponding imojis are returned in the callback
     */
    public abstract void getImojisById(List<String> imojiIds, Callback<List<Imoji>, String> cb);

    /**
     *
     */
    public abstract void addImojiToUserCollection(String imojiId, Callback<String, String> cb);


    /**
     * Initialize the API instance
     * @param context context used for api operations
     * @param clientId your client id
     * @param clientSecret your api client secret
     */
    public static void init(Context context, final String clientId, final String clientSecret) {
        init(context, clientId, clientSecret, null);
    }

    /**
     * Initialize the API
     * @param context context used for api operations
     * @param clientId your client id
     * @param clientSecret your api client secret
     * @param instance a previously initialized api instance
     */
    public static void init(Context context, final String clientId, final String clientSecret, ImojiApi instance) {
        SharedPreferenceManager.init(context);
        SharedPreferenceManager.putString(PrefKeys.CLIENT_ID_PROPERTY, clientId);
        SharedPreferenceManager.putString(PrefKeys.CLIENT_SECRET_PROPERTY, clientSecret);
        setInstance(instance == null ? new Builder(context).build() : instance);
    }

    static void setInstance(ImojiApi instance) {
        synchronized (ImojiApi.class) {
            if (sInstance != null) {
                throw new IllegalStateException("instance has already benn initialized");
            }

            sInstance = instance;
        }
    }

    /**
     *
     * @param context
     * @return Instance of ImojiApi to perform API operations. NOTE: You must have called #init before calling this method
     */
    public static ImojiApi with(Context context) {
        if (sInstance == null) {

            synchronized (ImojiApi.class) {
                sInstance = new Builder(context).build();
            }
        }

        return sInstance;
    }

    /**
     * Builder class for instantating an imoji api instance
     * This class providers some configuration options
     */
    public static class Builder {
        private ImojiApi mApi;

        public Builder(Context context) {
            mApi = new ImojiApiImpl(context);
        }

        public Builder defaultResultCount(int numResults) {
            mApi.mDefaultNumResults = numResults;
            return this;
        }

        public ImojiApi build() {
            mApi.mPicasso = new Picasso.Builder(mApi.mContext).build();
            return mApi;
        }
    }

}
