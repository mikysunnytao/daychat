package com.daychat.live.ui.fragment.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.daychat.live.R;
import com.daychat.live.base.BaseFragment;
import com.daychat.live.eventbus.UnReadBus;
import com.daychat.live.ui.act.HomeActivity;
import com.daychat.live.ui.act.MyActivity;
import com.daychat.live.util.TUIUtils;
import net.csdn.roundview.RoundTextView;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.qcloud.tuikit.tuicontact.TUIContactConstants;
import com.tencent.qcloud.tuikit.tuicontact.bean.ContactItemBean;
import com.tencent.qcloud.tuikit.tuicontact.classicui.pages.TUIContactFragment;
import com.tencent.qcloud.tuikit.tuicontact.classicui.widget.ContactListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class ContactFragment extends BaseFragment {

    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.fl_contact)
    FrameLayout flContact;

    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.iv_close)
    ImageView ivClose;

    @BindView(R.id.rt_message_num)
    RoundTextView rtvMessageNum;
    @Override
    protected void lazyLoad() {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TUIContactFragment tuiContactFragment = new TUIContactFragment();
        tuiContactFragment.setOnItemClickListener((position, contact) -> hideMenu());
        transaction.add(R.id.fl_contact,tuiContactFragment);
        transaction.commit();
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
        ivSearch.setOnClickListener(v->{
            hideMenu();
            Bundle bundle = new Bundle();
            bundle.putBoolean(TUIContactConstants.GroupType.GROUP, false);
            TUIUtils.startActivity("AddMoreActivity", bundle);
        });

        ivClose.setOnClickListener(v->{
            hideMenu();
            int count = getParentFragmentManager().getBackStackEntryCount();
            if (count>0){
                getParentFragmentManager().popBackStack();
            }
//            ((HomeActivity)getActivity()).switchTab(0);
        });
        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                rtvMessageNum.setText(String.valueOf(aLong));
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contact;
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
