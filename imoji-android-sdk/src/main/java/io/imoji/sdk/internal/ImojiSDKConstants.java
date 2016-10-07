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

package io.imoji.sdk.internal;

import android.net.Uri;

/**
 * Constant strings used throughout the SDK
 */
public class ImojiSDKConstants {

    public static final Uri SERVER_URL = Uri.parse("https://api.imoji.io/v2");

    public static final String SERVER_SDK_VERSION = "2.2.0";

    public static final String PREFERENCES_OAUTH_ACCESS_TOKEN_KEY = "t";

    public static final String PREFERENCES_OAUTH_EXPIRATION_KEY = "e";

    public static final String PREFERENCES_OAUTH_REFRESH_TOKEN_KEY = "r";

    public static final String PREFERENCES_OAUTH_CLIENT_ID_KEY = "c";

    public static final String PREFERENCES_OAUTH_CLIENT_TOKEN_KEY = "s";

    public static class Paths {

        public static final String CATEGORIES_FETCH = "imoji/categories/fetch";

        public static final String SEARCH = "imoji/search";

        public static final String FEATURED = "imoji/featured/fetch";

        public static final String FETCH_IMOJIS_BY_ID = "imoji/fetchMultiple";

        public static final String CREATE_IMOJI = "imoji/create";

        public static final String REMOVE_IMOJI = "imoji/remove";

        public static final String REPORT_IMOJI = "imoji/reportAbusive";

        public static final String IMOJI_USAGE = "analytics/imoji/sent";

        public static final String SET_USER_DEMOGRAPHICS = "analytics/demographics";

        public static final String COLLECTION_ADD = "user/imoji/collection/add";

        public static final String COLLECTION_FETCH = "user/imoji/fetch";

        public static final String IMOJI_ATTRIBUTION = "imoji/attribution";

        public static final String OAUTH_REGISTER = "oauth/token";

        private Paths() {
        }
    }

    public static class Errors {
        public static final String OAUTH_VERIFICATION_ERROR_STATUS = "server_error";
    }

    public static class Headers {
        public static final String SDK_VERSION = "Imoji-SDK-Version";

        public static final String LOCALE = "User-Locale";

        public static final String AUTHORIZATION = "Authorization";
    }

    public static class Params {
        public static final String AUTH_TOKEN = "access_token";
    }

    private ImojiSDKConstants() {

    }
}
