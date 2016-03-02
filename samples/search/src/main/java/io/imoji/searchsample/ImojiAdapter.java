package io.imoji.searchsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.imoji.sdk.objects.Imoji;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sajjadtabib on 5/4/15.
 */
public class ImojiAdapter extends ArrayAdapter<Imoji> {

    private LayoutInflater mInflater;

    public ImojiAdapter(Context context, int resource, List<Imoji> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.imoji_item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Imoji item = getItem(position);
        Picasso.with(getContext()).load(item.getStandardThumbnailUri()).into(holder.mImojiIv);

        return convertView;
    }

    static class ViewHolder {

        ImageView mImojiIv;

        public ViewHolder(View v) {
            mImojiIv = (ImageView) v.findViewById(R.id.iv_imoji);

        }
    }
}
