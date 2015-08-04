package com.imojiapp.imoji.sdksample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.lv_samples)
    ListView mSamples;

    private SampleAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        try {
            ActivityInfo[] activityInfos = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA).activities;

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
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.sample_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ActivityInfo item = getItem(position);
            holder.mSampleName.setText(item.metaData.getString(Constants.MetaData.IMOJI_SAMPLE_NAME));
            return convertView;
        }
    }

    static class ViewHolder {
        @InjectView(R.id.tv_sample_name)
        TextView mSampleName;

        public ViewHolder(View v) {
            ButterKnife.inject(this, v);
        }
    }


}
