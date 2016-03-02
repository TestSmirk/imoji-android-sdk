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

package io.imoji.sdk.response;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Imoji Android SDK
 * <p>
 * Created by nkhoshini on 3/1/16.
 */
public class ImojiUploadResponse implements ApiResponse {

    @NonNull
    private final String imojiId;

    @NonNull
    private final Uri uploadUri;

    private final int maxWidth;

    private final int maxHeight;

    public ImojiUploadResponse(@NonNull String imojiId, @NonNull Uri uploadUri, int maxWidth, int maxHeight) {
        this.imojiId = imojiId;
        this.uploadUri = uploadUri;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @NonNull
    public String getImojiId() {
        return imojiId;
    }

    @NonNull
    public Uri getUploadUri() {
        return uploadUri;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}
