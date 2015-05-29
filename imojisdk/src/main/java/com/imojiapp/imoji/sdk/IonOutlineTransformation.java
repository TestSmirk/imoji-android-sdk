package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.graphics.Bitmap;

import com.koushikdutta.ion.bitmap.Transform;

/**
 * Created by sajjadtabib on 5/29/15.
 */
class IonOutlineTransformation implements Transform{
    private OutlineOptions mOptions;
    private Context mContext;

    public IonOutlineTransformation(Context context, OutlineOptions options) {
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
