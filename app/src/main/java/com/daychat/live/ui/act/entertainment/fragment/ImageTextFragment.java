package com.daychat.live.ui.act.entertainment.fragment;

import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.daychat.live.R;
import com.daychat.live.bean.PictureBean;
import com.daychat.live.eventbus.UnReadBus;
import com.daychat.live.ui.act.HomeActivity;
import com.daychat.live.ui.act.entertainment.fragment.adapter.ImageTextAdapter;
import com.jaeger.library.StatusBarUtil;
import com.meiyinzb.nasinet.base.BaseFragment;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import net.csdn.roundview.RoundFrameLayout;
import net.csdn.roundview.RoundTextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ImageTextFragment extends BaseFragment {

    @BindView(R.id.header_view)
    RoundFrameLayout roundFrameLayout;

    @BindView(R.id.rv_picture)
    RecyclerView rvPicture;

    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.rt_message_num)
    RoundTextView rtvMessageNum;

    @BindView(R.id.iv_menu)
    ImageView ivMenu;

    ImageTextAdapter imageTextAdapter;

    @Override
    protected void lazyLoad() {
        ivMenu.setOnClickListener(v->{
            ((HomeActivity)getActivity()).showMenu();
        });
        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
//                rtvMessageNum.setText(String.valueOf(aLong));
            }

            @Override
            public void onError(int i, String s) {

            }
        });
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
        StatusBarUtil.setDarkMode(getActivity());
        view.findViewById(R.id.header_view).setVisibility(View.GONE);
        imageTextAdapter = new ImageTextAdapter();
        rvPicture.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        List<PictureBean> pictureBeans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pictureBeans.add(new PictureBean());
        }
        imageTextAdapter.setItems(pictureBeans);
        rvPicture.setAdapter(imageTextAdapter);
        ivSearch.setOnClickListener(v->{
            showPopup(v);
        });
    }

    private void showPopup(View v) {
        PopupWindow popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,false);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_search,null);
        popupWindow.setContentView(view);
        view.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
        int searchWidth = -view.getMeasuredWidth();
        int searchHeight = -view.getMeasuredHeight();
        int px = AdaptScreenUtils.pt2Px(32);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int t = AdaptScreenUtils.pt2Px(15);
        popupWindow.showAsDropDown(v,searchWidth-px,searchHeight+t);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_text;
    }
}
