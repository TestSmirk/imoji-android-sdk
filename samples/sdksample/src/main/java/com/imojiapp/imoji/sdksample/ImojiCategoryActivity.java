package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ImojiCategoryActivity extends Activity {
private static final String LOG_TAG = ImojiCategoryActivity.class.getSimpleName();
    @InjectView(R.id.bt_load_all_categories)
    Button mLoadAllBt;
    @InjectView(R.id.bt_load_emoji_categories)
    Button mLoadEmoticonBt;
    @InjectView(R.id.bt_load_trending_categories)
    Button mLoadTrending;
    @InjectView(R.id.gv_imoji_grid)
    GridView mCategoryGrid;

    ImojiCategoryAdapter mCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imoji_category);
        ButterKnife.inject(this);


        mLoadAllBt.setOnClickListener(mOnButtonClick);
        mLoadEmoticonBt.setOnClickListener(mOnButtonClick);
        mLoadTrending.setOnClickListener(mOnButtonClick);

        mCategoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImojiCategory category = (ImojiCategory) parent.getItemAtPosition(position);
                Intent intent = new Intent(ImojiCategoryActivity.this, SearchImojiActivity.class);
                intent.putExtra(SearchImojiActivity.QUERY_BUNDLE_ARG_KEY, category.getSearchText());
                startActivity(intent);
            }
        });
    }

    private View.OnClickListener mOnButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String classification;
            if (v.getId() == R.id.bt_load_trending_categories) {
                classification = ImojiCategory.Classification.TRENDING;
            }else if (v.getId() == R.id.bt_load_emoji_categories) {
                classification = ImojiCategory.Classification.GENERIC;
            } else {
                classification = ImojiCategory.Classification.NONE;
            }

            loadImojiCategories(classification);
        }
    };


    private void loadImojiCategories(String classification) {
        ImojiApi.with(ImojiCategoryActivity.this).getImojiCategories(classification, new Callback<List<ImojiCategory>, String>() {
            @Override
            public void onSuccess(List<ImojiCategory> result) {
                mCategoryAdapter = new ImojiCategoryAdapter(ImojiCategoryActivity.this, R.layout.imoji_category_item, result);
                mCategoryGrid.setAdapter(mCategoryAdapter);

            }

            @Override
            public void onFailure(String result) {
                Log.w(LOG_TAG, "" + result);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_imoji_category, menu);
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

    private class ImojiCategoryAdapter extends ArrayAdapter<ImojiCategory> {

        private LayoutInflater mInflater;

        public ImojiCategoryAdapter(Context context, int resource, List<ImojiCategory> items) {
            super(context, resource, items);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.imoji_category_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            ImojiCategory category = getItem(position);
            Picasso.with(getContext()).load(category.getImoji().getImageUrl(Imoji.ImageFormat.Png, Imoji.ImageSize.ImageSizeThumbnail)).into(holder.mImojiIv);
            holder.mTitleTv.setText(category.getTitle());
            holder.mSearchTextTv.setText(category.getSearchText());

            return convertView;
        }
    }

    static class ViewHolder{

        @InjectView(R.id.iv_imoji)
        ImageView mImojiIv;

        @InjectView(R.id.tv_title)
        TextView mTitleTv;

        @InjectView(R.id.tv_search_text)
        TextView mSearchTextTv;

        public ViewHolder(View v){
            ButterKnife.inject(this, v);
        }
    }
}
