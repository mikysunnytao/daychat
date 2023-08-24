package com.daychat.live.ui.act;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.daychat.live.R;
import com.daychat.live.base.BaseActivity;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import net.csdn.roundview.CircleImageView;
import net.csdn.roundview.RoundFrameLayout;
import net.csdn.roundview.RoundTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MenuWalletActivity extends BaseActivity {

    @BindView(R.id.fl_my)
    RoundFrameLayout flMy;

    @BindView(R.id.fl_message)
    RoundFrameLayout flMessage;

    @BindView(R.id.wallet)
    TextView tvWallet;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    @BindView(R.id.iv_header)
    CircleImageView ivHeader;

    @BindView(R.id.fl_blur)
    FrameLayout flBlur;

    @BindView(R.id.cl_main)
    ConstraintLayout clMain;


    @BindView(R.id.rt_message_num)
    RoundTextView rtvMessageNum;

    @BindView(R.id.ll_coupon)
    LinearLayout llCoupon;

    @BindView(R.id.ll_shopping)
    LinearLayout llShopping;

    @BindView(R.id.ll_buy_record)
    LinearLayout llBuyRecord;

    @BindView(R.id.ll_store)
    LinearLayout llStore;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_menu);
        unbinder = ButterKnife.bind(this);
//        Glide.with(this).load(MyUserInstance.getInstance().getUserinfo().getAvatar()).into(ivHeader);

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View child = scrollView.getChildAt(0);
                if (child.getHeight()<=scrollY+scrollView.getHeight()){
                    flBlur.setVisibility(View.GONE);
                }else {
                    flBlur.setVisibility(View.VISIBLE);
                }
            }
        });
        initListener();
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onUnreadCount(UnReadBus message) {
//        if (message.unread > 0) {
//            rtvMessageNum.setVisibility(View.VISIBLE);
//            rtvMessageNum.setText(String.valueOf(message.unread));
////            tv_red_hot.setVisibility(View.VISIBLE);
////            tv_red_hot.setText(String.valueOf(message.unread));
//        } else {
//            rtvMessageNum.setVisibility(View.GONE);
////            tv_red_hot.setVisibility(View.GONE);
//        }
//    }

    private void initListener() {
        flMessage.setOnClickListener(v->{
            startActivity(new Intent(this, MenuMessageActivity.class));
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
        llCoupon.setOnClickListener(v->{
            BottomDialog.show("쿠폰", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            });
        });
        llShopping.setOnClickListener(v->{
            BottomDialog.show("상점", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            });
        });
        llBuyRecord.setOnClickListener(v->{
            BottomDialog.show("구매내역", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            });
        });
        llStore.setOnClickListener(v->{
            BottomDialog.show("창고", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {

                }
            });
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

    @Override
    protected void onStart() {
        super.onStart();
        ObjectAnimator animator = ObjectAnimator.ofFloat(flMy,"rotation",0f,-5.2f);
        animator.setDuration(1000);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        flMy.setPivotX(width);
        flMy.setPivotY(height);
        animator.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(flMessage,"rotation",0f,-10f);
        animator1.setDuration(1000);
        flMessage.setPivotX(width);
        flMessage.setPivotY(height);
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
