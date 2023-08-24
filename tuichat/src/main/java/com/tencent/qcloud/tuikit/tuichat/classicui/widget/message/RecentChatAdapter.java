package com.tencent.qcloud.tuikit.tuichat.classicui.widget.message;

import static com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils.isC2CChat;
import static com.tencent.qcloud.tuikit.tuichat.util.TUIChatUtils.isGroupChat;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;
import com.tencent.qcloud.tuicore.TUIConstants;
import com.tencent.qcloud.tuicore.TUICore;
import com.tencent.qcloud.tuikit.tuichat.R;
import com.tencent.qcloud.tuikit.tuichat.classicui.page.TUIBaseChatActivity;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

import net.csdn.roundview.RoundFrameLayout;

import java.util.Collections;

public class RecentChatAdapter extends BaseQuickAdapter<ConversationInfo, QuickViewHolder> {

    private int[] colors;

    public RecentChatAdapter(int[] colors) {
        this.colors = colors;
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable ConversationInfo conversationBean) {
        RoundFrameLayout llMain = quickViewHolder.getView(R.id.main_layout);
        llMain.setBackgroundColor(colors[i % colors.length]);
        ImageView ivHeader = quickViewHolder.getView(R.id.iv_chat_header);

//        ivHeader.setImageResource(conversationBean.getFaceUrl());
        if (!conversationBean.isGroup()) {
            Glide.with(getContext()).load(conversationBean.getIconPath()).placeholder(R.drawable.header_main).error(R.drawable.header_main).into(ivHeader);
        }else {
            Glide.with(getContext()).load(conversationBean.getIconPath()).placeholder(R.drawable.group_chat).error(R.drawable.group_chat).into(ivHeader);
        }
        if (i == 0) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) llMain.getLayoutParams();
            int px = AdaptScreenUtils.pt2Px(95);
            params.leftMargin = px;
        } else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) llMain.getLayoutParams();
            params.leftMargin = 0;
        }
        quickViewHolder.setText(R.id.tv_tag, conversationBean.getShowName());
        if (conversationBean.isShowMenu()) {
            quickViewHolder.setGone(R.id.menu_layout, false);
        } else {
            quickViewHolder.setGone(R.id.menu_layout, true);
        }
        quickViewHolder.findView(R.id.iv_close).setOnClickListener(v -> {
            conversationBean.setShowMenu(false);
            getItems().remove(i);
//            notifyItemChanged(i);
            notifyItemRemoved(i);
            Context context = getContext();
            if (context instanceof TUIBaseChatActivity) {
                ((TUIBaseChatActivity) context).resetSetting();
            }
        });
        quickViewHolder.itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putString(TUIConstants.TUIChat.CHAT_ID, conversationBean.getId());
            bundle.putString(TUIConstants.TUIChat.CHAT_NAME, conversationBean.getShowName());
            bundle.putInt(TUIConstants.TUIChat.CHAT_TYPE, conversationBean.getConversation().getType());
            if (isC2CChat(conversationBean.getConversation().getType())) {
                TUICore.startActivity(TUIConstants.TUIChat.C2C_CHAT_ACTIVITY_NAME, bundle);
            } else if (isGroupChat(conversationBean.getConversation().getType())) {
                bundle.putString(TUIConstants.TUIChat.GROUP_TYPE, conversationBean.getGroupType());
                TUICore.startActivity(TUIConstants.TUIChat.GROUP_CHAT_ACTIVITY_NAME, bundle);
            }
        });
        quickViewHolder.getView(R.id.iv_previous).setOnClickListener(v->{
            if (i>0) {
                Collections.swap(getItems(),i,i-1);
                notifyItemChanged(i);
                notifyItemChanged(i-1);
            }else {
                ToastUtils.showShort("已经是最前面~");
            }
        });
        if (conversationBean.getUnreadCount()>0){
            quickViewHolder.setGone(R.id.iv_message_count,false);
        }else {
            quickViewHolder.setGone(R.id.iv_message_count,true);
        }
        quickViewHolder.itemView.setOnLongClickListener(v -> {
            conversationBean.setShowMenu(true);
            notifyItemChanged(i);
            if (getContext() instanceof TUIBaseChatActivity) {
                ((TUIBaseChatActivity) getContext()).checkAndResetSettingBtn();
            }
            return false;
        });
    }


    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_recent_chatting, viewGroup);
    }
}
