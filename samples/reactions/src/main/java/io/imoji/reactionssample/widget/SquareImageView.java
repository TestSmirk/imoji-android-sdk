package io.imoji.reactionssample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by sajjadtabib on 9/2/15.
 * Create a squared ImageView as long
 */
public class SquareImageView extends ImageView{

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if ((wMode == MeasureSpec.EXACTLY && hMode == MeasureSpec.EXACTLY) || (wMode == MeasureSpec.UNSPECIFIED && hMode == MeasureSpec.UNSPECIFIED)) {
            //parent has indicated exact measures or both unspecified measure; just run super
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        int maxWidth = -1;
        int maxHeight = -1;

        if (wMode == MeasureSpec.AT_MOST || wMode == MeasureSpec.EXACTLY) {
            maxWidth = width;
        }

        if (hMode == MeasureSpec.AT_MOST || hMode == MeasureSpec.EXACTLY) {
            maxHeight = height;
        }

        int size = 0;
        if (maxWidth > -1 && maxHeight > -1) {
            size = Math.min(maxHeight, maxWidth); //use the min of the two values
        }else if (maxWidth > -1) {
            size = maxWidth; //indicates that the height is unbounded, so use the maxWidth
        } else { //(maxHeight > -1)
            size = maxHeight; //indicates that the width is unbounded, so use the maxHeight as the size
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
