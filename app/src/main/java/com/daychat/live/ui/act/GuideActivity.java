package com.daychat.live.ui.act;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daychat.live.base.BaseMvpActivity;
import com.daychat.live.base.MyApp;
import com.daychat.live.contract.LoginContract;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.UserConfig;
import com.daychat.live.presenter.LoginPresenter;
import com.daychat.live.ui.adapter.BannerTextViewHolder;
import com.daychat.live.util.MyUserInstance;
import com.litao.slider.NiftySlider;
import com.daychat.live.R;
import com.daychat.live.util.DpUtil;
import com.meiyinzb.nasinet.userconfig.AppStatusManager;
import com.meiyinzb.nasinet.utils.AppManager;
import com.orhanobut.hawk.Hawk;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GuideActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {


    private Unbinder unbinder;

    @BindView(R.id.banner)
    Banner banner;

    @BindView(R.id.nifty_slider)
    NiftySlider niftySlider;


    @BindView(R.id.fl_slider)
    FrameLayout flSlider;

    @BindView(R.id.iv_top_line)
    ImageView ivTopLine;

    @BindView(R.id.iv_bottom_line)
    ImageView ivBottomLine;

    @BindView(R.id.fl_lines)
    FrameLayout flLines;


    @BindView(R.id.bg_slider)
    FrameLayout bgSlider;

    @BindView(R.id.iv_end_circle)
    ImageView ivEndCircle;

    @BindView(R.id.tv_start)
    TextView tvStart;

    @BindView(R.id.ll_start_view)
    LinearLayout llStartView;

    @BindView(R.id.iv_start1)
    ImageView ivStartView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initData() {
        MyUserInstance.getInstance().initVistor();
//        PkScreenUtils.getAndroiodScreenProperty(this);
        mPresenter.getCommonConfig();
    }

    @Override
    public void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        hideTitle(true);
        ((MyApp) MyApp.getInstance()).setLogin_mode(false);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        AppStatusManager.getInstance().setAppStatus(1);
        MyUserInstance.getInstance().setDevice_height(height);
        MyUserInstance.getInstance().setDevice_width(width);
        List<String> datas = new ArrayList<>();
        datas.add("");
        datas.add("");
        datas.add("");
        banner.setAdapter(new BannerAdapter<String, BannerTextViewHolder>(datas) {
            @Override
            public BannerTextViewHolder onCreateHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(GuideActivity.this).inflate(R.layout.item_guide, parent, false);
                return new BannerTextViewHolder(view);
            }

            @Override
            public void onBindView(BannerTextViewHolder holder, String data, int position, int size) {

            }
        });
        CircleIndicator circleIndicator = new CircleIndicator(this);
        banner.setIndicator(circleIndicator);
        niftySlider.setThumbCustomDrawable(R.mipmap.ic_circle_green_yellow);
        niftySlider.setOnValueChangeListener(new NiftySlider.OnValueChangeListener() {
            @Override
            public void onValueChange(@NonNull NiftySlider slider, float value, boolean fromUser) {
                if (value > 0) {
                    niftySlider.setThumbCustomDrawable(R.mipmap.ic_white_circle);
                }
                if (value == 0) {
                    niftySlider.setThumbCustomDrawable(R.mipmap.ic_circle_green_yellow);
                }

            }
        });
        niftySlider.setOnSliderTouchListener(new NiftySlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull NiftySlider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull NiftySlider slider) {
                if (niftySlider.getValue() < 100) {
                    slider.setValue(0);
                } else {
                    slider.setEnabled(false);
                    startAnim();
                }
            }
        });
        niftySlider.setThumbWidthAndHeight(DpUtil.dp2px(45), DpUtil.dp2px(45), 0);
    }

    private void startAnim() {

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(llStartView, "scaleX", 1f, 0.25f);
        animator2.setDuration(1000);
        animator2.start();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(llStartView, "translationX", 0f, 200f);
        animator3.setDuration(1000);
        animator3.start();
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(ivStartView, "translationX", 0f, 460f);
        animator4.setDuration(1000);
        animator4.start();
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(bgSlider, "scaleX", 1f, 0.25f);
        animator5.setDuration(1000);
        animator5.start();
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(bgSlider, "translationX", 0f, 210f);
        animator6.setDuration(1000);
        animator6.start();

        niftySlider.setVisibility(View.GONE);
        bgSlider.setVisibility(View.VISIBLE);
        ivEndCircle.setVisibility(View.VISIBLE);
        tvStart.setVisibility(View.GONE);
        new Handler().postDelayed(() -> {
            AppManager.getAppManager().startActivity(LoginActivity.class);
            finish();
        }, 1200);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @Override
    public void getCommonConfig(BaseResponse<UserConfig> bean) {

        if (bean.isSuccess()) {
            UserConfig userConfig = bean.getData();
            MyUserInstance.getInstance().setUserConfig(userConfig);
            Hawk.put("USER_CONFIG", JSON.toJSONString(userConfig));

        } else {
            Toast.makeText(this, bean.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }
}
