package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdksample.adapters.ImojiAdapter;
import com.imojiapp.imoji.sdksample.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchAsYouTypeImojiActivity extends Activity {

    private static final String LOG_TAG = SearchImojiActivity.class.getSimpleName();
    public static final String QUERY_BUNDLE_ARG_KEY = "QUERY_BUNDLE_ARG_KEY";

    @InjectView(R.id.et_search)
    EditText mSearchEt;

    @InjectView(R.id.gv_imoji_grid)
    GridView mImojiGrid;

    @InjectView(R.id.pb_progress)
    ProgressBar mProgress;

    private CancellableCallback mActiveCallback;

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

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Imoji imoji = (Imoji) parent.getItemAtPosition(position);
                Utils.launchImojiPopupWindow(SearchAsYouTypeImojiActivity.this, imoji);
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
        if (mActiveCallback != null) {
            mActiveCallback.cancel();
        }
        mActiveCallback = new CancellableCallback();
        ImojiApi.with(SearchAsYouTypeImojiActivity.this).search(query, 0, 8, mActiveCallback);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_as_you_type_imoji, menu);
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

    private class CancellableCallback implements Callback<List<Imoji>, String> {

        public void cancel() {
            mIsCancelled = true;
        }

        private boolean mIsCancelled;

        @Override
        public void onSuccess(List<Imoji> result) {
            if (!mIsCancelled) {
                ImojiAdapter adapter = new ImojiAdapter(SearchAsYouTypeImojiActivity.this, R.layout.imoji_item_layout, result == null ? new ArrayList<Imoji>() : result);
                mImojiGrid.setAdapter(adapter);
                mProgress.setVisibility(View.GONE);
            }

        }

        @Override
        public void onFailure(String error) {
            mProgress.setVisibility(View.GONE);
            Log.d(LOG_TAG, "failed with error: " + error);
        }
    }
}
