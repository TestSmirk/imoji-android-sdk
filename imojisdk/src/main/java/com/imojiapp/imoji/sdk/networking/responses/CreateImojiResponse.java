package com.imojiapp.imoji.sdk.networking.responses;

/**
 * Created by sajjad on 9/4/14.
 */

public class CreateImojiResponse extends BasicResponse<CreateImojiResponse>{

    public String imojiId;
    public String fullImageUrl;
    public String thumbImageUrl;
    public String originalImageUrl;
    public int resizeHeight;
    public int resizeWidth;
    public int fullImageResizeHeight;
    public int fullImageResizeWidth;

    @Override
    public CreateImojiResponse getPayload() {
        return this;
    }
}
