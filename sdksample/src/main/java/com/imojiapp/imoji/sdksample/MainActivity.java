package com.imojiapp.imoji.sdksample;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Callback;
import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiApi;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.imojiapp.imoji.sdksample.adapters.ImojiAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();



    @InjectView(R.id.lv_samples)
    ListView mSamples;

//    @InjectView(R.id.gv_imoji_grid)
//    GridView mImojiGrid;
//
//    @InjectView(R.id.et_search)
//    EditText mSearchEt;
//
//    @InjectView(R.id.iv_full_imoji)
//    ImageView mFullImoji;

    private SampleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        try {
            ActivityInfo[] activityInfos = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES|PackageManager.GET_META_DATA).activities;

            List<ActivityInfo> sampleActivities = new ArrayList<>();
            for (ActivityInfo info : activityInfos) {
                if (info.metaData != null && info.metaData.containsKey(Constants.MetaData.IMOJI_SAMPLE_NAME)) {
                    sampleActivities.add(info);
                }
            }

            mAdapter = new SampleAdapter(this, R.layout.sample_item, sampleActivities);
            mSamples.setAdapter(mAdapter);
            mSamples.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ActivityInfo info = (ActivityInfo) parent.getItemAtPosition(position);
                    Intent intent = new Intent();
                    intent.setClassName(info.packageName, info.name);
                    startActivity(intent);
                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


//        Picasso myPicasso = new Picasso.Builder(this).build();
//        ImojiApi.with(this).setPicassoInstance(myPicasso);
//
//        mImojiGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Imoji imoji = (Imoji) parent.getItemAtPosition(position);
//                ImojiApi.with(MainActivity.this).loadFull(imoji).into(mFullImoji);
//            }
//        });
//
//        mImojiGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Imoji imoji = (Imoji) parent.getItemAtPosition(position);
//
//                ImojiApi.with(MainActivity.this).addImojiToUserCollection(imoji.getImojiId(), new Callback<String, String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        Log.d(LOG_TAG, "Add Imoji Success with result: " + result);
//                    }
//
//                    @Override
//                    public void onFailure(String result) {
//                        Log.d(LOG_TAG, "Add Imoji Failed with result: " + result);
//                    }
//                });
//
//
//                return false;
//            }
//
//        });
//
//        ImojiApi.with(this).getFeatured(new Callback<List<Imoji>, String>() {
//            @Override
//            public void onSuccess(List<Imoji> result) {
//                Log.d(LOG_TAG, "imoji: " + result.get(0).toString());
//
//
//                ImojiAdapter adapter = new ImojiAdapter(MainActivity.this, R.layout.imoji_item_layout, result);
//                mImojiGrid.setAdapter(adapter);
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Log.d(LOG_TAG, "failure: " + error);
//            }
//        });
//
//        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    String query = v.getText().toString();
//                    ImojiApi.with(MainActivity.this).search(query, new Callback<List<Imoji>, String>() {
//                        @Override
//                        public void onSuccess(List<Imoji> result) {
//                            ImojiAdapter adapter = new ImojiAdapter(MainActivity.this, R.layout.imoji_item_layout, result);
//                            mImojiGrid.setAdapter(adapter);
//                        }
//
//                        @Override
//                        public void onFailure(String error) {
//                            Log.d(LOG_TAG, "failed with error: " + error);
//                        }
//                    });
//                    return true;
//                }
//                return false;
//            }
//        });

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
            return true;
        }else if (item.getItemId() == R.id.action_external_oauth) {
            ImojiApi.with(this).initiateUserOauth(new Callback<String, String>() {
                @Override
                public void onSuccess(String result) {
                    Log.d(LOG_TAG, "success: " + result);
                }

                @Override
                public void onFailure(String err) {
                    Log.d(LOG_TAG, "error: " + err);
                }
            });
            return true;
        }else if (item.getItemId() == R.id.action_get_user_imojis) {
            ImojiApi.with(this).getUserImojis(new Callback<List<Imoji>, String>() {
                @Override
                public void onSuccess(List<Imoji> result) {
                    Log.d(LOG_TAG, "got user imojis: " + result.size());
                }

                @Override
                public void onFailure(String error) {
                    Log.d(LOG_TAG, "failed to get user imojis: error");
                }
            });
        }else if (item.getItemId() == R.id.action_get_categories) {
            ImojiApi.with(this).getImojiCategories(ImojiCategory.Classification.GENERIC, new Callback<List<ImojiCategory>, String>() {
                @Override
                public void onSuccess(List<ImojiCategory> result) {
                    for (ImojiCategory c : result) {
                        Log.d(LOG_TAG, "" + c.getTitle());
                    }
                }

                @Override
                public void onFailure(String result) {

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

    private class SampleAdapter extends ArrayAdapter<ActivityInfo> {

        private LayoutInflater mInflater;

        public SampleAdapter(Context context, int resource, List<ActivityInfo> items) {
            super(context, resource, items);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.sample_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            ActivityInfo item = getItem(position);
            holder.mSampleName.setText(item.metaData.getString(Constants.MetaData.IMOJI_SAMPLE_NAME));
            return convertView;
        }
    }

    static class ViewHolder{
        @InjectView(R.id.tv_sample_name)
        TextView mSampleName;

        public ViewHolder(View v){
            ButterKnife.inject(this, v);
        }
    }


}
