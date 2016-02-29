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

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Represents a top level task returned by API calls that can be ran as an AsyncTask or immediately
 * with an ExecutorService.
 * <p>
 * Created by nkhoshini on 2/26/16.
 * @see AsyncTask
 * @see ExecutorService
 */
public class ApiTask<V> {

    /**
     * A wrapped convenience class for running ApiTask's for Imoji SDK calls. Callers need to
     * override onPostExecute to get the value asynchronously from the call.
     * @param <V>
     */
    public static abstract class WrappedAsyncTask<V> extends AsyncTask<Future<V>, Void, V> {
        @SafeVarargs
        @Override
        protected final V doInBackground(Future<V>... params) {
            try {
                return params[0].get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(ApiTask.class.getName(), "Unable to perform async task", e);
                return null;
            }
        }

        @Override
        protected abstract void onPostExecute(V v);
    }

    private final FutureTask<V> scheduledTask;

    public ApiTask(Callable<V> callable) {
        this.scheduledTask = new FutureTask<>(callable);
    }

    /**
     * Executes the task as an AsyncTask. Callers must create an instance of WrappedAsyncTask
     * and override onPostExecute to get the value of the Api call. The default ExecutorService
     * used is the same as the default one used by AsyncTask itself. Use executeAsyncTaskOnExecutor
     * to supply your own ExecutorService.
     *
     * @param asyncTask A wrapped async task instance
     * @return The executed async task. Callers can perform any operation on the WrappedAsyncTask
     * as a standard AsyncTask such as cancelling it.
     */
    public AsyncTask<Future<V>, Void, V> executeAsyncTask(@NonNull WrappedAsyncTask<V> asyncTask) {
        return asyncTask.execute((Future<V>) this.scheduledTask);
    }

    /**
     * Executes the task as an AsyncTask with a supplied ExecutorService. Callers must create an
     * instance of WrappedAsyncTask and override onPostExecute to get the value of the Api call.
     *
     * @param asyncTask       A wrapped async task instance
     * @param executorService The executor service to run on
     * @return The executed async task. Callers can perform any operation on the WrappedAsyncTask
     * as a standard AsyncTask such as cancelling it.
     */
    @TargetApi(11)
    public AsyncTask<Future<V>, Void, V> executeAsyncTaskOnExecutor(@NonNull WrappedAsyncTask<V> asyncTask,
                                                                    @NonNull ExecutorService executorService) {
        return asyncTask.executeOnExecutor(executorService, this.scheduledTask);
    }

    /**
     * Immediately resolves the task on the supplied ExecutorService and returns its value
     * @param executorService The executor service to run on.
     * @return The resolved value from running the task.
     * @throws ExecutionException From executorService.submit
     * @throws InterruptedException From executorService.submit
     * @see ExecutorService
     */
    @SuppressWarnings("unchecked")
    public V executeImmediately(@NonNull ExecutorService executorService) throws ExecutionException, InterruptedException {
        // annoying we have to send over a Runnable to the execture which isn't generified
        return ((Future<V>) executorService.submit(scheduledTask)).get();
    }
}
