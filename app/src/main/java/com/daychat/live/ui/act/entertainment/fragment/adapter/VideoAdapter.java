package com.daychat.live.ui.act.entertainment.fragment.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;
import com.daychat.live.R;
import com.daychat.live.bean.VideoBean;
import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.ui.act.entertainment.callback.VideoItemPlayCallback;
import com.daychat.live.widget.listvideo.ListVideoView;

import xyz.doikki.videocontroller.component.PrepareView;

public class VideoAdapter extends BaseQuickAdapter<ShortVideo, QuickViewHolder> {
    private VideoItemPlayCallback videoItemPlayCallback;

    public void setVideoItemPlayCallback(VideoItemPlayCallback videoItemPlayCallback) {
        this.videoItemPlayCallback = videoItemPlayCallback;
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable ShortVideo videoBean) {
//        ListVideoView videoView = quickViewHolder.getView(R.id.video);
        PrepareView pv = quickViewHolder.getView(R.id.prepare_view);
        ImageView thumb = pv.findViewById(R.id.thumb);
        Glide.with(getContext()).load(videoBean.getPlay_url()).into(thumb);
        ImageView ivHeader = quickViewHolder.getView(R.id.iv_header);
        Glide.with(getContext()).load(videoBean.getAuthor().getAvatar()).into(ivHeader);
//        videoView.setVideoPath(videoBean.getPlay_url());
//        ImageView ivPlay = quickViewHolder.getView(R.id.iv_play);
//        ivPlay.setOnClickListener(v->{
//            videoView.start();
//            if (videoItemPlayCallback!=null){
//                videoItemPlayCallback.release(i,videoBean.getPlay_url());
//            }
//        });
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_long_video,viewGroup);
    }
}
