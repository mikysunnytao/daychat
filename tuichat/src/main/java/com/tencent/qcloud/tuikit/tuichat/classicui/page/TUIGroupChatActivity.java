package com.tencent.qcloud.tuikit.tuichat.classicui.page;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuicore.util.ToastUtil;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.TUIChatConstants;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuichat.bean.GroupInfo;
import com.tencent.qcloud.tuikit.tuichat.presenter.GroupChatPresenter;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils;

public class TUIGroupChatActivity extends TUIBaseChatActivity {
    private static final String TAG = TUIGroupChatActivity.class.getSimpleName();

    private TUIGroupChatFragment chatFragment;
    private GroupChatPresenter presenter;

    @Override
    public void initChat(ChatInfo chatInfo) {
        TUIChatLog.i(TAG, "inti chat " + chatInfo);

        if (!TUIChatUtils.isGroupChat(chatInfo.getType())) {
            TUIChatLog.e(TAG, "init group chat failed , chatInfo = " + chatInfo);
            ToastUtil.toastShortMessage("init group chat failed.");
        }
        GroupInfo groupInfo = (GroupInfo) chatInfo;

        Glide.with(this).load(groupInfo.getFaceUrl()).into(ivHeader);
        tvName.setText(groupInfo.getChatName());
        if (groupInfo.getMemberDetails()==null){
            V2TIMManager.getGroupManager().getGroupMemberList(groupInfo.getId(), 0, Integer.MAX_VALUE, new V2TIMValueCallback<V2TIMGroupMemberInfoResult>() {
                @Override
                public void onSuccess(V2TIMGroupMemberInfoResult v2TIMGroupMemberInfoResult) {
                    tvGroupNum.setText(String.valueOf(v2TIMGroupMemberInfoResult.getMemberInfoList().size()));
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }else {
            tvGroupNum.setText(String.valueOf(groupInfo.getMemberCount()));
        }
        tvSettings.setOnClickListener(v->{
            if (TUIChatUtils.isTopicGroup(groupInfo.getId())) {
                Bundle bundle = new Bundle();
                bundle.putString(TUIConstants.TUICommunity.TOPIC_ID, groupInfo.getId());
                TUICore.startActivity(this, "TopicInfoActivity", bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(TUIChatConstants.Group.GROUP_ID, groupInfo.getId());
//                bundle.putString(TUIConstants.TUIChat.CHAT_BACKGROUND_URI, mChatBackgroundThumbnailUrl);
                TUICore.startActivity(this, "GroupInfoActivity", bundle);
            }
        });
        chatFragment = new TUIGroupChatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TUIChatConstants.CHAT_INFO, groupInfo);
        chatFragment.setArguments(bundle);
        presenter = new GroupChatPresenter();
        presenter.initListener();
        chatFragment.setPresenter(presenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, chatFragment).commitAllowingStateLoss();
    }
}