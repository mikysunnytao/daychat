package com.daychat.live.ui.act.entertainment.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daychat.live.R;
import com.daychat.live.base.BaseMvpFragment;
import com.daychat.live.contract.HomeContract;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.Comment;
import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.presenter.HomePresenter;
import com.daychat.live.ui.adapter.ShortVideoAdapter;
import com.daychat.live.widget.Dialogs;
import com.daychat.live.widget.pagerlayoutmanager.OnViewPagerListener;
import com.daychat.live.widget.pagerlayoutmanager.ViewPagerLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class ShortVideoFragment extends BaseMvpFragment<HomePresenter> implements HomeContract.View, OnViewPagerListener, OnLoadMoreListener, ShortVideoAdapter.CommentListener {
    @BindView(R.id.rv_short)
    RecyclerView rv_short;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.fl_close)
    FrameLayout flClose;

    ViewPagerLayoutManager pagerLayoutManager;
    ShortVideoAdapter videoAdapter;
    int type = 0;
    int page = 1;
    boolean first_play = true;
    boolean now_loadMore = true;
    boolean is_refresh = false;
    private Dialog dialog;
    public boolean action = false;
    String lastid = "";

    HashMap<String, ArrayList> hs_attend = new HashMap();
    String comment_count = "0";
    private int position;
    private String video_id = "";

    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    private Integer videoType = 0;

    public ShortVideoFragment( int type, int videoType) {
        this.type = type;
        this.videoType = videoType;
    }

    public ShortVideoFragment() {
    }

    public void initType(Integer type) {
        this.videoType = type;
        if (videoAdapter != null) {
            this.page = 1;
            is_refresh = false;
            this.position = 0;
//            pauseVideo();
            videoAdapter.cleanData();
            mPresenter.getRandomList(String.valueOf(page), videoType);
        }
    }

    //刷新暂停时候的激活
    private void setIsAction(boolean is_action) {
        if (pagerLayoutManager == null) {
            return;
        }
        int snapPosition = pagerLayoutManager.findSnapPosition();
        if (snapPosition >= 0) {
            ShortVideoAdapter.VideoViewHolder viewHolder =
                    (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(snapPosition);
            if (viewHolder != null) {
                viewHolder.videoView.setIs_action(is_action);
            }
        }
    }

    @Override
    protected void initView(View view) {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        refreshLayout.setEnableLoadMore(false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        Fresco.initialize(getContext());
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        flClose.setOnClickListener(v->{
            FragmentManager manager = getParentFragmentManager();
            int count = manager.getBackStackEntryCount();
            if (count>0){
                manager.popBackStack();
            }
        });
        rv_short.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                final int action = e.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN://手指按下
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_MOVE://手指移动（从手指按下到抬起 move多次执行）
                        break;
                    case MotionEvent.ACTION_UP://手指抬起
                        if (rv_short.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING &&
                                pagerLayoutManager.findSnapPosition() == 0) {
                            if (rv_short.getChildAt(0).getY() == 0 &&
                                    rv_short.canScrollVertically(1)) {//下滑操作
                                rv_short.stopScroll();
                            }
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshData();
            }
        });
        initData();
    }

    private boolean inited = false;

    private void initData() {
        inited = true;
        // showLoading();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        isVisible = getUserVisibleHint();
        if (isVisibleToUser) {
//            if (videoAdapter != null) {
//
////                mPresenter.getRandomList(page + "", videoType);
//            }
        } else {
            if (videoAdapter != null) {
                pauseVideo();
            }
        }
    }


    @Override
    protected void lazyLoad() {
        mPresenter.getRandomList(page + "", videoType);
    }


    @Override
    public void getRandomList(BaseResponse<ArrayList<ShortVideo>> bean) {
        refreshLayout.finishRefresh();
        refreshLayout.finishRefresh();

        if (!is_refresh) {
            if (first_play) {
                pagerLayoutManager = new ViewPagerLayoutManager(getContext(), LinearLayoutManager.VERTICAL);
                pagerLayoutManager.setOnViewPagerListener(this);

                videoAdapter = new ShortVideoAdapter(getActivity());

                rv_short.setLayoutManager(pagerLayoutManager);

                rv_short.setAdapter(videoAdapter);
                videoAdapter.addData(bean.getData());
                setAttend(videoAdapter.dataList);
                videoAdapter.setCommentListener(this);
//                new Handler().postDelayed(() -> {
//                    playVideo(0);
//                }, 500);
                is_refresh = false;
                page++;
            } else {
                now_loadMore = true;
                is_refresh = false;
                refreshLayout.finishLoadMore();

                if (null == bean.getData()) {
                    return;
                }
                if (bean.getData().size() == 0) {
                    refreshLayout.setEnableLoadMore(false);
                    return;
                }
                if (page ==1){

                }
                int now_size = videoAdapter.getVideoSize();

                videoAdapter.addData(bean.getData());
                setAttend(videoAdapter.dataList);
                videoAdapter.notifyItemRangeChanged(now_size, bean.getData().size());
                if (page == 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playVideo(0);
                        }
                    }, 500);
                }
                page++;
            }
        } else {
            is_refresh = false;
            now_loadMore = true;
            if (null == bean.getData()) {
                return;
            }
            if (bean.getData().size() == 0) {
                return;
            }
            videoAdapter.addData(bean.getData());
            videoAdapter.notifyDataSetChanged();
            setAttend(videoAdapter.dataList);
            rv_short.scrollToPosition(0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playVideo(0);
                }
            }, 500);
        }

    }


    private void setAttend(List<ShortVideo> beans) {
        hs_attend.clear();
        //初始化ID数组
        ArrayList temp_Ids = new ArrayList();
        for (int i = 0; i < beans.size(); i++) {
            temp_Ids.add(beans.get(i).getAuthor().getId());
        }
        //给ID去重
        HashSet set = new HashSet(temp_Ids);
        temp_Ids.clear();
        temp_Ids.addAll(set);
        //用去重的ID遍历 数据集
        for (int i = 0; i < temp_Ids.size(); i++) {
            ArrayList video_postion = new ArrayList();

            for (int y = 0; y < beans.size(); y++) {
                //当ID相同,加入对应的POSTION
                if (temp_Ids.get(i).equals(beans.get(y).getAuthor().getId())) {
                    video_postion.add(y);
                }
            }
            //加入HASHMAP,作为键值对储存
            hs_attend.put(temp_Ids.get(i).toString(), video_postion);

        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_short_video;
    }


    @Override
    public void showLoading() {
        hideLoading();
        dialog = Dialogs.createLoadingDialog(getContext());
        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        now_loadMore = true;
        if (null != refreshLayout) {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onInitComplete() {
        if (type == 0) {
//            if (action) {
            playVideo(0);
            first_play = false;
//            }
        }

    }

    @Override
    public void onPageSelected(int position, boolean isBottom) {
        if (this.position == position) {
            return;
        }
        if (position > 0) {
            refreshLayout.setEnableRefresh(false);
        }

        ShortVideoAdapter.VideoViewHolder viewHolder = (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(position);
        if (viewHolder != null) {
            viewHolder.iv_pause.setVisibility(View.GONE);

            if (videoAdapter.dataList.get(position).getAuthor().getIsattent() == 0) {

//                Glide.with(getContext()).load(R.mipmap.short_guanzhu).into(viewHolder.im_guanzhu);
            } else {

//                Glide.with(getContext()).load(R.mipmap.short_yiguanzhu).into(viewHolder.im_guanzhu);

            }
        }

        playVideo(position);
        if (null != videoAdapter) {
            if (videoAdapter.getVideoSize() >= 20 && videoAdapter.getVideoSize() % 20 == 0) {//说明后台有足够数据
                if (videoAdapter.getVideoSize() - (position + 1) < 6) {
                    if (now_loadMore) {
                        if (this.position < position) {
                            mPresenter.getRandomList(page + "", videoType);
                            now_loadMore = false;
                        }
                    }
                }
            }
        }

        this.position = position;
    }

    @Override
    public void onPageRelease(boolean isNext, int position) {
        releaseVideo(position);
    }


    public void checkPlay() {
        if (videoAdapter.getVideoSize() == 0) {
            return;
        }
        if (first_play) {
            if (type != 0) {
                playVideo(0);
                first_play = false;
            }
        } else {
            restartVideo();
        }
    }

    private void playVideo(int position) {
        if (position == 0) {
            refreshLayout.setEnableRefresh(true);
        }
        if (videoAdapter.getVideoSize() == 0) {
            return;
        }

        final ShortVideoAdapter.VideoViewHolder viewHolder = (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(position);
        ShortVideo videoEntity = videoAdapter.getDataByPosition(position);
        if (viewHolder != null && !viewHolder.videoView.isPlaying()) {
            viewHolder.iv_pause.setVisibility(View.GONE);
            viewHolder.videoView.setVideoPath(videoEntity.getPlay_url());
            viewHolder.videoView.getMediaPlayer().setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
                    if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        viewHolder.sdvCover.setVisibility(View.GONE);
                    }
                    return false;
                }
            });

            viewHolder.videoView.setLooping(true);
            viewHolder.videoView.prepareAsync();
        }
    }

    private void releaseVideo(int position) {
        ShortVideoAdapter.VideoViewHolder viewHolder = (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(position);
        if (viewHolder != null) {

            viewHolder.videoView.stopPlayback();
            viewHolder.sdvCover.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseVideo();
        setIsAction(false);
        action = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        setIsAction(true);
        if (action) {
            restartVideo();
        }
    }


    public void onResume2() {
        setIsAction(true);
        if (action) {
            restartVideo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    /**
     * 暂停视频
     */
    private void pauseVideo() {
        if (pagerLayoutManager == null) {
            return;
        }
        int snapPosition = pagerLayoutManager.findSnapPosition();
        if (snapPosition >= 0) {
            ShortVideoAdapter.VideoViewHolder viewHolder =
                    (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(snapPosition);
            if (viewHolder != null) {

                viewHolder.videoView.pause();
            }
        }
    }


    /**
     * 点击暂停/回复
     */
    private void clickVideo() {
        int snapPosition = pagerLayoutManager.findSnapPosition();

        if (snapPosition >= 0) {
            ShortVideoAdapter.VideoViewHolder viewHolder =
                    (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(snapPosition);
            if (viewHolder != null) {
                if (viewHolder.videoView.isPlaying()) {
                    viewHolder.videoView.pause();
                    viewHolder.iv_pause.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.videoView.start();
                    viewHolder.iv_pause.setVisibility(View.GONE);
                }

            }
        }
    }

    /**
     * 暂停后重新播放视频
     */
    private void restartVideo() {
        if (pagerLayoutManager != null) {
            int snapPosition = pagerLayoutManager.findSnapPosition();
            if (snapPosition >= 0) {
                ShortVideoAdapter.VideoViewHolder viewHolder =
                        (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(snapPosition);
                if (viewHolder != null) {
                    viewHolder.videoView.start();
                    viewHolder.iv_pause.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.finishLoadMore();
        refreshLayout.setEnableLoadMore(false);
    }


    public void refreshData() {
        page = 1;
        refreshLayout.setEnableLoadMore(false);
        now_loadMore = true;
        if (videoAdapter != null) {
            videoAdapter.cleanData();
        }

        //   showLoading();
        mPresenter.getRandomList(page + "", videoType);
        pauseVideo();
        is_refresh = true;
    }

    @Override
    public void onClick(ShortVideo videoEntity) {
        //   showLoading();
        mPresenter.getComments(lastid, videoEntity.getId());
        comment_count = videoEntity.getComment_count();
        video_id = videoEntity.getId();
    }

    @Override
    public void onVideoClick() {
        clickVideo();
    }

    @Override
    public void onZanClick(String count) {

    }

    @Override
    public void onAvatarClick(UserRegist author) {
//        startActivityForResult(new Intent(getContext(), ShortVideoCenterActivity.class).putExtra("authorInfo", author), 0x002);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x002 && resultCode == 0x001) {
            //当LoginActivity finish后，就会调用这里，data为值
            String author_id = data.getStringExtra("author_id");
            String type = data.getStringExtra("type");
            if (author_id == null) {
                return;
            }
            if (type == null) {
                return;
            }
            onAttend(author_id, type);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onAttend(String author_id, String type) {
        //iterating over keys only
        for (String key : hs_attend.keySet()) {
            if (key.equals(author_id)) {
                for (int i = 0; i < hs_attend.get(key).size(); i++) {
                    videoAdapter.dataList.get(Integer.parseInt(hs_attend.get(key).get(i).toString())).getAuthor().setIsattent(Integer.valueOf(type));
                }
                break;
            }
        }

        if (pagerLayoutManager != null) {
            int snapPosition = pagerLayoutManager.findSnapPosition();
            if (snapPosition >= 0) {
                ShortVideoAdapter.VideoViewHolder viewHolder =
                        (ShortVideoAdapter.VideoViewHolder) rv_short.findViewHolderForLayoutPosition(snapPosition);
                if (type.equals("0")) {
//                    Glide.with(this).load(R.mipmap.short_guanzhu).into(viewHolder.im_guanzhu);
                } else {
//                    Glide.with(this).load(R.mipmap.short_yiguanzhu).into(viewHolder.im_guanzhu);
                }
            }
        }


    }





    @Override
    public void getComments(BaseResponse<ArrayList<Comment>> bean) {

        if (bean == null) {
            return;
        }


    }


}
