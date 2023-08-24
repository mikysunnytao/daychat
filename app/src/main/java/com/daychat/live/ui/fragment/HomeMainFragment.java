package com.daychat.live.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.bean.MenuBean;
import com.daychat.live.eventbus.UnReadBus;
import com.daychat.live.ui.act.HomeActivity;
import com.daychat.live.ui.act.entertainment.fragment.ImageTextFragment;
import com.daychat.live.ui.act.entertainment.fragment.VideoFragment;
import com.daychat.live.ui.act.entertainment.fragment.ShortVideoFragment;
import com.daychat.live.ui.adapter.BannerTextViewHolder;
import com.daychat.live.ui.adapter.MenuAdapter;
import com.daychat.live.ui.fragment.chat.ChatNavFragment;
import com.daychat.live.ui.fragment.chat.ChatTagsFragment;
import com.daychat.live.ui.fragment.chat.HotChatFragment;
import com.daychat.live.ui.fragment.chat.MyChatFragment;
import com.daychat.live.util.DpUtil;
import net.csdn.roundview.RoundFrameLayout;
import net.csdn.roundview.RoundTextView;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.RectangleIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeMainFragment extends BaseFragment {


    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.rv_menus)
    RecyclerView rvMenus;

    @BindView(R.id.fl_title)
    FrameLayout flTitle;

    @BindView(R.id.short_video)
    LinearLayout llShortVideo;

    @BindView(R.id.ll_video)
    LinearLayout llVideo;

    @BindView(R.id.ll_chat)
    LinearLayout llChat;
    @BindView(R.id.ll_picture)
    LinearLayout llPicture;
    private MenuAdapter menuAdapter;

    @BindView(R.id.ll_tags)
    LinearLayout llTags;

    @BindView(R.id.ll_group_chat)
    LinearLayout llGroupChat;

    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.home_main)
    LinearLayout homeMain;

    @BindView(R.id.rt_message_num)
    RoundTextView rtvMessageNum;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
//    MyScrollView myScrollView;

