package com.daychat.live.ui.act;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.daychat.live.R;
import com.daychat.live.base.BaseActivity;
import com.daychat.live.ui.adapter.BannerTextViewHolder;
import com.daychat.live.util.MyUserInstance;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;

import net.csdn.roundview.RoundFrameLayout;
import net.csdn.roundview.RoundTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MenuMessageActivity extends BaseActivity {

    @BindView(R.id.fl_my)
    RoundFrameLayout flMy;

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.fl_wallet_bg)
    FrameLayout flWalletBg;

    @BindView(R.id.wallet)
    TextView tvWallet;

    @BindView(R.id.iv_close)
    ImageView ivClose;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.iv_down_arrow)
    ImageView ivArrowDown;

    @BindView(R.id.cl_main)
    ConstraintLayout clMain;


    @BindView(R.id.rt_message_num)
    RoundTextView rtvMessageNum;

    @BindView(R.id.iv_header)
    ImageView ivHeader;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_menu);
        unbinder = ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        flWalletBg.setOnClickListener(v->{
            startActivity(new Intent(this, MenuWalletActivity.class));
            overridePendingTransition(R.anim.activity_anim_in_right,R.anim.activity_anim_out_right);
            finish();
        });
        flMy.setOnClickListener(v->{
            startActivity(new Intent(this, MenuMyActivity.class));
            overridePendingTransition(R.anim.activity_anim_in_right,R.anim.activity_anim_out_right);
            finish();
        });
        ivClose.setOnClickListener(v->{
            finish();
        });
        clMain.setOnClickListener(v->{
            finish();
        });
        V2TIMManager.getConversationManager().addConversationListener(new V2TIMConversationListener() {
            @Override
            public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                if (totalUnreadCount>0){
                    rtvMessageNum.setVisibility(View.VISIBLE);
                    rtvMessageNum.setText(String.valueOf(totalUnreadCount));
                }else {
                    rtvMessageNum.setVisibility(View.GONE);
                }
            }
        });

        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                if (aLong>0){
                    rtvMessageNum.setVisibility(View.VISIBLE);
                    rtvMessageNum.setText(String.valueOf(aLong));
                }else {
                    rtvMessageNum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void initView() {

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View child = scrollView.getChildAt(0);
                if (child.getHeight()<=scrollY+scrollView.getHeight()){
                    ivArrowDown.setVisibility(View.GONE);
                }else {
                    ivArrowDown.setVisibility(View.VISIBLE);
                }
            }
        });
        List<String> datas = new ArrayList<>();
        datas.add("");
        datas.add("");
        datas.add("");
        Glide.with(this).load(MyUserInstance.getInstance().getUserinfo().getAvatar()).into(ivHeader);
        banner.setAdapter(new BannerAdapter<String, BannerTextViewHolder>(datas) {
            @Override
            public BannerTextViewHolder onCreateHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(MenuMessageActivity.this).inflate(R.layout.item_banner_message,parent,false);
                return new BannerTextViewHolder(view);
            }

            @Override
            public void onBindView(BannerTextViewHolder holder, String data, int position, int size) {

            }
        });
        CircleIndicator circleIndicator = new CircleIndicator(this);
        banner.setIndicator(circleIndicator);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ObjectAnimator animator = ObjectAnimator.ofFloat(flWalletBg,"rotation",0f,-5.2f);
        animator.setDuration(1000);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        flWalletBg.setPivotX(width);
        flWalletBg.setPivotY(height);
        animator.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(flMy,"rotation",0f,-10f);
        animator1.setDuration(1000);
        flMy.setPivotX(width);
        flMy.setPivotY(height);
        animator1.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder!=null){
            unbinder.unbind();
        }
    }
}
