package com.imojiapp.imoji.sdk;

/**
 * Created by sajjadtabib on 4/29/15.
 */
class ExternalIntents {

    /**
     * * Intent Actions ***
     */
    interface IntentActions {
        String INTENT_CREATE_IMOJI_ACTION = "com.imojiapp.imoji.CREATE_IMOJI";
        String INTENT_REQUEST_ACCESS = "com.imojiapp.imoji.oauth.external.REQUEST";
        String INTENT_ACCESS_GRANT = "com.imojiapp.imoji.oauth.external.GRANT";
    }

    /**
     * * Intent Categories ***
     */
    interface IntentCategories {
        String EXTERNAL_CATEGORY = "com.imojiapp.imoji.category.EXTERNAL_CATEGORY";
    }

    /**
     * * Bundle Arguments ***
     */
    interface BundleKeys{
        String GRANTED = "granted";
        String LANDING_PAGE_BUNDLE_ARG_KEY = "LANDING_PAGE_BUNDLE_ARG_KEY";
        String EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY = "EXTERNAL_OAUTH_TOKEN_BUNDLE_ARG_KEY";
    }

    /**
     * * Bundle Values ***
     */
    interface BundleValues {
        int CAMERA_PAGE = 0;
    }
}
