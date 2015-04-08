package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjad on 9/5/14.
 */
public class BasicResponse {

    public interface Status{
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR_UPDATE = "ERROR_UPDATE";
        public static final String ERROR_INSERTION = "ERROR_INSERTION";
        public static final String ERROR_DELETION = "ERROR_DELETION";
        public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
        public static final String INVALID_LOGIN = "INVALID_LOGIN";
        public static final String USER_EXISTS = "USER_EXISTS";
        public static final String EMAIL_IN_USE = "EMAIL_IN_USE";
        public static final String USER_NAME_IN_USE = "USER_NAME_IN_USE";
        public static final String USER_ID_MISSING = "USER_ID_MISSING";
        public static final String PASSWORD_MISSING = "PASSWORD_MISSING";
        public static final String EMAIL_MISSING = "EMAIL_MISSING";
        public static final String AGE_MISSING = "AGE_MISSING";
        public static final String EXTERNAL_ID_MISSING = "EXTERNAL_ID_MISSING";
        public static final String PROVIDER_NAME_MISSING = "PROVIDER_NAME_MISSING";
        public static final String IMOJI_ID_MISSING = "IMOJI_ID_MISSING";
        public static final String IMAGE_CONTENTS_ID_MISSING = "IMAGE_CONTENTS_ID_MISSING";
        public static final String GROUP_ID_MISSING = "GROUP_ID_MISSING";
        public static final String GROUP_TITLE_MISSING = "GROUP_TITLE_MISSING";
        public static final String PASSWORD_UPDATE_REQUIRED = "PASSWORD_UPDATE_REQUIRED";
        public static final String NETWORK_ERROR = "NETWORK_ERROR";
    }

    public String status;

    public Object mCallerPayload;

    public boolean isSuccess(){
        return Status.SUCCESS.equals(status);
    }
}
