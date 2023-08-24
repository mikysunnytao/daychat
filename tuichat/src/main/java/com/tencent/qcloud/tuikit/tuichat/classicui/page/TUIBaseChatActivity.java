package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageListGetOption;
import com.tencent.imsdk.v2.V2TIMReceiveMessageOptInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.TUILogin;
import com.tencent.qcloud.tuicore.component.activities.BaseLightActivity;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.DraftInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.GroupInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.GroupMemberInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.RecentChatBean;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;
import com.tencent.qcloud.tuikit.tuichat.classicui.widget.message.RecentChatAdapter;
import com.tencent.qcloud.tuikit.tuichat.util.ChatMessageBuilder;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.commonutil.ConversationUtils;

import net.csdn.roundview.CircleImageView;
import net.csdn.roundview.RoundFrameLayout;
import net.csdn.roundview.RoundImageView;
import net.csdn.roundview.RoundTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TUIBaseChatActivity extends BaseLightActivity {

    private static final String TAG = TUIBaseChatActivity.class.getSimpleName();

    private ImageView ivClose;

    RecyclerView rvRecentChat;
    RoundFrameLayout flMore;

    RoundTextView tvGroupNum;

    RoundFrameLayout flSetting;

    ImageView ivSetting;

    private RecentChatAdapter recentChatAdapter;

    public CircleImageView ivHeader;

    RoundTextView tvSettings;
    TextView tvName;

    TextView tvUnreadNum;

    private boolean checkAll = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TUIChatLog.i(TAG, "onCreate " + this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        tvGroupNum = findViewById(R.id.tv_group_count);
        tvSettings = findViewById(R.id.tv_settings);
        rvRecentChat = findViewById(R.id.rv_recent_chat);
        ivHeader = findViewById(R.id.iv_header);
        tvName = findViewById(R.id.tv_chat_name);
        tvUnreadNum = findViewById(R.id.tv_unread_count);
        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                if (aLong>99) {
                    tvUnreadNum.setText("99+");
                }else {
                    if (aLong>0){
                        tvUnreadNum.setText(""+aLong);
                    }else {
                        tvUnreadNum.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        int[] colors = new int[]{Color.parseColor("#8edade"), Color.parseColor("#fea583"), Color.parseColor("#c9acde"), Color.parseColor("#f0c0d3")};
        recentChatAdapter = new RecentChatAdapter(colors);
        rvRecentChat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvRecentChat.setAdapter(recentChatAdapter);
        ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(v -> {
            finish();
        });
        flMore = findViewById(R.id.fl_more);
        flMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        flSetting = findViewById(R.id.fl_setting);
        flSetting.setOnClickListener(v -> {
            //目前全选状态
            if (checkAll) {
                flSetting.setBackgroundColor(Color.WHITE);
                ivSetting.setImageResource(R.drawable.gear);
                List<ConversationInfo> infos = recentChatAdapter.getItems();
                for (ConversationInfo conversationInfo : infos) {
                    conversationInfo.setShowMenu(false);
                }
                checkAll = false;
                recentChatAdapter.notifyDataSetChanged();
            } else {
                flSetting.setBackgroundColor(Color.parseColor("#99000000"));
                ivSetting.setImageResource(R.drawable.ic_setting_white);
                List<ConversationInfo> infos = recentChatAdapter.getItems();
                for (ConversationInfo conversationInfo : infos) {
                    conversationInfo.setShowMenu(true);
                }
                checkAll = true;
                recentChatAdapter.notifyDataSetChanged();
            }
        });
        ivSetting = findViewById(R.id.iv_setting);
        V2TIMMessageListGetOption option = new V2TIMMessageListGetOption();
        String userId = Hawk.get("userId");
        option.setUserID(userId);

        V2TIMManager.getConversationManager().getConversationList(0, 10, new V2TIMValueCallback<V2TIMConversationResult>() {
            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                if (v2TIMConversationResult.getConversationList() != null && !v2TIMConversationResult.getConversationList().isEmpty()) {
                    List<ConversationInfo> infos = ConversationUtils.convertV2TIMConversationList(v2TIMConversationResult.getConversationList());
                    String conversationId = getIntent().getStringExtra(TUIConstants.TUIChat.CHAT_ID);
                    for (ConversationInfo info : infos) {
                        if (info.getConversation().getType() == V2TIMConversation.V2TIM_GROUP) {
                            conversationId = "group_" + conversationId;
                        }
                        if (info.getConversationId().equals(conversationId)) {//group_@TGS#1F2ZQGANX
                            infos.remove(info);
                            break;
                        }
                    }
                    recentChatAdapter.addAll(infos);
                }
            }

            @Override
            public void onError(int i, String s) {
                System.out.println(s);
            }
        });

        chat(getIntent());
    }

    public void resetSetting() {
        checkAll = false;
        flSetting.setBackgroundColor(Color.WHITE);
        ivSetting.setImageResource(R.drawable.gear);
    }

    public void checkAndResetSettingBtn() {
        List<ConversationInfo> infos = recentChatAdapter.getItems();
        boolean checkAll = true;
        for (ConversationInfo info : infos) {
            if (!info.isShowMenu()) {
                checkAll = false;
                break;
            }
        }
        if (checkAll) {
            checkAll = false;
            flSetting.setBackgroundColor(Color.parseColor("#99000000"));
            ivSetting.setImageResource(R.drawable.ic_setting_white);
        }

    }


    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 1290);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        TUIChatLog.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
        chat(intent);
    }

    @Override
    protected void onResume() {
        TUIChatLog.i(TAG, "onResume");
        super.onResume();
    }

    private void chat(Intent intent) {
        Bundle bundle = intent.getExtras();
        TUIChatLog.i(TAG, "bundle: " + bundle + " intent: " + intent);
        if (!TUILogin.isUserLogined()) {
            ToastUtil.toastShortMessage(getString(R.string.chat_tips_not_login));
            finish();
            return;
        }

        ChatInfo chatInfo = getChatInfo(intent);
        TUIChatLog.i(TAG, "start chatActivity chatInfo: " + chatInfo);
        if (chatInfo != null) {
            initChat(chatInfo);
        } else {
            ToastUtil.toastShortMessage("init chat failed , chatInfo is empty.");
            TUIChatLog.e(TAG, "init chat failed , chatInfo is empty.");
            finish();
        }
    }

    public abstract void initChat(ChatInfo chatInfo);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3 && data != null) {
            if (requestCode == 11) {
                List<String> stringList = data.getStringArrayListExtra("list");
                if (stringList != null && !stringList.isEmpty()) {
                    String[] stringArray = stringList.toArray(new String[]{});
                    data.putExtra(TUIConstants.TUICalling.PARAM_NAME_USERIDS, stringArray);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    for (String key : data.getExtras().keySet()) {
                        hashMap.put(key, data.getExtras().get(key));
                    }
                    TUICore.callService(TUIConstants.TUICalling.SERVICE_NAME,
                            TUIConstants.TUICalling.METHOD_NAME_CALL, hashMap);
                }
            }
        }

    }

    private ChatInfo getChatInfo(Intent intent) {
        int chatType = intent.getIntExtra(TUIConstants.TUIChat.CHAT_TYPE, ChatInfo.TYPE_INVALID);
        ChatInfo chatInfo;
        if (chatType == ChatInfo.TYPE_C2C) {
            chatInfo = new ChatInfo();
        } else if (chatType == ChatInfo.TYPE_GROUP) {
            chatInfo = new GroupInfo();
        } else {
            return null;
        }
        chatInfo.setType(chatType);
        chatInfo.setId(intent.getStringExtra(TUIConstants.TUIChat.CHAT_ID));
        chatInfo.setChatName(intent.getStringExtra(TUIConstants.TUIChat.CHAT_NAME));
        DraftInfo draftInfo = new DraftInfo();
        draftInfo.setDraftText(intent.getStringExtra(TUIConstants.TUIChat.DRAFT_TEXT));
        draftInfo.setDraftTime(intent.getLongExtra(TUIConstants.TUIChat.DRAFT_TIME, 0));
        chatInfo.setDraft(draftInfo);
        chatInfo.setTopChat(intent.getBooleanExtra(TUIConstants.TUIChat.IS_TOP_CHAT, false));
        V2TIMMessage v2TIMMessage = (V2TIMMessage) intent.getSerializableExtra(TUIConstants.TUIChat.LOCATE_MESSAGE);
        TUIMessageBean messageInfo = ChatMessageBuilder.buildMessage(v2TIMMessage);
        chatInfo.setLocateMessage(messageInfo);
        chatInfo.setAtInfoList((List<V2TIMGroupAtInfo>) intent.getSerializableExtra(TUIConstants.TUIChat.AT_INFO_LIST));
        chatInfo.setFaceUrl(intent.getStringExtra(TUIConstants.TUIChat.FACE_URL));

        if (chatType == ChatInfo.TYPE_GROUP) {
            GroupInfo groupInfo = (GroupInfo) chatInfo;
            groupInfo.setGroupName(intent.getStringExtra(TUIConstants.TUIChat.GROUP_NAME));
            groupInfo.setGroupType(intent.getStringExtra(TUIConstants.TUIChat.GROUP_TYPE));
            groupInfo.setJoinType(intent.getIntExtra(TUIConstants.TUIChat.JOIN_TYPE, 0));
            groupInfo.setMemberCount(intent.getIntExtra(TUIConstants.TUIChat.MEMBER_COUNT, 0));
            groupInfo.setMessageReceiveOption(intent.getBooleanExtra(TUIConstants.TUIChat.RECEIVE_OPTION, false));
            groupInfo.setNotice(intent.getStringExtra(TUIConstants.TUIChat.NOTICE));
            groupInfo.setOwner(intent.getStringExtra(TUIConstants.TUIChat.OWNER));
            groupInfo.setMemberDetails((List<GroupMemberInfo>) intent.getSerializableExtra(TUIConstants.TUIChat.MEMBER_DETAILS));
        }

        if (TextUtils.isEmpty(chatInfo.getId())) {
            return null;
        }
        return chatInfo;
    }
}
