package com.daychat.live.ui.act;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daychat.live.R;
import com.daychat.live.base.BaseMvpActivity;
import com.daychat.live.contract.HomeContract;
import com.daychat.live.eventbus.MessageBus;
import com.daychat.live.eventbus.UnReadBus;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.presenter.HomePresenter;
import com.daychat.live.util.MyUserInstance;
import com.daychat.live.util.WordUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;

import net.csdn.roundview.CircleImageView;
import net.csdn.roundview.RoundImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class MyActivity extends BaseMvpActivity<HomePresenter> implements HomeContract.View {

    @BindView(R.id.frag_my_txt_nickname)
    TextView frag_my_txt_nickname;
    @BindView(R.id.frag_my_txt_id)
    TextView frag_my_txt_id;

    @BindView(R.id.tv_num_follow)
    TextView tv_num_follow;

    @BindView(R.id.tv_num_fans)
    TextView tv_num_fans;
    @BindView(R.id.tv_authentication)
    TextView tv_authentication;

    @BindView(R.id.tv_num_guest)
    TextView tv_num_guest;

    @BindView(R.id.frag_my_img_icon)
    RoundImageView frag_my_img_icon;

    @BindView(R.id.tv_red_hot)
    TextView tv_red_hot;


    @BindView(R.id.iv_user_level)
    ImageView iv_user_level;
    @BindView(R.id.iv_anchor_level)
    ImageView iv_anchor_level;
    @BindView(R.id.iv_vip)
    ImageView iv_vip;
    @BindView(R.id.iv_edit)
    ImageView iv_edit;
    @BindView(R.id.rl_no_login)
    RelativeLayout rl_no_login;
    @BindView(R.id.rl_top_itmes)
    RelativeLayout rl_top_itmes;

    @BindView(R.id.tv_no_login)
    TextView tv_no_login;
    @BindView(R.id.rl_room_manager)
    RelativeLayout rl_room_manager;




    @Override
    protected void initView() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        EventBus.getDefault().register(this);
        initPage();


    }

    private void initPage() {
        frag_my_txt_nickname.setText(MyUserInstance.getInstance().getUserinfo().getNick_name());
        frag_my_txt_id.setText("ID：" + MyUserInstance.getInstance().getUserinfo().getId());
        Glide.with(this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(MyUserInstance.getInstance().getUserinfo().getAvatar()).into(frag_my_img_icon);
        if (MyUserInstance.getInstance().getUserinfo().getIs_anchor().equals("1")) {
            tv_authentication.setText(WordUtil.getString(R.string.LiveLog));

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnreadCount(UnReadBus message) {
        if (message.unread > 0) {
            tv_red_hot.setVisibility(View.VISIBLE);
            tv_red_hot.setText(String.valueOf(message.unread));
        } else {
            tv_red_hot.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rl_room_manager, R.id.rl_my_level, R.id.rl_my_trend, R.id.rl_my_short, R.id.rl_wallet, R.id.rl_income, R.id.rl_authentication, R.id.ll_follow, R.id.ll_fans, R.id.iv_pay, R.id.rl_setting, R.id.iv_message,
            R.id.rl_intimacy, R.id.tv_no_login, R.id.rl_business, R.id.rl_vip_center, R.id.rl_invite, R.id.rl_collection, R.id.frag_my_img_icon, R.id.iv_edit, R.id.rl_my_shop, R.id.rl_guild, R.id.rl_customer})
    public void onClick(View view) {
        if (isFastClick()) {
            return;
        }
        Intent i = null;
        switch (view.getId()) {
            case R.id.rl_my_level:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, MyLevelActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_my_trend:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, MyTrendActivity.class);
//                    startActivity(i);
//                }

                break;
            case R.id.rl_my_short:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, MyShortVideoActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_wallet:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, MyWalletActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_income:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, MyIncomeActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_authentication:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    if (MyUserInstance.getInstance().getUserinfo().getIs_anchor().equals("0")) {
//                        i = new Intent(this, ApplyAnchorActivity.class).putExtra("TYPE", 1);
//                    } else if (MyUserInstance.getInstance().getUserinfo().getIs_anchor().equals("1")) {
//                        i = new Intent(this, LiveManageActivity.class);
//                        tv_authentication.setText(WordUtil.getString(R.string.LiveLog));
//                    }
//                    startActivity(i);
//                }
                break;
            case R.id.rl_room_manager:
//                i = new Intent(this, RoomManagerActivity.class);
//                startActivity(i);
                break;


            case R.id.ll_follow:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, FollowActivity.class).putExtra("index", 0);
//                    startActivity(i);
//                }
                break;
            case R.id.ll_fans:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, FollowActivity.class).putExtra("index", 1);
//                    startActivity(i);
//                }
                break;
            case R.id.iv_pay:
                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, ToPayActivity.class);
//                    startActivity(i);
                }
                break;

            case R.id.iv_message:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, AllMessageActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_intimacy:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, IntimacyListActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_vip_center:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, BuyMemberActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_invite:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, InviteFriendActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_collection:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, CollectActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.frag_my_img_icon:


            case R.id.iv_edit:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//
//                    i = new Intent(this, MySettingActivity.class);
//                    startActivity(i);
//                }
                break;
            case R.id.rl_guild:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, WebShopActivity.class);
