package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import io.imoji.sdk.ApiTask;
import io.imoji.sdk.ImojiSDK;
import io.imoji.sdk.objects.Imoji;
import io.imoji.sdk.response.ImojisResponse;
import com.imojiapp.imoji.sdksample.adapters.ImojiAdapter;
import com.imojiapp.imoji.sdksample.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TrendingImojiActivity extends Activity {
    private static final String LOG_TAG = TrendingImojiActivity.class.getSimpleName();
    @InjectView(R.id.gv_imoji_grid)
    GridView mImojiGrid;

    @InjectView(R.id.bt_load_trending)
    Button mLoadTrending;

    @InjectView(R.id.pb_progress)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending_imoji);
        ButterKnife.inject(this);

        mLoadTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                loadTrendingImojis();
            }
        });

        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Imoji imoji = (Imoji) parent.getItemAtPosition(position);

                showImojiDetails(imoji);
            }
        });

    }

    private void showImojiDetails(Imoji imoji) {

        Utils.launchImojiPopupWindow(this, imoji);
    }

    private void loadTrendingImojis() {
        ImojiSDK.getInstance()
                .createSession(getApplicationContext())
                .getFeaturedImojis()
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        ImojiAdapter adapter = new ImojiAdapter(TrendingImojiActivity.this, R.layout.imoji_item_layout, imojisResponse.getImojis());
                        mImojiGrid.setAdapter(adapter);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trending_imoji, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
