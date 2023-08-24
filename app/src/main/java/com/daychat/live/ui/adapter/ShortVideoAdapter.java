package com.daychat.live.ui.adapter;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daychat.live.R;
//import com.daychat.live.dialog.ShareDialog;
import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.util.HttpUtils;
import com.daychat.live.util.MyUserInstance;

import net.csdn.roundview.CircleImageView;
import net.csdn.roundview.RoundLinearLayout;

import com.daychat.live.widget.listvideo.ListVideoView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
//import com.daychat.live.ui.act.TopicShortVideoActivity;
import com.meiyinzb.nasinet.utils.DipPxUtils;


import java.util.ArrayList;
import java.util.List;


/**
 * @author ArcherYc
 * @date on 2018/9/12  下午3:19
 * @mail 247067345@qq.com
 */
public class ShortVideoAdapter extends RecyclerView.Adapter<ShortVideoAdapter.VideoViewHolder> {

    public List<ShortVideo> dataList = new ArrayList<>();
    private final Activity mContext;
    private CommentListener commentListener;


    public interface CommentListener {
        void onClick(ShortVideo videoEntity);

        void onVideoClick();

        void onZanClick(String count);

        void onAvatarClick(UserRegist author);

        void onAttend(String author_id, String type);

    }

    public void setCommentListener(CommentListener commentListener) {
        this.commentListener = commentListener;
    }

    public ShortVideoAdapter(Activity context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_video, parent, false);
        return new VideoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {

        ShortVideo videoEntity = dataList.get(position);
        VideoViewHolder viewHolder = (VideoViewHolder) holder;

        holder.mPosition = position;
        Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(videoEntity.getThumb_url()).into(viewHolder.sdvCover);

        if (null != videoEntity.getAuthor().getAvatar()) {
            Glide.with(mContext).load(videoEntity.getAuthor().getAvatar()).into(viewHolder.civ_avatar);
            Glide.with(mContext).load(videoEntity.getAuthor().getAvatar()).into(holder.ivUserHeader);
        }
        viewHolder.civ_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != commentListener) {
                    commentListener.onAvatarClick(videoEntity.getAuthor());
                }
            }
        });
        viewHolder.tvUserName.setText(videoEntity.getAuthor().getNick_name());
        viewHolder.tv_zan_num.setText(videoEntity.getLike_count());
        viewHolder.tv_pinglun_num.setText(videoEntity.getComment_count());
        viewHolder.tv_zhuan_num.setText(videoEntity.getShare_count());
//        viewHolder.tv_name.setText("@" + videoEntity.getAuthor().getNick_name() + position);
//        viewHolder.tv_title.setText(videoEntity.getTitle());

        if (videoEntity.getTopic() != null) {

            SpannableString spString = new SpannableString(videoEntity.getTopic());
            spString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
//                    Intent intent = new Intent(mContext, TopicShortVideoActivity.class);
//                    intent.putExtra("topic", spString.toString());
//                    mContext.startActivity(intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setTextSize(DipPxUtils.sp2px(mContext, 15));//设置字体大小
                    ds.setColor(mContext.getResources().getColor(R.color.color_FF6273));//设置字体颜色
                }
            }, 0, videoEntity.getTopic().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            viewHolder.tv_title.setText(spString);
