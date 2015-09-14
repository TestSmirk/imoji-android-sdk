package com.imojiapp.imoji.sdksample.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdksample.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created by sajjadtabib on 8/3/15.
 */
public class Utils {

    public static PopupWindow launchImojiPopupWindow(Activity activity, Imoji imoji) {
        View v = LayoutInflater.from(activity).inflate(R.layout.imoji_detail, (ViewGroup)activity.findViewById(android.R.id.content), false);
        GradientDrawable d = new GradientDrawable();
        d.setColor(Color.WHITE);
        d.setCornerRadius(10f);
        v.setBackgroundDrawable(d);

        ImageView imojiIv = ButterKnife.findById(v, R.id.iv_imoji);
        TextView tagsTv = ButterKnife.findById(v, R.id.tv_tags);
        TextView idTv = ButterKnife.findById(v, R.id.tv_imoji_id);
        ImageButton closeBtn = ButterKnife.findById(v, R.id.ib_close);

        Picasso.with(activity).load(imoji.getThumbUrl()).into(imojiIv);
        tagsTv.setText(Joiner.on(", ").join(imoji.getTags()));
        idTv.setText(imoji.getImojiId());

        final PopupWindow window = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.showAtLocation(activity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });

        return window;
    }
}
