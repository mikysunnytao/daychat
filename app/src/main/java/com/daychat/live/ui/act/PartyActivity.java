package com.daychat.live.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.daychat.live.R;
import com.daychat.live.base.BaseActivity;
import com.daychat.live.ui.act.chat.ChattingActivity;
import com.daychat.live.ui.adapter.chat.ConversationAdapter;
import com.daychat.live.ui.fragment.chat.GroupChatFragment;
import com.daychat.live.ui.fragment.chat.MyChatFragment;
import com.daychat.live.util.DpUtil;
import com.meiyinzb.nasinet.base.NasiBaseActivity;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.classicui.page.TUIConversationFragment;

import net.csdn.roundview.RoundLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PartyActivity extends NasiBaseActivity {



    @BindView(R.id.drawer)
    public DrawerLayout menuLayout;


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_party);
//        initView();
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_party;
    }

    @Override
    protected void initData() {

    }
    @Override
    protected void initView() {
        hideTitle(true);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        GroupChatFragment groupChatFragment = new GroupChatFragment();
        transaction.add(R.id.fl_chat,groupChatFragment);
        transaction.commit();
//        conversationAdapter.setOnItemClickListener((baseQuickAdapter, view1, i) -> {
//            Intent intent = new Intent(this, ChattingActivity.class);
//            startActivity(intent);
//        });

    }




}
