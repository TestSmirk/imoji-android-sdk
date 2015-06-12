package com.imojiapp.imoji.sdk;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;

import com.imojiapp.imoji.sdk.networking.responses.ExternalOauthPayloadResponse;
import com.imojiapp.imoji.sdk.networking.responses.GetAuthTokenResponse;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sajjadtabib on 4/29/15.
 */
class ImojiApiImpl extends ImojiApi {
    private static final String LOG_TAG = ImojiApiImpl.class.getSimpleName();

    private ExecutionManager mExecutionManager;
    private ImojiNetworkingInterface mINetworking;

    ImojiApiImpl(Context context) {
        mContext = context;
        SharedPreferenceManager.init(context);
        try {
            Class.forName("com.squareup.picasso.Picasso");
            Class.forName("retrofit.RequestInterceptor");
            mINetworking = new ImojiNetApiHandle(context);
        } catch( ClassNotFoundException e ) {
            try {
                Class.forName("com.koushikdutta.ion.Ion");
                mINetworking = new IonNetApiHandle(context);
            } catch (ClassNotFoundException e1) {
                throw new IllegalStateException("Picasso/Retrofit or koush/ion dependency missing");
            }
        }
        mExecutionManager = new ExecutionManager(context, mINetworking);
    }

    @Override
    List<Imoji> getFeatured(int offset, int numResults) {
        return null;
    }

    @Override
    List<Imoji> getFeatured() {
        return null;
    }

    @Override
    List<Imoji> search(String query) {
        return search(query, DEFAULT_OFFSET, DEFAULT_RESULTS);
    }

    @Override
    List<Imoji> search(String query, int offset, int numResults) {
        return null;
    }

    @Override
    List<Imoji> getUserImojis() {
        return null;
    }

    @Override
    List<ImojiCategory> getImojiCategories() {
        return null;
    }