//    @BindView(R.id.main_message)
//    RoundLinearLayout mainMessage;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnreadCount(UnReadBus message) {
        if (message.unread > 0) {
            rtvMessageNum.setVisibility(View.VISIBLE);
            rtvMessageNum.setText(String.valueOf(message.unread));
//            tv_red_hot.setVisibility(View.VISIBLE);
//            tv_red_hot.setText(String.valueOf(message.unread));
        } else {
            rtvMessageNum.setVisibility(View.GONE);
//            tv_red_hot.setVisibility(View.GONE);
        }
    }


    @Override
    protected void lazyLoad() {
        ivSearch.setOnClickListener(v -> {
            hideMenu();
            showSearchPop(v);
        });

        homeMain.setOnClickListener((v)->{
            ((HomeActivity)getActivity()).hideMenu();
        });
//        ivMenu.setOnClickListener(v -> {
//            hideMenu();
//            startActivity(new Intent(getContext(), MyActivity.class));
//            getActivity().overridePendingTransition(R.anim.activity_anim_in_right, R.anim.activity_anim_out_right);
//        });
        llChat.setOnClickListener(v->{
            hideMenu();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            String tag = MyChatFragment.class.getName();
            Fragment myChatFragment = getParentFragmentManager().findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new MyChatFragment();
            }
            transaction.replace(R.id.fl_content,myChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
//            ((HomeActivity) getActivity()).switchShortVideoTab(1);
        });
        llShortVideo.setOnClickListener(v -> {
            hideMenu();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            String tag = ShortVideoFragment.class.getName();
            Fragment myChatFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new ShortVideoFragment();
            }
            transaction.replace(R.id.fl_content, myChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
//            startActivity(new Intent(getContext(),EntertainmentMainActivity.class));
        });

        scrollView.setOnTouchListener((v,motion)->{
            ((HomeActivity)getActivity()).hideMenu();
            return false;
        });
        llGroupChat.setOnClickListener(v->{
            hideMenu();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            String tag = HotChatFragment.class.getName();
            Fragment chatNavFragment = getParentFragmentManager().findFragmentByTag(tag);
            if (chatNavFragment==null){
                chatNavFragment = new ChatNavFragment();
            }
            transaction.replace(R.id.fl_content,chatNavFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
//            ((HomeActivity) getActivity()).switchShortVideoTab(3);
        });
        llTags.setOnClickListener(v->{
            hideMenu();
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            String tag = ChatTagsFragment.class.getName();
            Fragment chatTagsFragment = getParentFragmentManager().findFragmentByTag(tag);
            if (chatTagsFragment==null){
                chatTagsFragment = new ChatTagsFragment();
            }
            transaction.replace(R.id.fl_content,chatTagsFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
//            ((HomeActivity) getActivity()).switchShortVideoTab(2);
        });
        llVideo.setOnClickListener(v -> {
            hideMenu();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            String tag = VideoFragment.class.getName();
            Fragment myChatFragment = getParentFragmentManager().findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new VideoFragment();
            }
            transaction.replace(R.id.fl_content, myChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
//            Intent intent = new Intent(getContext(), EntertainmentMainActivity.class);
//            intent.putExtra("tab",1);
//            startActivity(intent);
        });
        List<String> bannerDatas = new ArrayList<>();
        bannerDatas.add("Welcome! We're glad you've joined our chat app. \n" +
                "Here, you can easily communicate with friends and family.\n" +
                " If you have any questions on how to use it, please don't hesitate to ask. We hope you have a great time chatting with us!");
        bannerDatas.add("좌우로 슬라이드하는 배너입니다!");
        bannerDatas.add("좌우로 슬라이드하는 배너입니다!");

        banner.setAdapter(new BannerAdapter<String, BannerTextViewHolder>(bannerDatas) {
            @Override
            public BannerTextViewHolder onCreateHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(getContext()).inflate(R.layout.item_banner_main, parent, false);
                return new BannerTextViewHolder(view1);
            }

            @Override
            public void onBindView(BannerTextViewHolder holder, String data, int position, int size) {
                TextView tvContent = holder.itemView.findViewById(R.id.tv_content);
                TextView tvContent1 = holder.itemView.findViewById(R.id.tv_content1);
                RoundFrameLayout frameLayout = holder.itemView.findViewById(R.id.main_layout);
                if (position == 0) {
                    frameLayout.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    frameLayout.setBackgroundColor(Color.WHITE);
                }
                if (position == 0) {
                    tvContent.setText(data);
                }else {
                    tvContent1.setText(data);
                }
            }


        });
        RectangleIndicator indicator = new RectangleIndicator(getActivity());
        banner.setIndicator(indicator);

        menuAdapter = new MenuAdapter();
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 5);

        llPicture.setOnClickListener(v->{
            hideMenu();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            String tag = MyChatFragment.class.getName();
            Fragment myChatFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new ImageTextFragment();
            }
            transaction.replace(R.id.fl_content, myChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        });
        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
            @Override
            public void onSuccess(Long unReadCount) {
//                rtvMessageNum.setText(String.valueOf(unReadCount));
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        rvMenus.setLayoutManager(manager);
        rvMenus.setAdapter(menuAdapter);
        List<MenuBean> beans = new ArrayList<>();
        beans.add(new MenuBean(1, 0, "모임"));
        beans.add(new MenuBean(2, 0, "쿠폰"));
        beans.add(new MenuBean(3, 0, "배달"));
        beans.add(new MenuBean(4, 0, "여행"));
        beans.add(new MenuBean(5, 0, "운동"));
        menuAdapter.setItems(beans);
    }

    @Override
    protected void initView(View view) {
        view.setOnTouchListener((v,m)->{
            hideMenu();
            return false;
        });
    }


    private void showSearchPop(View v) {
        PopupWindow popupWindow = new PopupWindow(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_search_popup, null);
        popupWindow.setContentView(view);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(() -> {
            flTitle.setVisibility(View.VISIBLE);
//            ivSearch.setImageResource(R.mipmap.ic_search_black);
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int margin = AdaptScreenUtils.pt2Px(15);
        popupWindow.showAsDropDown(v, -DpUtil.dp2px(152),margin);
        flTitle.setVisibility(View.GONE);
//        ivSearch.setImageResource(R.mipmap.ic_search_white);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_main;
    }

    public void setUnReadNum(long unread) {
        if (unread>0){
            rtvMessageNum.setVisibility(View.VISIBLE);
            rtvMessageNum.setText(String.valueOf(unread));
        }else {
            rtvMessageNum.setVisibility(View.GONE);
        }
    }
}
