package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.graphics.Bitmap;

import com.koushikdutta.ion.bitmap.Transform;
import com.squareup.picasso.Transformation;

/**
 * Created by sajjadtabib on 4/6/15.
 */
final class OutlineTransformation implements Transformation, Transform{

    private OutlineOptions mOptions;
    private Context mContext;

    public OutlineTransformation(Context context, OutlineOptions options) {
        this.mOptions = options;
        mContext = context;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        ImojiOutline outline = new ImojiOutline(mContext, source, mOptions);
        Bitmap out = outline.render();
        source.recycle();
        return out;
    }

    @Override
    public String key() {
        return "outline" + (mOptions != null ? mOptions.toString() : "");
    }


}