    @Override
    public void getFeatured(final int offset, final int numResults, final Callback<List<Imoji>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency()}), cb) {
            @Override
            public void run() {
                mINetworking.getFeaturedImojis(offset, numResults, cb);
            }
        });

    }

    @Override
    public void getFeatured(final Callback<List<Imoji>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency()}), cb) {
            @Override
            public void run() {
                getFeatured(DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
            }
        });

    }

    @Override
    public void search(final String query, final Callback<List<Imoji>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency()}), cb) {
            @Override
            public void run() {
                search(query, DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
            }
        });

    }

    @Override
    public void search(final String query, final int offset, final int numResults, final Callback<List<Imoji>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency()}), cb) {
            @Override
            public void run() {
                mINetworking.searchImojis(query, offset, numResults, cb);
            }
        });

    }

    @Override
     public void getImojiCategories(final Callback<List<ImojiCategory>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency()}), cb) {
            @Override
            public void run() {
                mINetworking.getImojiCategories(cb);
            }
        });
    }

    @Override
    public void getImojiCategories(final String classification, final Callback<List<ImojiCategory>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency()}), cb) {
            @Override
            public void run() {
                mINetworking.getImojiCategories(classification, cb);
            }
        });
    }

    @Override
    public void getUserImojis(final Callback<List<Imoji>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency(), new ExternalAuthDependency()}), cb) {

            @Override
            public void run() {
                mINetworking.getUserImojis(cb);
            }
        });
    }

    @Override
    public RequestCreator loadThumb(Imoji imoji, OutlineOptions options) {
        return mPicasso.with(mContext).load(imoji.getThumbImageUrl()).stableKey(imoji.getImojiId() + "thumb").transform(new PicassoOutlineTransformation(mContext, options));

    }

    @Override
    public RequestCreator loadFull(Imoji imoji, OutlineOptions options) {
        return mPicasso.with(mContext).load(imoji.getUrl()).stableKey(imoji.getImojiId() + "full").transform(new PicassoOutlineTransformation(mContext, options));
    }

    @Override
    public Builders.Any.BF<? extends Builders.Any.BF<?>> loadThumbWithIon(Imoji imoji, OutlineOptions options) {
        return Ion.with(mContext).load(imoji.getThumbImageUrl()).withBitmap().transform(new IonOutlineTransformation(mContext, options));

    }

    @Override
    public Builders.Any.BF<? extends Builders.Any.BF<?>> loadFullWithIon(Imoji imoji, OutlineOptions options) {
        return Ion.with(mContext).load(imoji.getUrl()).withBitmap().transform(new IonOutlineTransformation(mContext, options));
    }

    @Override
    public void createImoji() {
        Intent intent;
        if (!Utils.isImojiAppInstalled(mContext)) {
            intent = Utils.getPlayStoreIntent(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, mContext.getPackageName()));
        } else {
            intent = new Intent(ExternalIntents.Actions.INTENT_CREATE_IMOJI_ACTION);
            intent.putExtra(ExternalIntents.BundleKeys.LANDING_PAGE_BUNDLE_ARG_KEY, ExternalIntents.BundleValues.CAMERA_PAGE);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    //TODO: take a callback so that when things fail we can notify the client
    @Override
    public void initiateUserOauth(final Callback<String, String> statusCallback) {
        mINetworking.requestExternalOauth(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, null), new Callback<ExternalOauthPayloadResponse, String>() {
            @Override
            public void onSuccess(ExternalOauthPayloadResponse result) {
                String externalToken = result.payload;
                SharedPreferenceManager.putString(PrefKeys.EXTERNAL_TOKEN, externalToken);
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
                    try {
                        mContext.startActivity(playStoreIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        status = Status.PLAYSTORE_NOT_FOUND;
                    }
                }

                statusCallback.onFailure(status);

            }

            @Override
            public void onFailure(String error) {
                statusCallback.onFailure(error);
            }
        });

    }

    @Override
    public void getImojisById(final List<String> imojiIds, final Callback<List<Imoji>, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency()}), cb) {
            @Override
            public void run() {
                mINetworking.getImojisById(imojiIds, cb);
            }
        });
    }

    @Override
    public void addImojiToUserCollection(final String imojiId, final Callback<String, String> cb) {
        mExecutionManager.execute(new Command(Arrays.asList(new ExecutionDependency[]{new OauthDependency(), new ExternalAuthDependency()}), cb) {
            @Override
            public void run() {
                mINetworking.addImojiToUserCollection(imojiId, cb);
            }
        });
    }

    @Override
    public void setPicassoInstance(Picasso picasso) {
        mPicasso = picasso;
    }

    void executePendingCommands() {
        if (mExecutionManager != null) {
            mExecutionManager.executePendingCommands();
        }
    }

    private static class ExecutionHandlerThread extends HandlerThread {
        private final Handler mHandler;
        public ExecutionHandlerThread() {
            super("ExecutionHandlerThread");
            start();
            mHandler = new Handler(getLooper());

        }

        Handler getHandler() {
            return mHandler;
        }
    }


    private static class ExecutionManager {
        private volatile String mOauthToken;
        private volatile String mRefreshToken;
        private volatile long mExpirationTime;
        protected Queue<Command> mPendingCommands;
        private Context mContext;
        private final long mTimeoutMillis;
//        private final ExecutionHandlerThread mHandlerThread = new ExecutionHandlerThread();
        private final Handler mHandler = new Handler(); //UI Thread Handler

        private volatile boolean mIsAcquiringExternalToken;
        private volatile boolean mIsAcquiringAuthToken;
        private ImojiNetworkingInterface mINetworking;

        public ExecutionManager(Context context, ImojiNetworkingInterface networkingInterface) {
            mContext = context;
            mINetworking = networkingInterface;
            mTimeoutMillis = 30 * 1000;
            init();
//            mHandler = mHandlerThread.getHandler();
        }

        private void init() {
            mPendingCommands = new LinkedBlockingQueue<>();

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

        private void execute(final Command command) {

            //check that all dependencies have been satisfied
            if (command.isDependencySatisfied(this)) {
                command.run();             //we are good, so just execute the command and return
                return;
            }

            //schedule timeout and failure handling
            mHandler.removeCallbacksAndMessages(command);
            mHandler.postAtTime(new Runnable() {
                @Override
                public void run() {
                    boolean removed = false;
                    synchronized (ExecutionManager.class) {
                       removed = mPendingCommands.remove(command);
                    }
                        if (removed) {
                            if (command.mErrCallback != null) {
                                command.mErrCallback.onFailure(Status.TIMEOUT_FAILURE);
                            }
                        }

                }
            }, command, command.mExpiration);

            //add the command to the list of pending commands
            synchronized (ExecutionManager.class) {
                //otherwise, add the command to the queue
                mPendingCommands.add(command);
                command.satisfyDependencies(this);
            }

        }

        private synchronized void acquireOauthToken(final String clientId, final String clientSecret, final String refreshToken) {
            if (clientId == null || clientSecret == null) { //hacky way right now
                return;
            }
            if (!mIsAcquiringAuthToken) {
                mIsAcquiringAuthToken = true;
                //we need to get a new token
                new AsyncTask<String, Void, String>() { //Use a thread instead?
                    @Override
                    protected String doInBackground(String... params) {
                        String id = params[0];
                        String secret = params[1];
                        String refresh = params[2];
                        GetAuthTokenResponse response = mINetworking.getAuthToken(id, secret, refresh);

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

                        if (oauthToken != null) {
                            executePendingCommands();
                        } else {
                            //clear all values store for the token
                            SharedPreferenceManager.putString(PrefKeys.TOKEN_PROPERTY, null);
                            SharedPreferenceManager.putLong(PrefKeys.EXPIRATION_PROPERTY, -1);
                            SharedPreferenceManager.putBoolean(PrefKeys.EXTERNAL_GRANT_STATUS, false);

                            //Should we do the same for the refresh? probably not because this could've just been a refresh
                        }


                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, clientId, clientSecret, refreshToken);
            }
        }

        void executePendingCommands() {
            //execute all pending commands
            synchronized (ExecutionManager.class) {
                Command c;
                List<Command> executionList = new ArrayList<>();
                while ((c = mPendingCommands.poll()) != null) {
                    executionList.add(c);
                }

                if (!executionList.isEmpty()) { //prevent recursion by reading all from the queue, and then executing them again
                    for (Command cmd : executionList) {
                        //remove all the queued messages to remove the commands
                        mHandler.removeCallbacksAndMessages(cmd);
                        ExecutionManager.this.execute(cmd); //execute the command, which may then require another dependency to be resolved
                    }

                }
            }
        }

        private synchronized void startExternalOauth() {
            if (!mIsAcquiringExternalToken) {
                mIsAcquiringExternalToken = true;

                mINetworking.requestExternalOauth(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, null), new Callback<ExternalOauthPayloadResponse, String>() {
                    @Override
                    public void onSuccess(ExternalOauthPayloadResponse result) {
                        String externalToken = result.payload;
                        SharedPreferenceManager.putString(PrefKeys.EXTERNAL_TOKEN, externalToken);
                        //save the payload so that we can check later

                        String status = com.imojiapp.imoji.sdk.Status.SUCCESS;
                        //check to see if the app is available or not
                        if (Utils.isImojiAppInstalled(mContext)) {

                            if (Utils.canHandleUserOauth(mContext)) {
                                //send a broadcast to the main app telling it to grant us access
                                Intent intent = new Intent();
                                intent.putExtra(ExternalIntents.BundleKeys.EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY, externalToken);
                                intent.setAction(ExternalIntents.Actions.INTENT_REQUEST_ACCESS);
                                intent.addCategory(ExternalIntents.Categories.EXTERNAL_CATEGORY);
                                mContext.sendBroadcast(intent); //wait for a response
                                //statusCallback.onSuccess(status);
                                return;
                            } else {
                                status = com.imojiapp.imoji.sdk.Status.IMOJI_UPDATE_REQUIRED;
                            }

                        } else {
                            status = com.imojiapp.imoji.sdk.Status.LAUNCH_PLAYSTORE;
                            Intent playStoreIntent = Utils.getPlayStoreIntent(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, mContext.getPackageName()));
                            playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                            try {
                                mContext.startActivity(playStoreIntent);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        mIsAcquiringExternalToken = false;

                    }

                    @Override
                    public void onFailure(String error) {
                        mIsAcquiringExternalToken = false;
                        //statusCallback.onFailure(error);
                    }
                });

            }

        }

    }

    private interface ExecutionDependency {
        boolean isDependencySatisfied(ExecutionManager executionManager);

        void satisfyDependencies(ExecutionManager executionManager);
    }


    private class OauthDependency implements ExecutionDependency {

        @Override
        public boolean isDependencySatisfied(ExecutionManager executionManager) {
            return executionManager.mOauthToken != null && executionManager.mExpirationTime >= (System.currentTimeMillis() - 30 * 1000);
        }

        @Override
        public void satisfyDependencies(ExecutionManager executionManager) {
            executionManager.acquireOauthToken(SharedPreferenceManager.getString(PrefKeys.CLIENT_ID_PROPERTY, null), SharedPreferenceManager.getString(PrefKeys.CLIENT_SECRET_PROPERTY, null), SharedPreferenceManager.getString(PrefKeys.REFRESH_PROPERTY, null));
        }
    }

    private class ExternalAuthDependency implements ExecutionDependency {

        @Override
        public boolean isDependencySatisfied(ExecutionManager executionManager) {
            return SharedPreferenceManager.getBoolean(PrefKeys.EXTERNAL_GRANT_STATUS, false);
        }

        @Override
        public void satisfyDependencies(ExecutionManager executionManager) {
            executionManager.startExternalOauth();
        }
    }

    private static abstract class Command implements Runnable, ExecutionDependency {
        private int mRetries = 0;
        long mExpiration;
        List<ExecutionDependency> mExecutionDependencies;
        Callback<?, String> mErrCallback;



        Command(List<ExecutionDependency> dependencies, Callback<?, String> errCallback) {
            mExecutionDependencies = dependencies;
            mExpiration = SystemClock.uptimeMillis()  + 30 * 1000; //30 second, then expire and run
            mErrCallback = errCallback;
        }

        @Override
        public boolean isDependencySatisfied(ExecutionManager executionManager) {
            boolean isSatisfied = true;
            for (ExecutionDependency dependency : mExecutionDependencies) {
                isSatisfied &= dependency.isDependencySatisfied(executionManager);
            }
            return isSatisfied;
        }

        //later have a flag that allows serial vs parallel
        @Override
        public void satisfyDependencies(ExecutionManager executionManager) {
            for (ExecutionDependency dependency : mExecutionDependencies) {
                if (!dependency.isDependencySatisfied(executionManager)) {
                    dependency.satisfyDependencies(executionManager);
                    break; //satisfy dependencies one at a time
                }
            }
        }
    }


}
