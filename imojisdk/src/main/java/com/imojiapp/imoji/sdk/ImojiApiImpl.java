package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.squareup.picasso.RequestCreator;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sajjadtabib on 4/29/15.
 */
class ImojiApiImpl extends ImojiApi {

    private volatile String mOauthToken;
    private volatile String mRefreshToken;
    private volatile long mExpirationTime;
    protected volatile boolean mIsAcquiringAuthToken;
    protected Queue<Command> mPendingCommands;


    ImojiApiImpl(Context context) {
        mContext = context;
        mPendingCommands = new LinkedBlockingQueue<>();
        SharedPreferenceManager.init(context);
        //check to see if the token exists or expired
        mOauthToken = SharedPreferenceManager.getString(PrefKeys.TOKEN_PROPERTY, null);
        if (mOauthToken != null) {
            mExpirationTime = SharedPreferenceManager.getLong(PrefKeys.EXPIRATION_PROPERTY, -1);
            mRefreshToken = SharedPreferenceManager.getString(PrefKeys.REFRESH_PROPERTY, null);
            if (System.currentTimeMillis() >= mExpirationTime) {
                //get the refresh token then return
                acquireOauthToken(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, null), SharedPreferenceManager.getString(PrefKeys.CLIENT_SECRET_PROPERTY, null), mRefreshToken);
                return;
            }

            //we have a valid token, return
            return;
        }

