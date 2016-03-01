package com.imojiapp.imoji.sdksample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.imoji.sdk.ApiTask;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.Session;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.ApiResponse;
import com.imoji.sdk.response.ImojisResponse;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

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

        final AtomicReference<Imoji> abusiveImoji = new AtomicReference<>();
        final Session session = ImojiSDK.getInstance()
                .createSession(getApplicationContext());

        session
                .fetchImojisByIdentifiers(Collections.singletonList(ABUSIVE_IMOJI_ID))
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        abusiveImoji.set(imojisResponse.getImojis().iterator().next());
                        Picasso.with(ReportAbusiveImojiActivity.this).load(abusiveImoji.get().getStandardThumbnailUri()).into(mImojiIv);
                    }
                });

        mReportAbusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.reportImojiAsAbusive(abusiveImoji.get(), "Android Testing")
                        .executeAsyncTask(new ApiTask.WrappedAsyncTask<ApiResponse>() {
                            @Override
                            protected void onPostExecute(ApiResponse apiResponse) {
                                Snackbar.make(findViewById(R.id.iv_imoji), "Reported abusive imoji succeeded", Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}
