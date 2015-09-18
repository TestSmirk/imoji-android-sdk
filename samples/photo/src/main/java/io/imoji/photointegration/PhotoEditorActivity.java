package io.imoji.photointegration;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import io.imoji.photointegration.widget.SquareImageView;

public class PhotoEditorActivity extends AppCompatActivity {


    FrameLayout mEditor;
    ImageView mPhotoIv;
    Button mSelectBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editor);

        mEditor = (FrameLayout) findViewById(R.id.editor_container);
        mPhotoIv = (ImageView) findViewById(R.id.iv_image);
        mSelectBt = (Button) findViewById(R.id.bt_select);
        mSelectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImojiSearchFragment f = ImojiSearchFragment.newInstance();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container, f).commitAllowingStateLoss();
            }
        });
    }


    public void addImojiToEditor(BitmapDrawable bitmap) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.imoji_size), getResources().getDimensionPixelSize(R.dimen.imoji_size), ViewGroup.LayoutParams.WRAP_CONTENT);
        final ImageView imageView = new ImageView(this, null);
        imageView.setLayoutParams(params);
        imageView.setImageDrawable(bitmap);
        imageView.setOnTouchListener(new SuperTouchListener(this, imageView));
        mEditor.addView(imageView);
    }

}
