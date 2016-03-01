package com.imojiapp.messaging;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imoji.sdk.RenderingOptions;
import com.imoji.sdk.objects.Category;
import com.imoji.sdk.objects.Imoji;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sajjadtabib on 8/25/15.
 */
public class ImojiCategoryRecyclerAdapter extends RecyclerView.Adapter<ImojiCategoryRecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<Category> mImojiCategories;
    private Context mContext;

    public ImojiCategoryRecyclerAdapter(Context context) {
        this(context, null);
    }

    public ImojiCategoryRecyclerAdapter(Context context, List<Category> imojiCategories) {
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
        Category category = mImojiCategories.get(position);
        Imoji imoji = category.getPreviewImojis().iterator().next();
        Picasso.with(mContext).load(imoji.urlForRenderingOption(RenderingOptions.borderedPngThumbnail())).into(holder.mImojiIv);
        holder.mTitleTv.setText(category.getTitle());
    }

    public Category getItemAt(int position) {
        return mImojiCategories.get(position);
    }

    @Override
    public int getItemCount() {
        return mImojiCategories != null ? mImojiCategories.size() : 0;
    }

    public void setImojiCategories(List<Category> imojiCategories) {
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
