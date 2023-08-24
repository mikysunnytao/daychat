package com.daychat.live.ui.fragment.chat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.ui.act.HomeActivity;
import com.daychat.live.util.DpUtil;
import com.daychat.live.util.TUIUtils;
import com.tencent.qcloud.tuikit.tuiconversation.TUIConversationConstants;
import com.tencent.qcloud.tuikit.tuiconversation.bean.ConversationInfo;
import com.tencent.qcloud.tuikit.tuiconversation.classicui.page.TUIConversationFragment;
import com.tencent.qcloud.tuikit.tuiconversation.classicui.util.OnItemClickListener;

import butterknife.BindView;

public class MyChatFragment extends BaseFragment {

//    @BindView(R.id.rv_my_chats)
//    RecyclerView rvMyChats;

    @BindView(R.id.fl_back_nav)
    FrameLayout ivBackNav;

    @BindView(R.id.fl_create_group)
    FrameLayout ivCreateGroup;

    @BindView(R.id.fl_title)
    FrameLayout flTitle;

    @BindView(R.id.iv_search)
    ImageView ivSearch;

    private TUIConversationFragment tuiConversationFragment;

    @Override
    protected void lazyLoad() {
        ivSearch.setOnClickListener(v->{
            hideMenu();
            showSearchPop(v);
        });
        tuiConversationFragment = new TUIConversationFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chat,tuiConversationFragment);
        tuiConversationFragment.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void click(ConversationInfo conversationInfo) {
                if (getActivity() instanceof HomeActivity){
                    hideMenu();
                }
            }
        });
        transaction.commit();
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
    protected void initView(View view) {

        ivBackNav.setOnClickListener(v->{
            int fragmentCount = getParentFragmentManager().getBackStackEntryCount();
            if (fragmentCount>0){
                getParentFragmentManager().popBackStack();
            }
        });
        ivCreateGroup.setOnClickListener(v->{
//            ((HomeActivity)getActivity()).switchTab(0);
            Bundle bundle = new Bundle();
            bundle.putInt(TUIConversationConstants.GroupType.TYPE, TUIConversationConstants.GroupType.PUBLIC);
            TUIUtils.startActivity("StartGroupChatActivity", bundle);
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_chat;
    }
}
