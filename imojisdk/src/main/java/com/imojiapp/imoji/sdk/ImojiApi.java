package com.imojiapp.imoji.sdk;

import android.content.Context;

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
     * Asynchronously retrieves a list of server generated imoji categories
     *
     * @param cb a callback, called on the main thread, to notify when categories are fetched or whether
     *           it failed
     */
    public abstract void getImojiCategories(Callback<List<ImojiCategory>, String> cb);


    /**
     * Asynchronously retrieves a list of server generated imoji categories
     *
     * @param cb             a callback, called on the main thread, to notify when categories are fetched or whether
     *                       it failed
     * @param classification A com.imojiapp.imoji.sdk.ImojiCategory.Classification
     */
    public abstract void getImojiCategories(String classification, final com.imojiapp.imoji.sdk.Callback<List<ImojiCategory>, String> cb);

    /**
     * This method requires that your client has been granted
     * user level access via initiateUserOauth
     *
     * @return a list of the user's collection imojis
     */
    public abstract void getUserImojis(Callback<List<Imoji>, String> cb);

    /**
     * Takes user to imojiapp so that they can create an imoji.
     * If imojiapp does not exist, then the user is taken to the
     * Google Play store to download imojiapp.
     */
    public abstract void createImoji();

    /**
     * Initiates flow to give your client access to a user's personal imojis.
     * You must register a broadcast receiver that extends
     *
     * @param statusCallback callback with information regarding the result of the call
     * @see com.imojiapp.imoji.sdk.ExternalGrantReceiver with an intent-filter action set to
     * com.imojiapp.imoji.oauth.external.GRANT and an intent-filter category of
     * com.imojiapp.imoji.category.EXTERNAL_CATEGORY
     * If access is granted, the broadcast receiver will have the mGranted field set to true.
     * Note that if the device does not have 'imoji' installed, the user will get redirected
     * to the play store so they can download imojiapp
     */
    public abstract void initiateUserOauth(Callback<String, String> statusCallback);

    /**
     * Given a list of imoji ids, the corresponding imojis are returned in the callback
     */
    public abstract void getImojisById(List<String> imojiIds, Callback<List<Imoji>, String> cb);

    /**
     * Adds an imoji to the user's collection
     */
    public abstract void addImojiToUserCollection(String imojiId, Callback<String, String> cb);

    /**
     * Initialize the API instance
     *
     * @param context      context used for api operations
     * @param clientId     your client id
     * @param clientSecret your api client secret
     */
    public static void init(Context context, final String clientId, final String clientSecret) {
        init(context, clientId, clientSecret, null);
    }

    /**
     * Initialize the API
     *
     * @param context      context used for api operations
     * @param clientId     your client id
     * @param clientSecret your api client secret
     * @param instance     a previously initialized api instance
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
                throw new IllegalStateException("instance has already been initialized");
            }

            sInstance = instance;
        }
    }

    /**
     * @param context
     * @return Instance of ImojiApi to perform API operations. NOTE: You must have called #init before calling this method
     */
    public static ImojiApi with(Context context) {
        if (sInstance == null) {

            synchronized (ImojiApi.class) {
                if (sInstance == null) {
                    sInstance = new Builder(context).build();
                }
            }
        }

        return sInstance;
    }

    /**
     * Builder class for instantating an imoji api instance
     * This class providers some configuration options
     */
    public static class Builder {
        private Context mContext;
        private int mDefaultNumResults = DEFAULT_RESULTS;

        public Builder(Context context) {
            mContext = context;
        }

        /**
         * Sets the default number of search/featured restuls to return
         *
         * @param numResults
         * @return
         */
        public Builder defaultResultCount(int numResults) {
            mDefaultNumResults = numResults;
            return this;
        }

        public ImojiApi build() {
            ImojiApi api = new ImojiApiImpl(mContext);
            api.mDefaultNumResults = mDefaultNumResults;
            return api;
        }
    }

}
