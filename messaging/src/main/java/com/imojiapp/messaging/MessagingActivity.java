package com.imojiapp.messaging;

import android.app.ActionBar;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Imoji;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessagingActivity extends AppCompatActivity implements MessageInterface{

    private final Handler mHandler = new Handler();
    private FrameLayout mKeyboardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        mKeyboardContainer = (FrameLayout) findViewById(R.id.keyboard_container);
        mKeyboardContainer.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);

        getSupportFragmentManager().beginTransaction().add(R.id.keyboard_container, new KeyboardFragment()).commit();
    }


    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            mKeyboardContainer.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);


        }
    };

    @Override
    public void addText(String message) {

    }

    @Override
    public void addImoji(Imoji img) {

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
            Picasso.with(MessagingActivity.this).load(msg.mImoji.getThumbUrl()).into(vh.mImojiIv);
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
