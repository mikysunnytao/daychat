package com.daychat.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daychat.live.util.Config;
import com.daychat.live.R;

public class GifSelectorPanel extends LinearLayout {
    private final Context mContext;
    private final RecyclerView mGifListView;
    private OnGifSelectedListener mOnGifSelectedListener;

    private static final String[] GIF_NAMES = {"test", "watermark"};

    public GifSelectorPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.panel_image_selector, this);
        mGifListView = view.findViewById(R.id.recycler_paint_image);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mGifListView.setLayoutManager(layoutManager);
        mGifListView.setAdapter(new GifListAdapter());
    }

    public void setOnGifSelectedListener(OnGifSelectedListener listener) {
        mOnGifSelectedListener = listener;
    }

    public interface OnGifSelectedListener {
        void onGifSelected(String gifPath);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIcon;
        public TextView mName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.icon);
            mName = itemView.findViewById(R.id.name);
        }
    }

    private class GifListAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.filter_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, final int position) {

            final String imagePath = Config.GIF_STICKER_DIR + GIF_NAMES[position] + ".gif";
            holder.mName.setText(GIF_NAMES[position]);
            Glide.with(mContext).load(imagePath).into(holder.mIcon);
            holder.mIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnGifSelectedListener != null) {
                        mOnGifSelectedListener.onGifSelected(imagePath);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return GIF_NAMES.length;
        }
    }
}
