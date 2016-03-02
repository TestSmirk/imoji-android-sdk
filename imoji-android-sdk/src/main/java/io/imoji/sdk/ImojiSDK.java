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

import android.content.Context;
import android.support.annotation.NonNull;

import io.imoji.sdk.internal.ApiSession;

import java.util.UUID;

/**
 * Base class to create an Session object and set client credentials.
 * Refer to https://developer.imoji.io for information on obtaining a token
 */
public class ImojiSDK {
    private static final ImojiSDK INSTANCE = new ImojiSDK();

    private UUID clientId;

    private String apiToken;

    public ImojiSDK setCredentials(@NonNull UUID clientId, @NonNull String apiToken) {
        this.clientId = clientId;
        this.apiToken = apiToken;

        return this;
    }

    /**
     * Creates a new session with the Android application context
     * @param context The supplied application context
     * @return A new Session object to be used for making API calls
     */
    public Session createSession(@NonNull Context context) {
        if (this.apiToken == null) {
            throw new RuntimeException("apiToken has not been set");
        }

        if (this.clientId == null) {
            throw new RuntimeException("clientId has not been set");
        }

        return this.createSessionWithStoragePolicy(StoragePolicy.createWithContext(context));
    }

    /**
     * Creates a new session with a custom StoragePolicy instance
     * @param storagePolicy The requested StoragePolicy to use
     * @return A new Session object to be used for making API calls
     */
    public Session createSessionWithStoragePolicy(@NonNull StoragePolicy storagePolicy) {
        return new ApiSession(storagePolicy);
    }

    /**
     * @return The client ID in use
     */
    @NonNull
    public UUID getClientId() {
        return clientId;
    }

    /**
     * @return The supplied API Token
     */
    @NonNull
    public String getApiToken() {
        return apiToken;
    }

    public static ImojiSDK getInstance() {
        return INSTANCE;
    }
}
