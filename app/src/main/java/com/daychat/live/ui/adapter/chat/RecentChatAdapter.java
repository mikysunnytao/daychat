package com.daychat.live.ui.adapter.chat;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;
import com.daychat.live.R;
import com.daychat.live.ui.act.chat.ChattingActivity;
import net.csdn.roundview.RoundFrameLayout;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

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
        Glide.with(getContext()).load(conversationBean.getIconPath()).error(R.mipmap.header_main).into(ivHeader);
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
            notifyItemChanged(i);
            Context context = getContext();
            if (context instanceof ChattingActivity) {
                ((ChattingActivity) context).resetSetting();
            }
        });
        quickViewHolder.itemView.setOnLongClickListener(v -> {
            conversationBean.setShowMenu(true);
            notifyItemChanged(i);
            if (getContext() instanceof ChattingActivity) {
                ((ChattingActivity) getContext()).checkAndResetSettingBtn();
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
