package com.imojiapp.imoji.sdksample;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.OutlineOptions;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {
private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.gv_imoji_grid)
    GridView mImojiGrid;

    @InjectView(R.id.et_search)
    EditText mSearchEt;

    @InjectView(R.id.iv_full_imoji)
    ImageView mFullImoji;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        ImojiApi.init(this, "Your API Key");

        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Imoji imoji = (Imoji) parent.getItemAtPosition(position);
                OutlineOptions options = new OutlineOptions();
                options.color = Color.parseColor("#F88920");
                ImojiApi.with(MainActivity.this).loadFull(imoji, options).into(mFullImoji);


            }
        });

        ImojiApi.with(this).getFeatured(new Callback<List<Imoji>>() {
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
                    ImojiApi.with(MainActivity.this).search(query, new Callback<List<Imoji>>() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_imoji) {
            ImojiApi.with(this).createImoji();
        }
        return super.onOptionsItemSelected(item);
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
            ImojiApi.with(MainActivity.this).loadThumb(item, null).into(holder.mImojiIv);
            
    
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