//            viewHolder.tv_title.setMovementMethod(LinkMovementMethod.getInstance());
//            viewHolder.tv_title.append(" " + videoEntity.getTitle());

        } else {
//            viewHolder.tv_title.setText(videoEntity.getTitle());
        }


        viewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyUserInstance.getInstance().visitorIsLogin()) {
                    if (null != commentListener) {
                        commentListener.onClick(videoEntity);
                    }
                }
            }
        });


        if (null == videoEntity.getIs_like()) {
            Glide.with(mContext).load(R.mipmap.zan).into(holder.iv_zan);
        } else {
            if (videoEntity.getIs_like().equals("0")) {
                Glide.with(mContext).load(R.mipmap.zan).into(holder.iv_zan);
            } else {
//                Glide.with(mContext).load(R.mipmap.zan_pre).into(holder.iv_zan);
                holder.tv_zan_num.setText(videoEntity.getLike_count());
            }
        }


        if (videoEntity.getAuthor().getIsattent() == 0) {

//            Glide.with(mContext).load(R.mipmap.short_guanzhu).into(viewHolder.im_guanzhu);
        } else {

//            Glide.with(mContext).load(R.mipmap.short_yiguanzhu).into(viewHolder.im_guanzhu);

        }

        viewHolder.im_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyUserInstance.getInstance().visitorIsLogin()) {

                    if (videoEntity.getAuthor().getIsattent() == 0) {
                        attendAnchor(videoEntity.getAuthor().getId(), "1", holder);
                    } else {
                        attendAnchor(videoEntity.getAuthor().getId(), "0", holder);

                    }
                }
            }
        });

        holder.ll_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyUserInstance.getInstance().visitorIsLogin()) {

                }
            }
        });


        viewHolder.ll_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyUserInstance.getInstance().visitorIsLogin()) {
                    String is_like = videoEntity.getIs_like();
                    String temp = "0";
                    if (is_like == null || is_like.equals("0")) {
                        temp = "1";
                    } else {
                        temp = "0";

                    }
                    String finalTemp = temp;
                    HttpUtils.getInstance().likeVideo(videoEntity.getId(), temp, new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            JSONObject data = HttpUtils.getInstance().check(response);
                            if (data.get("status").toString().equals("0")) {
                                if (null != commentListener) {
                                    if (finalTemp.equals("0")) {

                                        Glide.with(mContext).load(R.mipmap.zan).into(holder.iv_zan);
                                        holder.tv_zan_num.setText(data.getJSONObject("data").getString("like_count"));
                                        dataList.get(position).setLike_count(data.getJSONObject("data").getString("like_count"));
                                        dataList.get(position).setIs_like("0");
                                        if (commentListener != null) {
                                            commentListener.onZanClick(data.getJSONObject("data").getString("like_count"));
                                        }
                                    } else {
//                                        Glide.with(mContext).load(R.mipmap.zan_pre).into(holder.iv_zan);
                                        holder.tv_zan_num.setText(data.getJSONObject("data").getString("like_count"));
                                        dataList.get(position).setLike_count(data.getJSONObject("data").getString("like_count"));
                                        dataList.get(position).setIs_like("1");
                                        if (commentListener != null) {
                                            commentListener.onZanClick(data.getJSONObject("data").getString("like_count"));
                                        }
                                    }

                                }
                            }
                        }
                    });

                }
            }
        });

        holder.llChat.setOnClickListener(v -> {
            int px = AdaptScreenUtils.pt2Px(2400);
            BottomDialog dialog = BottomDialog.build().setMinHeight(px);
            dialog.setCustomView(new OnBindView<BottomDialog>(R.layout.dialog_chat_person) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            });
            dialog.show();
//            FullScreenDialog fullScreenDialog = new FullScreenDialog(new OnBindView<FullScreenDialog>() {
//                @Override
//                public void onBind(FullScreenDialog dialog, View v) {
//
//                }
//            });
//            int px = AdaptScreenUtils.pt2Px(2400);
//            fullScreenDialog.setMaxHeight(px);
//            fullScreenDialog.show();
//            if (holder.videoView.isPlaying()) {
//                holder.videoView.pause();
//                viewHolder.iv_pause.setVisibility(View.VISIBLE);
//            }

        });
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentListener != null) {
                    commentListener.onVideoClick();
                }
            }
        });

    }

    private void attendAnchor(String anchorid, String type, VideoViewHolder holder) {

        HttpUtils.getInstance().attentAnchor(anchorid, type, new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {

                JSONObject data = HttpUtils.getInstance().check(response);
                if (data.get("status").toString().equals("0")) {
                    if (null != commentListener) {
                        commentListener.onAttend(anchorid, type);
                    }
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addData(List<ShortVideo> newDataList) {
        this.dataList.addAll(newDataList);
    }

    public ShortVideo getDataByPosition(int position) {
        return dataList.get(position);
    }

    public void cleanData() {
        if (dataList != null) {
            dataList.clear();
        }
        notifyDataSetChanged();
    }

    public int getVideoSize() {
        return dataList.size();
    }

    public List<ShortVideo> getDataList(){
        return dataList;
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {

        public int mPosition;

        public FrameLayout mPlayerContainer;
        public ListVideoView videoView;
        public ImageView sdvCover;
        public CircleImageView civ_avatar;
        RoundLinearLayout llChat;

        public TextView tvUserName;

        public CircleImageView ivUserHeader;
        public ImageView im_guanzhu, iv_zan, iv_pause;
        public TextView tv_zan_num, tv_pinglun_num, tv_zhuan_num; //tv_name, tv_title;
        public LinearLayout ll_comment, ll_zan, ll_zhuanfa;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ivUserHeader = itemView.findViewById(R.id.iv_user_header);
            mPlayerContainer = itemView.findViewById(R.id.container);
            videoView = itemView.findViewById(R.id.tiktok_View);
            sdvCover = itemView.findViewById(R.id.sdv_cover);
            civ_avatar = itemView.findViewById(R.id.civ_avatar);
            llChat = itemView.findViewById(R.id.chat_layout);
            im_guanzhu = itemView.findViewById(R.id.im_guanzhu);
            tv_zan_num = itemView.findViewById(R.id.tv_zan_num);
            tv_pinglun_num = itemView.findViewById(R.id.tv_pinglun_num);
            tv_zhuan_num = itemView.findViewById(R.id.tv_zhuan_num);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
//            tv_name = itemView.findViewById(R.id.tv_name);
//            tv_title = itemView.findViewById(R.id.tv_title);
            ll_comment = itemView.findViewById(R.id.ll_comment);
            iv_zan = itemView.findViewById(R.id.iv_zan);
            ll_zan = itemView.findViewById(R.id.ll_zan);
            ll_zhuanfa = itemView.findViewById(R.id.ll_zhuanfa);
            iv_pause = itemView.findViewById(R.id.iv_pause);
            itemView.setTag(this);
        }
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
