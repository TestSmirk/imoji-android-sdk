package com.imojiapp.imoji.sdk;

import android.content.Context;

import com.imojiapp.imoji.sdk.imoji.ImojiOutline;
import com.imojiapp.imoji.sdk.imoji.OutlineTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

/**
 * Created by sajjadtabib on 4/6/15.
 */
public abstract class ImojiApi {
    static final int DEFAULT_OFFSET = 0;
    static final int DEFAULT_RESULTS = 51;
    int mDefaultNumResults;


    /**
     *
     * @param apiToken
     * @return
     */
    public static ImojiApi newInstance(String apiToken) {
        return new ImojiApiImpl(apiToken);
    }

    /**
     *
     * @param offset
     * @param numResults
     * @return
     */
    abstract List<Imoji> getFeatured(int offset, int numResults);

    /**
     *
     * @return
     */
    abstract List<Imoji> getFeatured();

    /**
     *
     * @param query
     * @return
     */
    abstract List<Imoji> search(String query);

    /**
     *
     * @param query
     * @param offset
     * @param numResults
     * @return
     */
    abstract List<Imoji> search(String query, int offset, int numResults);

    /**
     * Asynchronous call that fetches featured imojis
     * @param offset
     * @param numResults
     * @param cb
     */
    abstract void getFeatured(int offset, int numResults, Callback<List<Imoji>> cb);

    /**
     *
     * @param cb
     */
    abstract void getFeatured(Callback<List<Imoji>> cb);

    /**
     *
     * @param query
     * @param cb
     */
    abstract void search(String query, Callback<List<Imoji>> cb);

    /**
     *
     * @param query
     * @param offset
     * @param numResults
     * @param cb
     */
    abstract void search(String query, int offset, int numResults, Callback<List<Imoji>> cb);

    /**
     *
     * @param imoji
     * @return
     */
    abstract RequestCreator loadThumb(Context context, Imoji imoji, ImojiOutline.OutlineOptions options);

    /**
     *
     * @param imoji
     * @return
     */
    abstract RequestCreator loadFull(Context context, Imoji imoji, ImojiOutline.OutlineOptions options);

    public static class Builder {
        private ImojiApi mApi;

        public Builder(String apiKey) {
            mApi = new ImojiApiImpl(apiKey);
        }

        public Builder numResults(int numResults) {
            mApi.mDefaultNumResults = numResults;
            return this;
        }

        public ImojiApi build() {
            return mApi;
        }
    }

    static class ImojiApiImpl extends ImojiApi{
        private String mApiToken;

        ImojiApiImpl(String apiToken) {
            mApiToken = apiToken;
        }

        @Override
        public List<Imoji> getFeatured(int offset, int numResults) {
            return ImojiNetApiHandle.getFeaturedImojis(mApiToken, offset, numResults);
        }

        @Override
        public List<Imoji> getFeatured() {
            return ImojiNetApiHandle.getFeaturedImojis(mApiToken, DEFAULT_OFFSET, DEFAULT_RESULTS);
        }

        @Override
        public List<Imoji> search(String query) {
            return search(query, DEFAULT_OFFSET, DEFAULT_RESULTS);
        }

        @Override
        public List<Imoji> search(String query, int offset, int numResults) {
            return ImojiNetApiHandle.searchImojis(mApiToken, query, offset, numResults);
        }

        @Override
        public void getFeatured(int offset, int numResults, Callback<List<Imoji>> cb) {
            ImojiNetApiHandle.getFeaturedImojis(mApiToken, offset, numResults, cb);
        }

        @Override
        public void getFeatured(Callback<List<Imoji>> cb) {
            getFeatured(DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
        }

        @Override
        public void search(String query, Callback<List<Imoji>> cb) {
            search(query, DEFAULT_OFFSET, DEFAULT_RESULTS, cb);
        }

        @Override
        public void search(String query, int offset, int numResults, Callback<List<Imoji>> cb) {
            ImojiNetApiHandle.searchImojis(mApiToken, query, offset, numResults, cb);
        }

        @Override
        public RequestCreator loadThumb(Context context, Imoji imoji, ImojiOutline.OutlineOptions options) {
            return Picasso.with(context).load(imoji.getThumbImageUrl()).transform(new OutlineTransformation(context, options));

        }

        @Override
        RequestCreator loadFull(Context context, Imoji imoji, ImojiOutline.OutlineOptions options) {
            return Picasso.with(context).load(imoji.getFullImageUrl()).transform(new OutlineTransformation(context, options));
        }
    }
}
