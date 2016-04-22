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

package io.imoji.sdk;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Represents a top level task returned by API calls that can be ran as an AsyncTask or immediately
 * with an ExecutorService.
 * <p>
 * Created by nkhoshini on 2/26/16.
 *
 * @see AsyncTask
 * @see ExecutorService
 */
public class ApiTask<V> {

    /**
     * HTTP Executor settings borrowed from Bolts-Android's AndroidExecutors.newCachedThreadPool
     * https://github.com/BoltsFramework/Bolts-Android/blob/master/bolts-tasks/src/main/java/bolts/AndroidExecutors.java
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREAD_POOL_CORE_SIZE = CPU_COUNT + 2;
    private static final int THREAD_POOL_MAX_SIZE = CPU_COUNT * 2 + 2;
    private static final long THREAD_POOL_KEEP_ALIVE_TIME = 1L;
    private static final ExecutorService THREAD_POOL_EXECUTOR_SERVICE = new ThreadPoolExecutor(
            THREAD_POOL_CORE_SIZE,
            THREAD_POOL_MAX_SIZE,
            THREAD_POOL_KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>()
    );

    private static final int WRAPPED_ASYNC_ERROR_MESSAGE = 1;

    /**
     * A wrapped convenience class for running ApiTask's for Imoji SDK calls. Callers need to
     * override onPostExecute to get the value asynchronously from the call.
     *
     * @param <V>
     */
    public static abstract class WrappedAsyncTask<V> extends AsyncTask<Future<V>, Void, V> {
        @SafeVarargs
        @Override
        protected final V doInBackground(Future<V>... params) {
            try {
                FutureTask<V> task = (FutureTask<V>) params[0];
                THREAD_POOL_EXECUTOR_SERVICE.submit(task);
                return task.get();
            } catch (InterruptedException | ExecutionException e) {
                Log.e(ApiTask.class.getName(), "Unable to perform async task", e);
                Message message = MAIN_LOOP_HANDLER.obtainMessage(
                        WRAPPED_ASYNC_ERROR_MESSAGE,
                        new Pair<WrappedAsyncTask<?>, Throwable>(this, e)
                );
                MAIN_LOOP_HANDLER.dispatchMessage(message);

                throw new RuntimeException("Unable to perform async task", e);
            }
        }

        @Override
        protected abstract void onPostExecute(V v);

        /**
         * Called when the ApiTask encounters an error.
         *
         * @param error The error that occurred making the API call.
         */
        @SuppressWarnings("UnusedParameters")
        protected void onError(@NonNull Throwable error) {

        }
    }

    private static class MainLoopDispatchHandler extends Handler {
        public MainLoopDispatchHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WRAPPED_ASYNC_ERROR_MESSAGE) {
                @SuppressWarnings("unchecked")
                Pair<WrappedAsyncTask<?>, Throwable> details
                        = (Pair<WrappedAsyncTask<?>, Throwable>) msg.obj;
                details.first.onError(details.second);
            }
        }
    }

    private static final Handler MAIN_LOOP_HANDLER = new MainLoopDispatchHandler();

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
    @SuppressWarnings("unchecked")
    public AsyncTask<Future<V>, Void, V> executeAsyncTask(@NonNull WrappedAsyncTask<V> asyncTask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return this.executeAsyncTaskOnExecutor(asyncTask, THREAD_POOL_EXECUTOR_SERVICE);
        }

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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("unchecked")
    public AsyncTask<Future<V>, Void, V> executeAsyncTaskOnExecutor(@NonNull WrappedAsyncTask<V> asyncTask,
                                                                    @NonNull ExecutorService executorService) {
        return asyncTask.executeOnExecutor(executorService, this.scheduledTask);
    }

    /**
     * Immediately resolves the task on a shared thread pool executor service.
     *
     * @return The resolved value from running the task.
     * @throws ExecutionException   From executorService.submit
     * @throws InterruptedException From executorService.submit
     * @see ExecutorService
     */
    public V executeImmediately() throws ExecutionException, InterruptedException {
        return this.executeImmediately(THREAD_POOL_EXECUTOR_SERVICE);
    }

    /**
     * Immediately resolves the task on the supplied ExecutorService and returns its value
     *
     * @param executorService The executor service to run on.
     * @return The resolved value from running the task.
     * @throws ExecutionException   From executorService.submit
     * @throws InterruptedException From executorService.submit
     * @see ExecutorService
     */
    public V executeImmediately(@NonNull ExecutorService executorService) throws ExecutionException, InterruptedException {
        executorService.submit(scheduledTask);
        return scheduledTask.get();
    }
}
