package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdksample.adapters.ImojiAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserImojisActivity extends Activity {

    private static final String LOG_TAG = UserImojisActivity.class.getSimpleName();

    @InjectView(R.id.bt_get_user_imojis)
    Button mGetUserImojis;

    @InjectView(R.id.bt_get_user_access)
    Button mGetUserAccess;

    @InjectView(R.id.tv_granted)
    TextView mGrantedTv;

    @InjectView(R.id.gv_imoji_grid)
    GridView mImojiGrid;

    @InjectView(R.id.ll_container)
    View mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_imojis);
        ButterKnife.inject(this);

        mGetUserAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserAccess();
            }
        });

        mGetUserImojis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserImojis();
            }
        });

    }

    private void getUserAccess() {
        ImojiApi.with(this).initiateUserOauth(new Callback<String, String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(LOG_TAG, "success: " + result);
                mGrantedTv.setText("TRUE");
                mGrantedTv.setTextColor(Color.GREEN);
                mContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String err) {
                Log.d(LOG_TAG, "error: " + err);
                mGrantedTv.setText("FALSE");
                mGrantedTv.setTextColor(Color.RED);
            }
        });
    }

    private void getUserImojis() {
        ImojiApi.with(UserImojisActivity.this).getUserImojis(new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                ImojiAdapter adapter = new ImojiAdapter(UserImojisActivity.this, R.layout.imoji_item_layout, result);
                mImojiGrid.setAdapter(adapter);
            }

            @Override
            public void onFailure(String result) {
                Log.w(LOG_TAG, "failed: " + result);

            }
        });
    }




    /**
     * Created by sajjadtabib on 5/1/15.
     */
    public static class ExternalGrantReceiver extends com.imojiapp.imoji.sdk.ExternalGrantReceiver {
        private static final String LOG_TAG = ExternalGrantReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Log.d(LOG_TAG, "external grant receiver granted: " + mGranted);
        }
    }
}
