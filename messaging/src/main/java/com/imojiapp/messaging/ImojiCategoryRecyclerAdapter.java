package com.imojiapp.messaging;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imojiapp.imoji.sdk.Imoji;
import com.imojiapp.imoji.sdk.ImojiCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sajjadtabib on 8/25/15.
 */
public class ImojiCategoryRecyclerAdapter extends RecyclerView.Adapter<ImojiCategoryRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<ImojiCategory> mImojiCategories;
    private Context mContext;

    public ImojiCategoryRecyclerAdapter(Context context) {
        this(context, null);
    }

    public ImojiCategoryRecyclerAdapter(Context context, List<ImojiCategory> imojiCategories) {
        mImojiCategories = imojiCategories;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.category_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImojiCategory category = mImojiCategories.get(position);
        Picasso.with(mContext).load(category.getImoji().getImageUrl(Imoji.ImageFormat.Png, Imoji.ImageSize.ImageSizeThumbnail)).into(holder.mImojiIv);
        holder.mTitleTv.setText(category.getTitle());
    }

    public ImojiCategory getItemAt(int position) {
        return mImojiCategories.get(position);
    }

    @Override
    public int getItemCount() {
        return mImojiCategories != null ? mImojiCategories.size() : 0;
    }

    public void setImojiCategories(List<ImojiCategory> imojiCategories) {
        mImojiCategories = imojiCategories;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImojiIv;

        TextView mTitleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mImojiIv = (ImageView) itemView.findViewById(R.id.iv_imoji);
            mTitleTv = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
