package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdksample.adapters.ImojiAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserImojisActivity extends Activity {

    private static final String LOG_TAG = UserImojisActivity.class.getSimpleName();
    public static final String GRANTED_BUNDLE_ARG_KEY = "GRANTED_BUNDLE_ARG_KEY";

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

    boolean mIsGettingAccess;
    boolean mAccessGranted;

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

    @Override
    protected void onNewIntent(Intent intent) {
        mAccessGranted = intent.getBooleanExtra(GRANTED_BUNDLE_ARG_KEY, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAccessGranted && mIsGettingAccess) {
            mIsGettingAccess = false;
            mGrantedTv.setTextColor(Color.GREEN);
            mGrantedTv.setText("TRUE");
            mContainer.setVisibility(View.VISIBLE);
        }
    }

    private void getUserAccess() {
        mIsGettingAccess = true;
        ImojiApi.with(this).initiateUserOauth(new Callback<String, String>() {
            @Override
            public void onSuccess(String result) {
                Log.d(LOG_TAG, "User access grant in progress, wait for broadcast - server result: " + result);
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
     * Create a broadcast receiver that extends com.imojiapp.imoji.sdk.ExternalGrantReceiver
     *
     */
    public static class ExternalGrantReceiver extends com.imojiapp.imoji.sdk.ExternalGrantReceiver {
        private final String LOG_TAG = ExternalGrantReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Log.d(LOG_TAG, "external grant receiver granted: " + mGranted);

            //Notify Activity that access was granted
            Intent activityIntent = new Intent();
            activityIntent.setClass(context, UserImojisActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activityIntent.putExtra(GRANTED_BUNDLE_ARG_KEY, mGranted);
            context.startActivity(activityIntent);

        }
    }
}
