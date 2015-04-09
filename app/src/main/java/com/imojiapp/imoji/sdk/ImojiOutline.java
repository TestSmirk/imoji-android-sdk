package com.imojiapp.imoji.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.imojiapp.imoji.imojisdk.R;
import com.imojiapp.imoji.renderscript.NativeOutline;

final class ImojiOutline {
    private static final int ALIGN_TO = 16;
    private Bitmap outBitmap;
    private Context context;

    private Bitmap sourceBitmap;
    private int color;
    private OutlineOptions options;

    private Bitmap tempBitmap;

    ImojiOutline(Context context, final Bitmap sourceBitmap, OutlineOptions options) {
        this.sourceBitmap = sourceBitmap;
        this.color = options != null ? options.color : Color.WHITE;
        this.context = context;
    }


    Bitmap render() {
        int shadowOffset = Math.max(1, Math.max(sourceBitmap.getWidth(), sourceBitmap.getHeight()) / 30);
        int radius = (options != null && options.outlineRadius != Integer.MAX_VALUE) ? options.outlineRadius : Math.max(1, Math.max(sourceBitmap.getWidth(), sourceBitmap.getHeight()) / 50);
        int shadowRadius = (options != null && options.shadowRadius != Integer.MAX_VALUE) ? options.outlineRadius : Math.max(1, Math.max(1, Math.max(sourceBitmap.getWidth(), sourceBitmap.getHeight()) / 30));
        int shadowOffsetX = options != null && options.shadowOffsetX != Integer.MAX_VALUE ? options.shadowOffsetX : 0; //(int) (Math.cos(shadowAngle * Math.PI / 180.0) * shadowOffset);
        int shadowOffsetY = (options != null && options.shadowOffsetY != Integer.MAX_VALUE ) ? options.shadowOffsetY : context.getResources().getDimensionPixelSize(R.dimen.one_dp);//(int) (Math.sin(shadowAngle * Math.PI / 180.0) * shadowOffset);
        int shadowColor = options != null ? options.shadowColor : 0x80000000;

        int padding = radius + shadowRadius + shadowOffset;
        int width = sourceBitmap.getWidth() + padding * 4;
        int height = sourceBitmap.getHeight() + padding * 4;

        tempBitmap = Bitmap.createBitmap(width, height, sourceBitmap.getConfig());
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(sourceBitmap, padding * 2, padding * 2, null);

        NativeOutline.perform(tempBitmap, radius, color, shadowRadius, shadowOffsetX, shadowOffsetY, shadowColor);

        return tempBitmap;
    }

    public static class OutlineOptions {
        public int color;
        public int outlineRadius = Integer.MAX_VALUE;
        public int shadowRadius = Integer.MAX_VALUE;
        public int shadowOffsetX = Integer.MAX_VALUE;
        public int shadowOffsetY = Integer.MAX_VALUE; //Integer.MAX_VALUE indicates it's uninitialized
        public int shadowColor = 0x80000000;

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
