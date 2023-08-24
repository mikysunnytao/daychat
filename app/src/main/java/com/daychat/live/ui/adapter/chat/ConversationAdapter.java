package com.daychat.live.ui.adapter.chat;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.QuickViewHolder;
import com.daychat.live.R;
import com.tencent.qcloud.tuikit.tuichat.bean.ChatInfo;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;

public class ConversationAdapter extends BaseQuickAdapter<ConversationInfo, QuickViewHolder> {

    public ConversationAdapter() {
    }


    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder quickViewHolder, int i, @Nullable ConversationInfo item) {
        quickViewHolder.setImageResource(R.id.iv_header, Integer.parseInt(item.getIconUrlList().get(0).toString()));
//        quickViewHolder.setText(R.id.tv_content,item.getTitle());
//        if (item.getType() == ) {
//        } else {
//            int px = AdaptScreenUtils.pt2Px(150);
//            int dp = SizeUtils.px2dp(px);
//            List<Integer>转list
//            Integer [] header = (GroupInfo)item..toArray(new Integer[]{});
//            int [] headers = Arrays.stream(header).mapToInt(Integer::valueOf).toArray();
//            CombineBitmap.init(getContext())
//                    .setSize(dp)
//                    .setLayoutManager(new WechatLayoutManager())
//                    .setResourceIds(headers).setImageView(quickViewHolder.getView(R.id.iv_header)).build();
//        }
    }

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.item_conversation, viewGroup);
    }
}