//                    i.putExtra("jump_url", APIService.Guild + "token=" + URLEncoder.encode(MyUserInstance.getInstance().getUserinfo().getToken()) + "&uid=" + MyUserInstance.getInstance().getUserinfo().getId());
//                    startActivity(i);
//                }
                break;
            case R.id.rl_my_shop:
//                if (MyUserInstance.getInstance().visitorIsLogin()) {
//                    i = new Intent(this, WebShopActivity.class);
//                    i.putExtra("jump_url", APIService.Personal + "token=" + URLEncoder.encode(MyUserInstance.getInstance().getUserinfo().getToken()) + "&uid=" + MyUserInstance.getInstance().getUserinfo().getId());
//                    startActivity(i);
//
//                }

                break;
            case R.id.rl_business:

//                i = new Intent(this, WebShopActivity.class);
//                i.putExtra("jump_url", APIService.Business);
//
//                startActivity(i);
                break;
            case R.id.tv_no_login:
//                AppManager.getAppManager().startActivity(PhoneLoginActivity.class);
                break;
            case R.id.rl_setting:
//                i = new Intent(this, SettingActivity.class);
//                startActivity(i);
                break;
            case R.id.rl_customer:
//                i = new Intent(this, CustomerServiceActivity.class);
//                startActivity(i);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (MyUserInstance.getInstance().hasToken()) {
            mPresenter.getUserInfo();
            rl_no_login.setVisibility(View.GONE);
            rl_top_itmes.setVisibility(View.VISIBLE);
        } else {
            rl_no_login.setVisibility(View.VISIBLE);
            rl_top_itmes.setVisibility(View.GONE);
        }
    }


    @Override
    public void setUserInfo(UserRegist bean) {

        if (bean == null) {
            return;
        }
        frag_my_txt_nickname.setText(bean.getNick_name());
        tv_num_follow.setText(bean.getAttent_count());
        tv_num_fans.setText(bean.getFans_count());
        tv_num_guest.setText(bean.getVisitor_count());
        if (bean.getIs_anchor().equals("1")) {
            tv_authentication.setText(WordUtil.getString(R.string.LiveLog));
        } else {
            tv_authentication.setText(WordUtil.getString(R.string.AnchorAuthentication));
        }
        String faceurl = bean.getAvatar() ;//App.getUser().headimgurl
        String nickname = bean.getNick_name();
        V2TIMUserFullInfo info = new V2TIMUserFullInfo();
        info.setNickname(nickname);
        info.setFaceUrl(faceurl);
        V2TIMManager.getInstance().setSelfInfo(info,new V2TIMCallback(){

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(int i, String s) {

            }
        });
        frag_my_txt_id.setText("ID：" + MyUserInstance.getInstance().getUserinfo().getId());
        Glide.with(this).load(bean.getAvatar()).into(frag_my_img_icon);

//        Glide.with(this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(LevelUtils.getUserLevel(bean.getUser_level())).into(iv_user_level);

        if (null != bean.getIs_anchor()) {
            if (bean.getIs_anchor().equals("0")) {
                iv_anchor_level.setVisibility(View.GONE);
            } else {

                iv_anchor_level.setVisibility(View.VISIBLE);
//                Glide.with(this).applyDefaultRequestOptions(new RequestOptions().centerCrop()).load(LevelUtils.getAnchorLevel(bean.getAnchor_level())).into(iv_anchor_level);
            }
        } else {
            iv_anchor_level.setVisibility(View.GONE);
        }

        if (bean.getVip_level() != 0) {

            if (MyUserInstance.getInstance().OverTime(bean.getVip_date())) {
//                Glide.with(this).applyDefaultRequestOptions(new RequestOptions().centerCrop())
//                        .load(ArmsUtils.getVipLevelIcon(bean.getVip_level(), bean.getVip_date())).into(iv_vip);
//                iv_vip.setVisibility(View.VISIBLE);
            } else {
                iv_vip.setVisibility(View.GONE);
            }
        } else {
            iv_vip.setVisibility(View.GONE);
        }

        MyUserInstance.getInstance().setUserInfo(bean);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageBus message) {

        int num = Integer.valueOf(message.message);
        String result = "0";
        if (num > 99) {
            result = "99+";
        } else {
            result = String.valueOf(num);
        }
        if (!result.equals("0")) {
            tv_red_hot.setVisibility(View.VISIBLE);
            tv_red_hot.setText(result);
        } else {
            tv_red_hot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
