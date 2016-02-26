/*
 * Imoji Android SDK
 * Created by nkhoshini on 2/25/16.
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

import android.support.annotation.NonNull;

import com.imoji.sdk.internal.ApiSession;

import java.util.UUID;

public class ImojiSDK {
    private static final ImojiSDK INSTANCE = new ImojiSDK();

    private UUID clientId;

    private String apiToken;

    public ImojiSDK setCredentials(@NonNull UUID clientId, @NonNull String apiToken) {
        this.clientId = clientId;
        this.apiToken = apiToken;

        return this;
    }

    public Session createSession() {
        return new ApiSession();
    }

    @NonNull
    public UUID getClientId() {
        return clientId;
    }

    @NonNull
    public String getApiToken() {
        return apiToken;
    }

    public static ImojiSDK getInstance() {
        return INSTANCE;
    }
}
