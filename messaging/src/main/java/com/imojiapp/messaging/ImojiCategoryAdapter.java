package com.imojiapp.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sajjadtabib on 8/25/15.
 */
public class ImojiCategoryAdapter extends ArrayAdapter<ImojiCategory> {

    private LayoutInflater mInflater;

    public ImojiCategoryAdapter(Context context, int resource, List<ImojiCategory> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.category_item_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImojiCategory category = getItem(position);
        Picasso.with(getContext()).load(category.getImoji().getImageUrl(Imoji.ImageFormat.Png, Imoji.ImageSize.ImageSizeThumbnail)).into(holder.mImojiIv);
        holder.mTitleTv.setText(category.getTitle());

        return convertView;
    }

    /**
     * Created by sajjadtabib on 8/25/15.
     */
    public static class ViewHolder {

        ImageView mImojiIv;

        TextView mTitleTv;

        public ViewHolder(View v) {
            mImojiIv = (ImageView) v.findViewById(R.id.iv_imoji);
            mTitleTv = (TextView) v.findViewById(R.id.tv_title);
        }
    }
}
