package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imoji.sdk.ApiTask;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.ImojisResponse;
import com.imojiapp.imoji.sdksample.adapters.ImojiAdapter;
import com.imojiapp.imoji.sdksample.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchImojiActivity extends Activity {

    private static final String LOG_TAG = SearchImojiActivity.class.getSimpleName();
    public static final String QUERY_BUNDLE_ARG_KEY = "QUERY_BUNDLE_ARG_KEY";

    @InjectView(R.id.et_search)
    EditText mSearchEt;

    @InjectView(R.id.gv_imoji_grid)
    GridView mImojiGrid;

    @InjectView(R.id.pb_progress)
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_imoji);
        ButterKnife.inject(this);

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String query = v.getText().toString();
                    mProgress.setVisibility(View.VISIBLE);
                    doSearch(query);
                    return true;
                }
                return false;
            }
        });

        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Imoji imoji = (Imoji) parent.getItemAtPosition(position);
                Utils.launchImojiPopupWindow(SearchImojiActivity.this, imoji);
            }
        });

        //let's check to see if we have a query in the intent
        if (getIntent().hasExtra(QUERY_BUNDLE_ARG_KEY)) {
            String query = getIntent().getStringExtra(QUERY_BUNDLE_ARG_KEY);
            mSearchEt.setText(query);
            doSearch(query);
        }
    }

    private void doSearch(String query) {
        ImojiSDK.getInstance()
                .createSession(getApplicationContext())
                .searchImojis(query)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<ImojisResponse>() {
                    @Override
                    protected void onPostExecute(ImojisResponse imojisResponse) {
                        ImojiAdapter adapter = new ImojiAdapter(SearchImojiActivity.this, R.layout.imoji_item_layout, imojisResponse.getImojis());
                        mImojiGrid.setAdapter(adapter);
                        mProgress.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_imoji, menu);
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