        acquireOauthToken(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, null), SharedPreferenceManager.getString(PrefKeys.CLIENT_SECRET_PROPERTY, null), null);
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
    List<Imoji> getUserImojis() {
        return null;
    }

    @Override
    List<ImojiCategory> getImojiCategories() {
        return ImojiNetApiHandle.getImojiCategories(mOauthToken);
    }

    @Override
    public void getFeatured(final int offset, final int numResults, final Callback<List<Imoji>, String> cb) {
        execute(new Command() {
            @Override
            public void run() {
                ImojiNetApiHandle.getFeaturedImojis(mOauthToken, offset, numResults, cb);
            }
        });

    }

    @Override
    public void getFeatured(final Callback<List<Imoji>, String> cb) {
        execute(new Command() {
            @Override
            public void run() {
                getFeatured(DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
            }
        });

    }

    @Override
    public void search(final String query, final Callback<List<Imoji>, String> cb) {
        execute(new Command() {
            @Override
            public void run() {
                search(query, DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
            }
        });

    }

    @Override
    public void search(final String query, final int offset, final int numResults, final Callback<List<Imoji>, String> cb) {
        execute(new Command() {
            @Override
            public void run() {
                ImojiNetApiHandle.searchImojis(mOauthToken, query, offset, numResults, cb);
            }
        });

    }

    @Override
    public void getImojiCategories(final Callback<List<ImojiCategory>, String> cb) {
        execute(new Command() {
            @Override
            public void run() {
                ImojiNetApiHandle.getImojiCategories(mOauthToken, cb);
            }
        });
    }

    @Override
    public void getUserImojis(final Callback<List<Imoji>, String> cb) {
        execute(new Command() {
            @Override
            public void run() {
                ImojiNetApiHandle.getUserImojis(mOauthToken, cb);
            }
        });
    }

    @Override
    public RequestCreator loadThumb(Imoji imoji, OutlineOptions options) {
        return mPicasso.with(mContext).load(imoji.getThumbImageUrl()).stableKey(imoji.getImojiId() + "thumb").transform(new OutlineTransformation(mContext, options));

    }

    @Override
    public RequestCreator loadFull(Imoji imoji, OutlineOptions options) {
        return mPicasso.with(mContext).load(imoji.getUrl()).stableKey(imoji.getImojiId() + "full").transform(new OutlineTransformation(mContext, options));
    }

    @Override
    public void createImoji() {
        if (!Utils.isImojiAppInstalled(mContext)) {
            Intent intent = Utils.getPlayStoreIntent(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, mContext.getPackageName()));
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(ExternalIntents.Actions.INTENT_CREATE_IMOJI_ACTION);
            intent.putExtra(ExternalIntents.BundleKeys.LANDING_PAGE_BUNDLE_ARG_KEY, ExternalIntents.BundleValues.CAMERA_PAGE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    //TODO: take a callback so that when things fail we can notify the client
    @Override
    public void initiateUserOauth(final Callback<String, String> statusCallback) {
        ImojiNetApiHandle.requestExternalOauth(mOauthToken, SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, null), new Callback<ExternalOauthPayloadResponse, String>() {
            @Override
            public void onSuccess(ExternalOauthPayloadResponse result) {
                String externalToken = result.payload;
                SharedPreferenceManager.putString(ImojiApi.PrefKeys.EXTERNAL_TOKEN, externalToken);
                //save the payload so that we can check later

                String status = Status.SUCCESS;
                //check to see if the app is available or not
                if (Utils.isImojiAppInstalled(mContext)) {

                    if (Utils.canHandleUserOauth(mContext)) {
                        //send a broadcast to the main app telling it to grant us access
                        Intent intent = new Intent();
                        intent.putExtra(ExternalIntents.BundleKeys.EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY, externalToken);
                        intent.setAction(ExternalIntents.Actions.INTENT_REQUEST_ACCESS);
                        intent.addCategory(ExternalIntents.Categories.EXTERNAL_CATEGORY);
                        mContext.sendBroadcast(intent);
                        statusCallback.onSuccess(status);
                        return;
                    } else {
                        status = Status.IMOJI_UPDATE_REQUIRED;
                    }

                } else {
                    status = Status.LAUNCH_PLAYSTORE;
                    Intent playStoreIntent = Utils.getPlayStoreIntent(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, mContext.getPackageName()));
                    playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(playStoreIntent);
                }

                statusCallback.onFailure(status);

            }

            @Override
            public void onFailure(String error) {
                statusCallback.onFailure(error);
            }
        });

    }

    private void execute(Command command) {

        //check to see if there's a valid oauth token or not
        if (mOauthToken != null && mExpirationTime >= (System.currentTimeMillis() - 30 * 1000)) {

            //we are good, so just execute the command and return
            command.run();
            return;
        }

        //otherwise, add the command to the queue
        mPendingCommands.add(command);
        if (!mIsAcquiringAuthToken) {
            acquireOauthToken(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, null), SharedPreferenceManager.getString(PrefKeys.CLIENT_SECRET_PROPERTY, null), SharedPreferenceManager.getString(PrefKeys.REFRESH_PROPERTY, null));
        }

    }

    private synchronized void acquireOauthToken(final String clientId, final String clientSecret, final String refreshToken) {
        mIsAcquiringAuthToken = true;
        //we need to get a new token
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                GetAuthTokenResponse response = ImojiNetApiHandle.getAuthToken(clientId, clientSecret, refreshToken);

                if (response != null) {
                    long expire = System.currentTimeMillis() + response.expires_in * 1000;

                    SharedPreferenceManager.putString(PrefKeys.TOKEN_PROPERTY, response.access_token);
                    SharedPreferenceManager.putString(PrefKeys.REFRESH_PROPERTY, response.refresh_token);
                    SharedPreferenceManager.putLong(PrefKeys.EXPIRATION_PROPERTY, expire);

                    mOauthToken = response.access_token;
                    mRefreshToken = response.refresh_token;
                    mExpirationTime = expire;

                    return response.access_token;
                }

                return null;
            }

            @Override
            protected void onPostExecute(String oauthToken) {
                mIsAcquiringAuthToken = false;
                mOauthToken = oauthToken;

                //execute all pending commands
                Command c;
                while ((c = mPendingCommands.poll()) != null) {
                    c.run();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static abstract class Command implements Runnable {

    }

}
