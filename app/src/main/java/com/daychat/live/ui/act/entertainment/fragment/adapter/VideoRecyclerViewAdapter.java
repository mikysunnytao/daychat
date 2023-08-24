package com.daychat.live.ui.act.entertainment.fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daychat.live.R;
import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.ui.act.entertainment.fragment.adapter.listener.OnItemChildClickListener;
import com.daychat.live.ui.act.entertainment.fragment.adapter.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import xyz.doikki.videocontroller.component.PrepareView;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.VideoHolder> {

    private List<ShortVideo> videos;

    private OnItemChildClickListener mOnItemChildClickListener;

    private OnItemClickListener mOnItemClickListener;

    @Override
    @NonNull
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_long_video, parent, false);
        return new VideoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {

        ShortVideo videoBean = videos.get(position);

        String videoUrl;
        if (videoBean.getThumb_url()==null || videoBean.getThumb_url().equals("")){
            videoUrl = videoBean.getPlay_url();
        }else {
            videoUrl = videoBean.getThumb_url();
        }
        Glide.with(holder.mThumb.getContext())
                .load(videoUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.mThumb);
        Glide.with(holder.ivHeader.getContext()).load(videoBean.getAuthor().getAvatar()).into(holder.ivHeader);
        holder.mTitle.setText(videoBean.getTitle());

        holder.mPosition = position;
    }

    @Override
    public int getItemCount() {
        return videos==null?0:videos.size();
    }

    public void addData(List<ShortVideo> videoList) {
        int size = videos.size();
        videos.addAll(videoList);
        //使用此方法添加数据，使用notifyDataSetChanged会导致正在播放的视频中断
        notifyItemRangeChanged(size, videos.size());
    }

    public void setItems(ArrayList<ShortVideo> data) {
        this.videos = data;
        notifyDataSetChanged();
    }

    public void addDatas(List<ShortVideo> data){
        this.videos.addAll(data);
        notifyItemRangeInserted(this.videos.size()-data.size(),data.size());
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public int mPosition;
        public FrameLayout mPlayerContainer;
        public TextView mTitle;
        public ImageView mThumb;

        public ImageView ivHeader;
        public PrepareView mPrepareView;

        VideoHolder(View itemView) {
            super(itemView);
            mPlayerContainer = itemView.findViewById(R.id.player_container);
            mTitle = itemView.findViewById(R.id.tv_title);
            mPrepareView = itemView.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);
            ivHeader = itemView.findViewById(R.id.iv_header);
            if (mOnItemChildClickListener != null) {
                mPlayerContainer.setOnClickListener(this);
            }
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            //通过tag将ViewHolder和itemView绑定
            itemView.setTag(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_container) {
                if (mOnItemChildClickListener != null) {
                    mOnItemChildClickListener.onItemChildClick(mPosition);
                }
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mPosition);
                }
            }

        }
    }


    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}