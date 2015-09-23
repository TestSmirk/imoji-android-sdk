package com.imojiapp.imoji.sdk;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.imojiapp.imoji.sdk.networking.responses.CreateImojiResponse;
import com.imojiapp.imoji.sdk.networking.responses.FetchImojisResponse;
import com.imojiapp.imoji.sdk.networking.responses.ImojiAckResponse;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sajjadtabib on 9/22/15.
 */
class ImojiUploadTask extends AsyncTask<Void, Void, Imoji>{
    private static final String LOG_TAG = ImojiUploadTask.class.getSimpleName();

    private final ImojiNetworkingInterface mApiHandle;
    private final Bitmap mBitmap;
    private List<String> mTags;
    private OnImojiUploadCompleteListener mUploadListener;

    public ImojiUploadTask(ImojiNetworkingInterface apiHandle, Bitmap bitmap, List<String> tags, OnImojiUploadCompleteListener uploadListener) {
        mApiHandle = apiHandle;
        mBitmap = bitmap;
        mTags = tags;
        mUploadListener = uploadListener;

    }

    @Override
    protected  Imoji doInBackground(Void... params) {

        //1. Create imoji
        CreateImojiResponse createResponse = mApiHandle.createImoji(mTags);
        if (createResponse == null || !createResponse.isSuccess()) {
            return null;
        }

        //2. Prepare imoji for upload
        byte[] fullImageData = prepareImageDataForUpload(mBitmap, createResponse.fullImageResizeWidth, createResponse.fullImageResizeHeight);
        byte[] thumbImageData = prepareImageDataForUpload(mBitmap, createResponse.resizeWidth, createResponse.resizeHeight);

        //3. Upload
        boolean fullUploadStatus = upload(createResponse.fullImageUrl, fullImageData);
        boolean thumbUploadStatus = upload(createResponse.thumbImageUrl, thumbImageData);

        //4. ack the upload
        ImojiAckResponse ackResponse = mApiHandle.ackImoji(createResponse.imojiId, fullUploadStatus, thumbUploadStatus);
        if (!ackResponse.isSuccess()) {
            return null;
        }

        //5. Fetch the newly created imoji
        FetchImojisResponse response = mApiHandle.getImojisById(Arrays.asList(new String[]{createResponse.imojiId}));
        if (response != null && response.isSuccess() && response.getPayload().size() > 0) {
            return response.getPayload().get(0);
        }

        return null;
    }

    private boolean upload(String url, byte[] fullImageData) {

        try {
            Map<String, String> requestProperties = new HashMap<>();
            requestProperties.put("Content-Type", "image/png");
            URL fullImageUploadUrl = new URL(url);
            return NetworkUtils.putObject(fullImageUploadUrl, fullImageData, requestProperties);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private byte[] prepareImageDataForUpload(Bitmap bitmap, int maxWidth, int maxHeight) {

        if (bitmap.getWidth() > maxWidth && bitmap.getHeight() > maxHeight) {
            int[] size = BitmapUtils.getSizeWithinBounds(bitmap.getWidth(), bitmap.getHeight(), maxWidth, maxHeight, false);
            //resize image
            bitmap = Bitmap.createScaledBitmap(bitmap, size[0], size[1], false);
        }

        //estimate the size
        int initialArraySize = bitmap.getWidth() * bitmap.getHeight() / 10;

        ByteArrayOutputStream out = new ByteArrayOutputStream(initialArraySize);

        //compress the bitmap to png
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        return out.toByteArray();
    }

    @Override
    protected void onPostExecute(Imoji imoji) {
        if (mUploadListener != null) {
            mUploadListener.onImojiUploadComplete(imoji);
        }
    }

    interface OnImojiUploadCompleteListener {
        void onImojiUploadComplete(Imoji imoji);
    }
}
