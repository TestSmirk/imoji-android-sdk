package io.imoji.photointegration.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by sajjadtabib on 9/3/15.
 */
public class PhotoEditor extends FrameLayout {
    public PhotoEditor(Context context) {
        this(context, null);
    }

    public PhotoEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Bitmap mPhoto;



    @Override
    protected void onDraw(Canvas canvas) {
        if (mPhoto != null) {
            canvas.drawBitmap(mPhoto, 0, 0, null);
        }


    }


    public void loadPhotoIntoEditor(Bitmap photo) {
        mPhoto = photo;
    }
}
