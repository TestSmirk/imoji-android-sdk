package com.imojiapp.imoji.sdksample;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReportAbusiveImojiActivity extends AppCompatActivity {

    private static final String LOG_TAG = ReportAbusiveImojiActivity.class.getSimpleName();
    private static final String ABUSIVE_IMOJI_ID = "ac6e038f-3392-46a6-a1fb-573cd2fea1cb";

    @InjectView(R.id.iv_imoji)
    ImageView mImojiIv;

    @InjectView(R.id.bt_report_abusive)
    Button mReportAbusive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abusive_imoji);
        ButterKnife.inject(this);

        ImojiApi.with(this).getImojisById(Arrays.asList(new String[]{ABUSIVE_IMOJI_ID}), new Callback<List<Imoji>, String>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                Imoji imoji = result.get(0);
                Picasso.with(ReportAbusiveImojiActivity.this).load(imoji.getThumbUrl()).into(mImojiIv);
            }

            @Override
            public void onFailure(String result) {
                Log.d(LOG_TAG, "failed: " + result);
            }
        });

        mReportAbusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImojiApi.with(ReportAbusiveImojiActivity.this).reportAbusiveImoji(ABUSIVE_IMOJI_ID, new Callback<String, String>() {
                    @Override
                    public void onSuccess(String result) {
                        Snackbar.make(findViewById(R.id.iv_imoji), "Reported abusive imoji - status: " + result, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String result) {

                    }
                });
            }
        });
    }
}
