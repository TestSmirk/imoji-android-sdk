package com.imojiapp.triggers;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TriggerActivity extends AppCompatActivity implements MessageInterface{

    private final Handler mHandler = new Handler();
    FrameLayout mFrameLayout;
    ImageButton mImojiPanelBt;
    ListView mMessageLv;
    Button mSendBt;
    EditText mInputBar;
    MessageAdapter mAdapter;
    SearchRunnable mSearchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);


        if (savedInstanceState == null) {
            ImojiCategoryFragment f = ImojiCategoryFragment.newInstance(ImojiCategory.Classification.TRENDING);
            getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
        }


        initViews();

        mSearchRunnable = new SearchRunnable(this);

    }

    private void initViews() {
        mFrameLayout = (FrameLayout) findViewById(R.id.container);

        mImojiPanelBt = (ImageButton) findViewById(R.id.ib_search);
        mImojiPanelBt.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            public void onClick(View v) {
                if (mFrameLayout.getVisibility() == View.GONE) {
                    mFrameLayout.setVisibility(View.VISIBLE);
                    ObjectAnimator.ofFloat(mImojiPanelBt, "rotation", 45f).start();
                } else {
                    popBackstack();
                    mFrameLayout.setVisibility(View.GONE);
                    ObjectAnimator.ofFloat(mImojiPanelBt, "rotation", 0f).start();

                }
            }
        });

        mMessageLv = (ListView) findViewById(R.id.lv_msg_list);
        if (mAdapter == null) {
            mAdapter = new MessageAdapter(this, -1, new ArrayList<Message>());
        }

        mMessageLv.setAdapter(mAdapter);

        mInputBar = (EditText) findViewById(R.id.et_input_bar);
        mInputBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mHandler.removeCallbacks(mSearchRunnable);
                    mHandler.postDelayed(mSearchRunnable, 1000);
                }
            }
        });

        mSendBt = (Button) findViewById(R.id.bt_send);
        mSendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputBar.getText().length() > 0) {
                    addText(mInputBar.getText().toString());
                    mInputBar.getText().clear();
                }
            }
        });


    }

    static class SearchRunnable implements Runnable {

        private WeakReference<TriggerActivity> mActivityWeakReference;

        public SearchRunnable(TriggerActivity activity) {
            mActivityWeakReference = new WeakReference<TriggerActivity>(activity);
        }

        @Override
        public void run() {
            TriggerActivity a = mActivityWeakReference.get();
            if (Build.VERSION.SDK_INT >= 17 && a.isDestroyed()) {
                return;
            }

            if (a != null ) {
                ImojiSearchFragment f = (ImojiSearchFragment) a.getSupportFragmentManager().findFragmentByTag(ImojiSearchFragment.FRAGMENT_TAG);
                String text = a.mInputBar.getText().toString();
                if (f != null) {
                    f.doSearch(text);
                } else {
                    f = ImojiSearchFragment.newInstance(text);
                    a.getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, f, ImojiSearchFragment.FRAGMENT_TAG).commitAllowingStateLoss();
                }

            }

        }
    }


    private void popBackstack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public void addText(String message) {
        if (mAdapter != null) {
            TextMessage tm = new TextMessage();
            tm.mMessage = message;
            mAdapter.add(tm);
            mMessageLv.smoothScrollToPosition(mAdapter.getCount() - 1);
        }

    }

    @Override
    public void addImoji(Imoji imoji) {

        if (mAdapter != null) {
            ImojiMessage im = new ImojiMessage();
            im.mImoji = imoji;
            mAdapter.add(im);
            mMessageLv.smoothScrollToPosition(mAdapter.getCount() - 1);
        }

    }


    private class MessageAdapter extends ArrayAdapter<Message> {

        private LayoutInflater mInflater;

        public MessageAdapter(Context context, int resource, List<Message> items) {
            super(context, resource, items);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                int layout = getItemViewType(position) == Message.IMOJI ? R.layout.imoji_image_item : R.layout.text_item;
                convertView = mInflater.inflate(layout, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            Message msg = getItem(position);
            bindMessage(msg, holder);


            return convertView;
        }

        private void bindMessage(Message msg, ViewHolder vh) {
            if (msg.getType() == Message.IMOJI) {
                bindImoji((ImojiMessage) msg, vh);
            }else if (msg.getType() == Message.TEXT) {
                bindText((TextMessage) msg, vh);
            }
        }

        private void bindImoji(ImojiMessage msg, ViewHolder vh) {
            Picasso.with(TriggerActivity.this).load(msg.mImoji.getThumbUrl()).into(vh.mImojiIv);
        }

        private void bindText(TextMessage msg, ViewHolder vh) {
            vh.mTextView.setText(msg.mMessage);
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).getType();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }

    static class ViewHolder{
        TextView mTextView;
        ImageView mImojiIv;

        public ViewHolder(View v) {
            mTextView = (TextView) v.findViewById(R.id.tv_text);
            mImojiIv = (ImageView) v.findViewById(R.id.iv_imoji);
        }
    }

    private interface Message {
        int IMOJI = 0;
        int TEXT = 1;

        int getType();
    }

    private class ImojiMessage implements Message {

        Imoji mImoji;

        @Override
        public int getType() {
            return IMOJI;
        }
    }

    private class TextMessage implements Message {
        String mMessage;

        @Override
        public int getType() {
            return TEXT;
        }
    }

}
