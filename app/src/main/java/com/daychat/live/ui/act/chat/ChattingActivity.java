package com.daychat.live.ui.act.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daychat.live.R;
import com.daychat.live.ui.adapter.chat.RecentChatAdapter;
import net.csdn.roundview.RoundFrameLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.GroupInfo;
import com.tencent.qcloud.tuikit.tuichat.classicui.page.TUIBaseChatActivity;
import com.tencent.qcloud.tuikit.tuichat.classicui.page.TUIC2CChatFragment;
import com.tencent.qcloud.tuikit.tuichat.classicui.page.TUIGroupChatFragment;
import com.tencent.qcloud.tuikit.tuichat.presenter.C2CChatPresenter;
import com.tencent.qcloud.tuikit.tuichat.presenter.GroupChatPresenter;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.hutool.core.bean.BeanUtil;

public class ChattingActivity extends TUIBaseChatActivity {

    private RecentChatAdapter recentChatAdapter;

    private Fragment chatFragment;

    private GroupChatPresenter groupChatPresenter;

    private C2CChatPresenter c2CChatPresenter;
    private static final String TAG = ChattingActivity.class.getSimpleName();

    private boolean checkAll = false;
    RecyclerView rvRecentChat;

    RoundFrameLayout flSetting;

    ImageView ivSetting;


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chatting);
//        initData();
//    }


    protected void initData() {
        ImmersionBar.with(this).init();
        rvRecentChat = findViewById(R.id.rv_recent_chat);
        flSetting = findViewById(R.id.fl_setting);
        ivSetting = findViewById(R.id.iv_setting);
        int[] colors = new int[]{Color.parseColor("#8edade"), Color.parseColor("#fea583"), Color.parseColor("#c9acde"), Color.parseColor("#f0c0d3")};
        recentChatAdapter = new RecentChatAdapter(colors);
        rvRecentChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecentChat.setAdapter(recentChatAdapter);
        flSetting.setOnClickListener(v->{
            //目前全选状态
            if (checkAll){
                flSetting.setBackgroundColor(Color.WHITE);
                ivSetting.setImageResource(R.drawable.gear);
                List<ConversationInfo> infos = recentChatAdapter.getItems();
                for (ConversationInfo conversationInfo : infos) {
                    conversationInfo.setShowMenu(false);
                }
                checkAll = false;
                recentChatAdapter.notifyDataSetChanged();
            }else {
                flSetting.setBackgroundColor(Color.parseColor("#99000000"));
                ivSetting.setImageResource(R.mipmap.ic_setting_white);
                List<ConversationInfo> infos = recentChatAdapter.getItems();
                for (ConversationInfo conversationInfo : infos) {
                    conversationInfo.setShowMenu(true);
                }
                checkAll = true;
                recentChatAdapter.notifyDataSetChanged();
            }
        });

    }

    public void resetSetting(){
        checkAll = false;
        flSetting.setBackgroundColor(Color.WHITE);
        ivSetting.setImageResource(R.drawable.gear);
    }

    public void checkAndResetSettingBtn() {
        List<ConversationInfo> infos = recentChatAdapter.getItems();
        boolean checkAll = true;
        for (ConversationInfo info : infos) {
            if (!info.isShowMenu()){
                checkAll = false;
                break;
            }
        }
        if (checkAll){
            checkAll = false;
            flSetting.setBackgroundColor(Color.parseColor("#99000000"));
            ivSetting.setImageResource(R.mipmap.ic_setting_white);
        }

    }


    @Override
    public void initChat(ChatInfo chatInfo) {
        initData();
        boolean groupChat = TUIChatUtils.isGroupChat(chatInfo.getType());
        if (groupChat) {
            GroupInfo groupInfo = (GroupInfo) chatInfo;
            chatFragment = new TUIGroupChatFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(TUIChatConstants.CHAT_INFO, groupInfo);
            chatFragment.setArguments(bundle);
            groupChatPresenter = new GroupChatPresenter();
            groupChatPresenter.initListener();
            ((TUIGroupChatFragment) chatFragment).setPresenter(groupChatPresenter);
            getSupportFragmentManager().beginTransaction().replace(com.tencent.qcloud.tuikit.tuichat.R.id.empty_view, chatFragment).commitAllowingStateLoss();
        } else {
//            Glide.with(this).load(chatInfo.getIconUrlList().get(0)).placeholder(R.drawable.header_main).error(R.drawable.header_main).into(ivHeader);
            chatFragment = new TUIC2CChatFragment();
            Bundle bundle = new Bundle();

            bundle.putSerializable(TUIChatConstants.CHAT_INFO, chatInfo);
            chatFragment.setArguments(bundle);
            c2CChatPresenter = new C2CChatPresenter();
            c2CChatPresenter.initListener();
            ((TUIC2CChatFragment) chatFragment).setPresenter(c2CChatPresenter);
            getSupportFragmentManager().beginTransaction().replace(com.tencent.qcloud.tuikit.tuichat.R.id.empty_view, chatFragment).commitAllowingStateLoss();
        }
    }


}
