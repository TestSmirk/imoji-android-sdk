package com.imojiapp.imoji.sdk;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public abstract class ImojiApi {

    static final int DEFAULT_OFFSET = 0;
    static final int DEFAULT_RESULTS = 60;
    protected static final String PREF_FILE = "imoji-store";

    protected static final String CLIENT_ID_PROPERTY = "c";
    protected static final String CLIENT_SECRET_PROPERTY = "s";
    protected static final String TOKEN_PROPERTY = "t";
    protected static final String EXPIRATION_PROPERTY = "e";
    protected static final String REFRESH_PROPERTY = "r";

    protected static volatile ImojiApi sInstance;
    int mDefaultNumResults;
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
    public abstract void getFeatured(int offset, int numResults, Callback<List<Imoji>> cb);

    /**
     * Asynchronous call that fetches featured imojis using default offset of
     * 0 and a default numResult
     *
     * @param cb a callback, called on the main thread, to notify the status of fetching featured imojis
     */
    public abstract void getFeatured(Callback<List<Imoji>> cb);

    /**
     * Asynchronous call that searches for imojis given a query
     *
     * @param query the search string to query imojis
     * @param cb    a callback, called on the main thread, to notify the status of searching imojis
     */
    public abstract void search(String query, Callback<List<Imoji>> cb);

    /**
     * @param query      the search string to query imojis
     * @param offset     offset into the searched imoji results
     * @param numResults the maximum number of results to return
     * @param cb         a callback, called on the main thread, to notify the status of searching imojis
     */
    public abstract void search(String query, int offset, int numResults, Callback<List<Imoji>> cb);

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
    public abstract void getImojiCategories(Callback<List<ImojiCategory>> cb);

    /**
     * @return a list of the user's collection imojis
     */
    abstract List<Imoji> getCollectionImojis();


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
     * Google Play store to download the app
     */
    public abstract void createImoji();


    /**
     * Initialize the API instance
     *
     * @param context context used for api operations
     * @return an instance of the Imoji Api
     */
    public static void init(Context context, final String clientId, final String clientSecret) {
        init(context, clientId, clientSecret, new Builder(context).build());
    }

    public static void init(Context context, final String clientId, final String clientSecret, ImojiApi instance) {
        final SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        prefs.edit().putString(CLIENT_ID_PROPERTY, clientId).apply();
        prefs.edit().putString(CLIENT_SECRET_PROPERTY, clientSecret).apply();
        setInstance(instance);

    }


    static void setInstance(ImojiApi instance) {
        synchronized (ImojiApi.class) {
            if (sInstance != null) {
                throw new IllegalStateException("instance has already benn initialized");
            }

            sInstance = instance;
        }
    }

    private abstract class Command implements Runnable {
    }

    public static ImojiApi with(Context context) {
        if (sInstance == null) {

            synchronized (ImojiApi.class) {
                sInstance = new Builder(context).build();
            }
        }

        return sInstance;
    }

    public static class Builder {
        private ImojiApi mApi;

        public Builder(Context context) {
            mApi = new ImojiApiImpl(context);
        }

        public Builder numResults(int numResults) {
            mApi.mDefaultNumResults = numResults;
            return this;
        }

        public ImojiApi build() {
            mApi.mPicasso = new Picasso.Builder(mApi.mContext).build();
            return mApi;
        }
    }

    static class ImojiApiImpl extends ImojiApi {
        private static final String INTENT_CREATE_IMOJI_ACTION = "com.imojiapp.imoji.CREATE_IMOJI";
        private static final String LANDING_PAGE_BUNDLE_ARG_KEY = "LANDING_PAGE_BUNDLE_ARG_KEY";
        private static final int CAMERA_PAGE = 0;
        private SharedPreferences mPrefs;
        private volatile String mOauthToken;
        private volatile String mRefreshToken;
        private volatile long mExpirationTime;
        protected volatile boolean mIsAcquiringAuthToken;
        protected Queue<Command> mPendingCommands;


        ImojiApiImpl(Context context) {
            mContext = context;
            mPrefs = mContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
            mPendingCommands = new LinkedBlockingQueue<>();

            //check to see if the token exists or expired
            mOauthToken = mPrefs.getString(TOKEN_PROPERTY, null);
            if (mOauthToken  != null) {
                mExpirationTime = mPrefs.getLong(EXPIRATION_PROPERTY, -1);
                mRefreshToken = mPrefs.getString(REFRESH_PROPERTY, null);
                if (System.currentTimeMillis() >= mExpirationTime) {
                    //get the refresh token then return
                    acquireOauthToken(mPrefs.getString(CLIENT_ID_PROPERTY, null), mPrefs.getString(CLIENT_SECRET_PROPERTY, null), mPrefs.getString(REFRESH_PROPERTY, null));
                    return;
                }

                //we have a valid token, return
                return;
            }

            acquireOauthToken(mPrefs.getString(CLIENT_ID_PROPERTY, null), mPrefs.getString(CLIENT_SECRET_PROPERTY, null), null);
        }

        @Override
        List<Imoji> getFeatured(int offset, int numResults) {
            return ImojiNetApiHandle.getFeaturedImojis(mOauthToken, offset, numResults);
        }

        @Override
        List<Imoji> getFeatured() {
            return ImojiNetApiHandle.getFeaturedImojis(mOauthToken, DEFAULT_OFFSET, DEFAULT_RESULTS);
        }

        @Override
        List<Imoji> search(String query) {
            return search(query, DEFAULT_OFFSET, DEFAULT_RESULTS);
        }

        @Override
        List<Imoji> search(String query, int offset, int numResults) {
            return ImojiNetApiHandle.searchImojis(mOauthToken, query, offset, numResults);
        }

        @Override
        List<Imoji> getCollectionImojis() {
            throw new IllegalStateException("not implemented");
        }

        @Override
        List<ImojiCategory> getImojiCategories() {
            return ImojiNetApiHandle.getImojiCategories(mOauthToken);
        }

        @Override
        public void getFeatured(final int offset, final int numResults, final Callback<List<Imoji>> cb) {
            execute(new Command() {
                @Override
                public void run() {
                    ImojiNetApiHandle.getFeaturedImojis(mOauthToken, offset, numResults, cb);
                }
            });

        }

        @Override
        public void getFeatured(final Callback<List<Imoji>> cb) {
            execute(new Command() {
                @Override
                public void run() {
                    getFeatured(DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
                }
            });

        }

        @Override
        public void search(final String query, final Callback<List<Imoji>> cb) {
            execute(new Command() {
                @Override
                public void run() {
                    search(query, DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
                }
            });

        }

        @Override
        public void search(final String query, final int offset, final int numResults, final Callback<List<Imoji>> cb) {
            execute(new Command() {
                @Override
                public void run() {
                    ImojiNetApiHandle.searchImojis(mOauthToken, query, offset, numResults, cb);
                }
            });

        }

        @Override
        public void getImojiCategories(final Callback<List<ImojiCategory>> cb) {
            execute(new Command() {
                @Override
                public void run() {
                    ImojiNetApiHandle.getImojiCategories(mOauthToken, cb);
                }
            });
        }


        @Override
        public RequestCreator loadThumb(Imoji imoji, OutlineOptions options) {
            return mPicasso.with(mContext).load(imoji.getThumbImageUrl()).transform(new OutlineTransformation(mContext, options));

        }

        @Override
        public RequestCreator loadFull(Imoji imoji, OutlineOptions options) {
            return mPicasso.with(mContext).load(imoji.getUrl()).transform(new OutlineTransformation(mContext, options));
        }

        @Override
        public void createImoji() {
            PackageManager packageManager = mContext.getPackageManager();
            Intent intent = new Intent();
            intent.setAction(INTENT_CREATE_IMOJI_ACTION);
            intent.putExtra(LANDING_PAGE_BUNDLE_ARG_KEY, CAMERA_PAGE);
            ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);

            if (resolveInfo == null) {
                Intent playStoreIntent = new Intent();
                playStoreIntent.setAction(Intent.ACTION_VIEW);
                playStoreIntent.setData(Uri.parse("market://details?id=" + Constants.IMOJI_APP_PACKAGE));
                try {
                    mContext.startActivity(playStoreIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }

            mContext.startActivity(intent);
        }

        private void execute(Command command) {
            //check to see if there's a valid oauth token or not
            if(mOauthToken != null && mExpirationTime >= (System.currentTimeMillis() - 30 * 1000)){
                //we are good, so just execute the command and return
                command.run();
                return;
            }

            Log.d("debug", "adding command to queue");
            //otherwise, add the command to the queue
            mPendingCommands.add(command);
            if (!mIsAcquiringAuthToken) {
                acquireOauthToken(mPrefs.getString(CLIENT_ID_PROPERTY, null), mPrefs.getString(CLIENT_SECRET_PROPERTY, null), mPrefs.getString(REFRESH_PROPERTY, null));
            }

        }

        private synchronized void acquireOauthToken(final String clientId, final String clientSecret, final String refreshToken) {
            mIsAcquiringAuthToken = true;
            //we need to get a new token
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {

                    GetAuthTokenResponse response = ImojiNetApiHandle.getAuthToken(clientId, clientSecret, refreshToken);
                    if (response != null) {
                        long expire = System.currentTimeMillis() + response.expires_in * 1000;

                        mPrefs.edit().putString(TOKEN_PROPERTY, response.access_token).apply();
                        mPrefs.edit().putString(REFRESH_PROPERTY, response.refresh_token).apply();
                        mPrefs.edit().putLong(EXPIRATION_PROPERTY, expire ).apply();

                        mOauthToken = response.access_token;
                        mRefreshToken = response.refresh_token;
                        mExpirationTime = expire;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    mIsAcquiringAuthToken = false;
                    //execute all pending commands
                    Command c;
                    while ((c = mPendingCommands.poll()) != null) {
                        Log.d("debug", "the oauth token is: " + mOauthToken);
                        c.run();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

}
