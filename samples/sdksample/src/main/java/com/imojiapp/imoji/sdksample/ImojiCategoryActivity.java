package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.imoji.sdk.ApiTask;
import com.imoji.sdk.ImojiSDK;
import com.imoji.sdk.RenderingOptions;
import com.imoji.sdk.Session;
import com.imoji.sdk.objects.Category;
import com.imoji.sdk.objects.Imoji;
import com.imoji.sdk.response.CategoriesResponse;
import com.squareup.picasso.Picasso;

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
    Session mImojiSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imoji_category);
        ButterKnife.inject(this);


        mLoadAllBt.setOnClickListener(mOnButtonClick);
        mLoadEmoticonBt.setOnClickListener(mOnButtonClick);
        mLoadTrending.setOnClickListener(mOnButtonClick);
        mImojiSession = ImojiSDK.getInstance().createSession(getApplicationContext());

        mCategoryGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category category = (Category) parent.getItemAtPosition(position);
                Intent intent = new Intent(ImojiCategoryActivity.this, SearchImojiActivity.class);
                intent.putExtra(SearchImojiActivity.QUERY_BUNDLE_ARG_KEY, category.getIdentifier());
                startActivity(intent);
            }
        });
    }

    private View.OnClickListener mOnButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Category.Classification classification;
            if (v.getId() == R.id.bt_load_trending_categories) {
                classification = Category.Classification.Trending;
            }else if (v.getId() == R.id.bt_load_emoji_categories) {
                classification = Category.Classification.Generic;
            } else {
                classification = Category.Classification.None;
            }

            loadImojiCategories(classification);
        }
    };


    private void loadImojiCategories(Category.Classification classification) {
        mImojiSession
                .getImojiCategories(classification)
                .executeAsyncTask(new ApiTask.WrappedAsyncTask<CategoriesResponse>() {
                    @Override
                    protected void onPostExecute(CategoriesResponse categoriesResponse) {
                        mCategoryAdapter = new ImojiCategoryAdapter(ImojiCategoryActivity.this, R.layout.imoji_category_item, categoriesResponse.getCategories());
                        mCategoryGrid.setAdapter(mCategoryAdapter);
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

    private class ImojiCategoryAdapter extends ArrayAdapter<Category> {

        private LayoutInflater mInflater;

        public ImojiCategoryAdapter(Context context, int resource, List<Category> items) {
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

            Category category = getItem(position);
            Imoji preview = category.getPreviewImoji();
            Picasso.with(getContext()).load(preview.urlForRenderingOption(RenderingOptions.borderedPngThumbnail())).into(holder.mImojiIv);
            holder.mTitleTv.setText(category.getTitle());
            holder.mSearchTextTv.setText(category.getIdentifier());

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
