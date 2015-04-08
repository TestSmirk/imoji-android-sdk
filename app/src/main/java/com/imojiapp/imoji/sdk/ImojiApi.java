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
    int mDefaultNumResults;


    /**
     * @param offset The offset into the results
     * @param numResults The maximum number of results to return
     * @return A list of featured imojis or null if there was an error
     */
    public abstract List<Imoji> getFeatured(int offset, int numResults);

    /**
     * @return A list of featured imojis that uses the default offset 0
     * and default numResults of 60, or null to indicate there was
     * an error
     */
    public abstract List<Imoji> getFeatured();

    /**
     * @param query the search string to query imojis
     * @return A list of featured imojis or null if there was an error
     */
    public abstract List<Imoji> search(String query);

    /**
     * A synchronous call the searches for imojis based on a query string
     * @param query the search string to query imojis
     * @param offset the offset in the search results
     * @param numResults the maximum number of results to return in the call
     * @return A list of imojis that match the query string or null if there was
     * an error
     */
    public abstract List<Imoji> search(String query, int offset, int numResults);

    /**
     * Asynchronous call that fetches featured imojis
     * @param offset offset into the featured imoji results
     * @param numResults the maximum number of results to return
     * @param cb a callback to notify the status of fetching featured imojis
     */
    public abstract void getFeatured(int offset, int numResults, Callback<List<Imoji>> cb);

    /**
     * Asynchronous call that fetches featured imojis using default offset of
     * 0 and a default numResult
     * @param cb a callback to notify the status of fetching featured imojis
     */
    public abstract void getFeatured(Callback<List<Imoji>> cb);

    /**
     * Asynchronous call that searches for imojis given a query
     * @param query the search string to query imojis
     * @param cb a callback to notify the status of searching imojis
     */
    public abstract void search(String query, Callback<List<Imoji>> cb);

    /**
     *
     * @param query the search string to query imojis
     * @param offset offset into the searched imoji results
     * @param numResults the maximum number of results to return
     * @param cb a callback to notify the status of searching imojis
     */
    public abstract void search(String query, int offset, int numResults, Callback<List<Imoji>> cb);

    /**
     * Synchronously retreives a list of imoji categories that can then be used
     * to search for popular imojis
     * @return A list of popular categories
     */
    public abstract List<ImojiCategory> getImojiCategories();

    /**
     * Asynchronously retrieves a list of server generated imoji categories
     * @param cb a callback to notify when categories are fetched or whether
     *           it failed
     */
    public abstract void getImojiCategories(Callback<List<ImojiCategory>> cb);


    /**
     * Helper class to load imoji thumbs given an Imoji object.
     * For more information on how to use the RequestCreator,
     * take a look at Square's Picasso Library http://square.github.io/picasso/
     *
     * @param context android context to use
     * @param imoji an imoji object
     * @param options outline options used to render an imoji
     * @return a Picasso RequestCreator object used to load bitmaps
     */
    public abstract RequestCreator loadThumb(Context context, Imoji imoji, ImojiOutline.OutlineOptions options);

    /**
     * Helper class to load full imojis given an Imoji object.
     * For more information on how to use the RequestCreator,
     * take a look at Square's Picasso Library http://square.github.io/picasso/
     *
     * @param context android context to use
     * @param imoji an imoji object
     * @param options outline options used to render an imoji
     * @return a Picasso RequestCreator object used to load bitmaps
     */
    public abstract RequestCreator loadFull(Context context, Imoji imoji, ImojiOutline.OutlineOptions options);

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
        public void getImojiCategories(Callback<List<ImojiCategory>> cb) {
            ImojiNetApiHandle.getImojiCategories(mApiToken, cb);
        }

        @Override
        public List<ImojiCategory> getImojiCategories() {
            return ImojiNetApiHandle.getImojiCategories(mApiToken);
        }

        @Override
        public RequestCreator loadThumb(Context context, Imoji imoji, ImojiOutline.OutlineOptions options) {
            return Picasso.with(context).load(imoji.getThumbImageUrl()).transform(new OutlineTransformation(context, options));

        }

        @Override
        public RequestCreator loadFull(Context context, Imoji imoji, ImojiOutline.OutlineOptions options) {
            return Picasso.with(context).load(imoji.getFullImageUrl()).transform(new OutlineTransformation(context, options));
        }
    }
}
