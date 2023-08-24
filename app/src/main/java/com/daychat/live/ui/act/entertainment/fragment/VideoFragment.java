package com.daychat.live.ui.act.entertainment.fragment;

import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daychat.live.R;
import com.daychat.live.base.BaseMvpFragment;
import com.daychat.live.bean.VideoBean;
import com.daychat.live.contract.HomeContract;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.presenter.HomePresenter;
import com.daychat.live.ui.act.entertainment.fragment.adapter.VideoAdapter;
import com.daychat.live.ui.act.entertainment.fragment.adapter.VideoRecyclerViewAdapter;
import com.daychat.live.ui.act.entertainment.fragment.adapter.listener.OnItemChildClickListener;
import com.daychat.live.ui.adapter.chat.TagsAdapter;
import com.daychat.live.util.ViewUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import xyz.doikki.videocontroller.StandardVideoController;
import xyz.doikki.videocontroller.component.CompleteView;
import xyz.doikki.videocontroller.component.ErrorView;
import xyz.doikki.videocontroller.component.GestureView;
import xyz.doikki.videocontroller.component.TitleView;
import xyz.doikki.videocontroller.component.VodControlView;
import xyz.doikki.videoplayer.player.VideoView;

/**
 * 长视频页面
 */
public class VideoFragment extends BaseMvpFragment<HomePresenter> implements HomeContract.View, OnLoadMoreListener, OnItemChildClickListener {

    @BindView(R.id.rv_tags)
    RecyclerView rvTags;

    @BindView(R.id.rv_videos)
    RecyclerView rvVideos;


    @BindView(R.id.iv_close)
    ImageView ivClose;
    TagsAdapter tagsAdapter;

    //    VideoAdapter videoAdapter;
    VideoRecyclerViewAdapter mAdapter;
    protected VideoView mVideoView;
    protected StandardVideoController mController;
    protected ErrorView mErrorView;
    protected CompleteView mCompleteView;
    protected TitleView mTitleView;

    protected LinearLayoutManager mLinearLayoutManager;
    /**
     * 当前播放的位置
     */
    protected int mCurPos = -1;
    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    protected int mLastPos = mCurPos;

    private HomePresenter homePresenter;
    private int[] colors;

    private List<ShortVideo> videoBeans = new ArrayList<>();
    private Integer page = 1;

    private int currPlayPosition;

    @Override
    protected void lazyLoad() {
        homePresenter = new HomePresenter();
        homePresenter.attachView(this);
        homePresenter.getRandomList(String.valueOf(page), 1);
    }

    @Override
    protected void initView(View view) {
        colors = new int[]{getContext().getResources().getColor(R.color.tag_color1), getContext().getResources().getColor(R.color.tag_color2), getContext().getResources().getColor(R.color.tag_color3), getContext().getResources().getColor(R.color.tag_color4), getContext().getResources().getColor(R.color.tag_color5), getContext().getResources().getColor(R.color.tag_color6), getContext().getResources().getColor(R.color.tag_color7), getContext().getResources().getColor(R.color.tag_color8)};
        rvTags.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tagsAdapter = new TagsAdapter(colors);
        String[] tags = new String[]{"# 한글", "# HIPHOP", "# 맛집", "# 스위스여행", "# 한글"};
        rvTags.setAdapter(tagsAdapter);
        tagsAdapter.setItems(Arrays.asList(tags));
        initVideoView();
//        List<VideoBean> videoBeans = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            videoBeans.add(new VideoBean());
//        }

        ivClose.setOnClickListener(v -> {
//            int count = getChildFragmentManager().getBackStackEntryCount();
            int pCount = getParentFragmentManager().getBackStackEntryCount();
            if (pCount > 0) {
                getParentFragmentManager().popBackStack();
            }
        });
//        videoAdapter = new VideoAdapter();
        mAdapter = new VideoRecyclerViewAdapter();
        mAdapter.setOnItemChildClickListener(this);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvVideos.setLayoutManager(mLinearLayoutManager);
        rvVideos.setAdapter(mAdapter);
        rvVideos.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
//                FrameLayout playerContainer = view.findViewById(R.id.player_container);
            }
        });


    }

    protected void startPlay(int position) {
        if (mCurPos == position) return;
        if (mCurPos != -1) {
            releaseVideoView();
        }
        ShortVideo videoBean = videoBeans.get(position);
        //边播边存
//        String proxyUrl = ProxyVideoCacheManager.getProxy(getActivity()).getProxyUrl(videoBean.getUrl());
//        mVideoView.setUrl(proxyUrl);

        mVideoView.setUrl(videoBean.getPlay_url());
        mTitleView.setTitle(videoBean.getTitle());
        View itemView = mLinearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoRecyclerViewAdapter.VideoHolder viewHolder = (VideoRecyclerViewAdapter.VideoHolder) itemView.getTag();
        //把列表中预置的PrepareView添加到控制器中，注意isDissociate此处只能为true, 请点进去看isDissociate的解释
        mController.addControlComponent(viewHolder.mPrepareView, true);
        ViewUtils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, "list");
        mVideoView.start();
        mCurPos = position;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.release();
    }

    private void releaseVideoView() {
        mVideoView.release();
        if (mVideoView.isFullScreen()) {
            mVideoView.stopFullScreen();
        }
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mCurPos = -1;
    }

    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mVideoView.setOnStateChangeListener(new VideoView.SimpleOnStateChangeListener() {
            @Override
            public void onPlayStateChanged(int playState) {
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    ViewUtils.removeViewFormParent(mVideoView);
                    mLastPos = mCurPos;
                    mCurPos = -1;
                }
            }
        });
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void getRandomList(BaseResponse<ArrayList<ShortVideo>> bean) {
        if (page == 1) {
            this.videoBeans.clear();
            rvVideos.post(() -> {
                //自动播放第一个
                startPlay(0);
            });
            mAdapter.setItems(bean.getData());
        } else {
            mAdapter.addDatas(bean.getData());
        }
        this.videoBeans.addAll(bean.getData());
//        System.out.println(bean.toString());
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }
}
