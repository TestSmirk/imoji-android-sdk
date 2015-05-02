package com.imojiapp.imoji.sdk;

/**
 * Created by sajjadtabib on 4/29/15.
 */
class ImojiAppConstants {

    /**
     * * Intent Actions ***
     */
    interface IntentActions {
        String INTENT_CREATE_IMOJI_ACTION = "com.imojiapp.imoji.CREATE_IMOJI";
        String INTENT_GRANT_ACCESS = "com.imojiapp.imoji.GRANT_ACCESS";
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
