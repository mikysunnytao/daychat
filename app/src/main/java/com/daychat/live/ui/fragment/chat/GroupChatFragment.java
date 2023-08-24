package com.daychat.live.ui.fragment.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.ui.act.PartyActivity;
import com.daychat.live.ui.adapter.chat.ConversationAdapter;
import com.daychat.live.util.DpUtil;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

import net.csdn.roundview.RoundLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GroupChatFragment extends BaseFragment {

    @BindView(R.id.fl_my_chats)
    FrameLayout flChat;

    @BindView(R.id.ll_chat)
    RoundLinearLayout llChat;

    @BindView(R.id.fl_show_chats)
    FrameLayout flShowChats;
    @BindView(R.id.fl_close)
    FrameLayout flClose;

    @BindView(R.id.fl_back)
    FrameLayout flBack;

    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;

    @BindView(R.id.rv_hot)
    RecyclerView rvHot;

    @BindView(R.id.fl_search)
    FrameLayout flSearch;

    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    @BindView(R.id.fl_title)
    FrameLayout flTitle;


    private ConversationAdapter conversationAdapter;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initView(View view) {
        flBack.setOnClickListener(v->{
//            int count = getParentFragmentManager().getBackStackEntryCount();
//            if (count>0){
//                getParentFragmentManager().popBackStack();
//            }
            getActivity().finish();
        });
        ivMenu.setOnClickListener(v->{
            ((PartyActivity)getActivity()).menuLayout.openDrawer(GravityCompat.END);
        });

        flClose.setOnClickListener(v->{
            llChat.setVisibility(View.GONE);
        });
        flSearch.setOnClickListener(this::showSearchPop);
        flShowChats.setOnClickListener(v->{
            hideMenu();
            FragmentManager manager = getParentFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            String tag = MyChatFragment.class.getSimpleName();
            Fragment fragment = manager.findFragmentByTag(tag);
            if (fragment == null){
                fragment = new MyChatFragment();
            }
            transaction.replace(R.id.fl_chat,fragment);
            transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        });
        conversationAdapter = new ConversationAdapter();
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        rvRecommend.setLayoutManager(manager1);
        rvRecommend.setAdapter(conversationAdapter);
        ConversationInfo conversationBean = new ConversationInfo();
        List<Object> urls = new ArrayList<>();
        urls.add(R.mipmap.header1);
        conversationBean.setIconUrlList(urls);
        conversationAdapter.add(conversationBean);
        conversationBean = new ConversationInfo();
        List<Object> headers = new ArrayList<>();
        headers.add(R.mipmap.group_chat1);
        conversationBean.setIconUrlList(headers);
        conversationAdapter.add(conversationBean);
        ConversationInfo conversationBean2 = new ConversationInfo();
        urls = new ArrayList<>();
        urls.add(R.mipmap.header6);
        conversationBean2.setIconUrlList(urls);
        conversationAdapter.add(conversationBean2);
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        conversationAdapter = new ConversationAdapter();
        rvHot.setLayoutManager(manager2);
        rvHot.setAdapter(conversationAdapter);
        conversationBean = new ConversationInfo();
        urls = new ArrayList<>();
        urls.add(R.mipmap.header1);
        conversationBean.setIconUrlList(urls);
        conversationAdapter.add(conversationBean);
        conversationBean = new ConversationInfo();
        headers = new ArrayList<>();
        headers.add(R.mipmap.header9);
        conversationBean.setIconUrlList(headers);
        conversationAdapter.add(conversationBean);
        conversationBean2 = new ConversationInfo();
        urls = new ArrayList<>();
        urls.add(R.mipmap.group_chat2);
        conversationBean2.setIconUrlList(urls);
        conversationAdapter.add(conversationBean2);
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
    }
    @Override
    protected int getLayoutId() {
        return R.layout.layout_group_chat;
    }
}
