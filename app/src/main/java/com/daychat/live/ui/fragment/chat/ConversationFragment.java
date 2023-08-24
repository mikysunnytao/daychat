package com.daychat.live.ui.fragment.chat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.eventbus.UnReadBus;
import com.daychat.live.ui.act.MyActivity;
import com.daychat.live.util.DpUtil;
import net.csdn.roundview.RoundTextView;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class ConversationFragment extends BaseFragment {


    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    @BindView(R.id.rt_message_num)
    RoundTextView rtvMessageNum;


    @Override
    protected void lazyLoad() {
//        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
//            @Override
//            public void onSuccess(Long aLong) {
//                rtvMessageNum.setText(String.valueOf(aLong));
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnreadCount(UnReadBus message) {
        if (message.unread > 0) {
            rtvMessageNum.setVisibility(View.VISIBLE);
            rtvMessageNum.setText(String.valueOf(message.unread));
//            tv_red_hot.setVisibility(View.VISIBLE);
//            tv_red_hot.setText(String.valueOf(message.unread));
        } else {
            rtvMessageNum.setVisibility(View.GONE);
//            tv_red_hot.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView(View view) {
        ChatNavFragment fragment1 = new ChatNavFragment();
        String TAG = ChatNavFragment.class.getName();
        getChildFragmentManager().beginTransaction().add(R.id.fl_chat, fragment1).commitAllowingStateLoss();
        ivSearch.setOnClickListener(v->{
            hideMenu();
            showSearchPop(v);
        });
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
    protected int getLayoutId() {
        return R.layout.fragment_conversation;
    }

    public void setFragment(Integer type) {
        if (type == 0) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            String tag = ChatNavFragment.class.getName();
            Fragment myChatFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new ChatNavFragment();
            }
            transaction.replace(R.id.fl_chat, myChatFragment);
            transaction.commit();
        } else if (type == 1) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            String tag = MyChatFragment.class.getName();
            Fragment myChatFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new MyChatFragment();
            }
            transaction.replace(R.id.fl_chat, myChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        } else if (type == 2) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            String tag = ChatTagsFragment.class.getName();
            Fragment tagsFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (tagsFragment == null) {
                tagsFragment = new ChatTagsFragment();
            }
            transaction.replace(R.id.fl_chat, tagsFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        }else if (type==3){
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            String tag = HotChatFragment.class.getName();
            Fragment hotChatFragment = getChildFragmentManager().findFragmentByTag(tag);
            if (hotChatFragment == null) {
                hotChatFragment = new HotChatFragment();
            }
            transaction.replace(R.id.fl_chat, hotChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
        }
    }

    public void setUnReadNum(long unread) {
        if (unread>0){
            rtvMessageNum.setVisibility(View.VISIBLE);
            rtvMessageNum.setText(String.valueOf(unread));
        }else {
            rtvMessageNum.setVisibility(View.GONE);
        }
    }
}
