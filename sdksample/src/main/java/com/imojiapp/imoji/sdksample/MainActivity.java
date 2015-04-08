package com.imojiapp.imoji.sdksample;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {
private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.gv_imoji_grid)
    GridView mImojiGrid;

    @InjectView(R.id.et_search)
    EditText mSearchEt;

    private ImojiApi mImojiApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ImojiApi.Builder builder = new ImojiApi.Builder("wasup dawg");
        mImojiApi = builder.build();
        mImojiApi.getFeatured(new Callback<List<Imoji>>() {
            @Override
            public void onSuccess(List<Imoji> result) {
                ImojiAdapter adapter = new ImojiAdapter(MainActivity.this, R.layout.imoji_item_layout, result);
                mImojiGrid.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                Log.d(LOG_TAG, "failure");
            }
        });

        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String query = v.getText().toString();
                    mImojiApi.search(query, new Callback<List<Imoji>>() {
                        @Override
                        public void onSuccess(List<Imoji> result) {
                            ImojiAdapter adapter = new ImojiAdapter(MainActivity.this, R.layout.imoji_item_layout, result);
                            mImojiGrid.setAdapter(adapter);
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                    return true;
                }
                return false;
            }
        });

    }

    private class ImojiAdapter extends ArrayAdapter<Imoji> {
    
        private LayoutInflater mInflater;
    
        public ImojiAdapter(Context context, int resource, List<Imoji> items) {
            super(context, resource, items);
            mInflater = LayoutInflater.from(context);
        }
    
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.imoji_item_layout, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            
            Imoji item = getItem(position);
            mImojiApi.loadThumb(getContext(), item, null).into(holder.mImojiIv);
            
    
            return convertView;
        }
    }
    
    static class ViewHolder{

        @InjectView(R.id.iv_imoji)
        ImageView mImojiIv;

        public ViewHolder(View v){
            ButterKnife.inject(this, v);
        }
    }

}
