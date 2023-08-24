package com.daychat.live.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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


import com.daychat.live.R;

import java.io.IOException;
import java.io.InputStream;

public class ImageSelectorPanel extends LinearLayout {
    private final Context mContext;
    private final RecyclerView mImageListView;
    private OnImageSelectedListener mOnImageSelectedListener;

    private static final String[] IMAGE_PATHS = {"1960s", "camomile", "candy", "cold", "dark"};

    public ImageSelectorPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.panel_image_selector, this);
        mImageListView = view.findViewById(R.id.recycler_paint_image);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mImageListView.setLayoutManager(layoutManager);
        mImageListView.setAdapter(new ImageListAdapter());
    }

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        mOnImageSelectedListener = listener;
    }

    public interface OnImageSelectedListener {
        void onImageSelected(Drawable drawable);
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

    private class ImageListAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View contactView = inflater.inflate(R.layout.filter_item, parent, false);
            ItemViewHolder viewHolder = new ItemViewHolder(contactView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ItemViewHolder holder, int position) {
            try {
                final String imagePath = "filters/" + IMAGE_PATHS[position] + "/thumb.png";
                InputStream is = mContext.getAssets().open(imagePath);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                holder.mName.setVisibility(GONE);
                holder.mIcon.setImageBitmap(bitmap);
                holder.mIcon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnImageSelectedListener != null) {
                            mOnImageSelectedListener.onImageSelected(holder.mIcon.getDrawable());
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return IMAGE_PATHS.length;
        }
    }
}
