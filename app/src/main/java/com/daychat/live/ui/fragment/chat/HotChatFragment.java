package com.daychat.live.ui.fragment.chat;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.ui.act.chat.ChattingActivity;
import com.daychat.live.ui.adapter.chat.ConversationAdapter;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.classicui.page.TUIConversationFragment;

import net.csdn.roundview.RoundLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HotChatFragment extends BaseFragment {

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.rv_recommend)
    RecyclerView rvRecommend;

    @BindView(R.id.rv_hot)
    RecyclerView rvHot;

    @BindView(R.id.fl_my_chats)
    FrameLayout flChat;

    @BindView(R.id.ll_chat)
    RoundLinearLayout llChat;

    @BindView(R.id.fl_show_chats)
    FrameLayout flShowChats;
    @BindView(R.id.fl_close)
    FrameLayout flClose;
    private ConversationAdapter conversationAdapter;
    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initView(View view) {


        flClose.setOnClickListener(v->{
            hideMenu();
            llChat.setVisibility(View.GONE);
        });
        conversationAdapter = new ConversationAdapter();
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TUIConversationFragment tuiConversationFragment = new TUIConversationFragment();
        transaction.add(R.id.fl_my_chats,tuiConversationFragment);
//        transaction.commit();
        conversationAdapter.setOnItemClickListener((baseQuickAdapter, view1, i) -> {
            Intent intent = new Intent(getContext(), ChattingActivity.class);
            startActivity(intent);
        });
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_chat;
    }
}
