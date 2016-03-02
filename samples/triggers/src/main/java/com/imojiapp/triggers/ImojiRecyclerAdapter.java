package com.imojiapp.triggers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.imoji.sdk.objects.RenderingOptions;
import io.imoji.sdk.objects.Imoji;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImojiRecyclerAdapter extends RecyclerView.Adapter<ImojiRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Imoji> mItems;

    public ImojiRecyclerAdapter(Context context) {
        this(context, new ArrayList<Imoji>());
    }

    public ImojiRecyclerAdapter(Context context, List<Imoji> items) {
        super();
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItems = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.imoji_item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Imoji imoji = mItems.get(position);
        Picasso.with(mContext).load(imoji.urlForRenderingOption(RenderingOptions.borderedPngThumbnail())).into(holder.mImojiIv);

    }

    public void setList(List<Imoji> imojis) {
        mItems = imojis;
        notifyDataSetChanged();
    }

    public Imoji getItemAt(int position) {
        return mItems.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImojiIv;

        public ViewHolder(View itemView) {
            super(itemView);
            mImojiIv = (ImageView) itemView.findViewById(R.id.iv_imoji);
        }
    }
}

