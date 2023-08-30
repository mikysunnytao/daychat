package com.daychat.live.ui.act;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.UriUtils;
import com.daychat.live.R;
import com.daychat.live.base.BaseMvpActivity;
import com.daychat.live.contract.HomeContract;
import com.daychat.live.eventbus.MessageBus;
import com.daychat.live.eventbus.UnReadBus;
import com.daychat.live.im.TxImUtils;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.presenter.HomePresenter;
import com.daychat.live.ui.act.video.TrimVideoActivity;
import com.daychat.live.ui.act.video.VideoCameraActivity;
import com.daychat.live.ui.fragment.HomePageFragment1;
import com.daychat.live.ui.fragment.MyFragment;
import com.daychat.live.ui.fragment.chat.ContactFragment;
import com.daychat.live.ui.fragment.chat.ConversationFragment;
import com.daychat.live.ui.fragment.chat.MyChatFragment;
import com.daychat.live.util.MyUserInstance;
import com.daychat.live.util.ToastUtils;
import com.daychat.live.widget.Dialogs;
import com.daychat.live.widget.NoSrcollViewPager;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.meiyinzb.nasinet.utils.AppManager;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class HomeActivity extends BaseMvpActivity<HomePresenter> implements HomeContract.View {


    @BindView(R.id.vp_main)
    NoSrcollViewPager vpMain;

    @BindView(R.id.iv_release)
    ImageView ivRelease;

    @BindView(R.id.nav1)
    ImageView nav1;

    @BindView(R.id.nav2)
    ImageView nav2;

    @BindView(R.id.nav3)
    ImageView nav3;

    @BindView(R.id.nav4)
    ImageView nav4;


    @BindView(R.id.fl_menus)
    FrameLayout flMenus;

    @BindView(R.id.iv_camera)
    ImageView ivCamera;

    @BindView(R.id.main_layout)
    ConstraintLayout mainLayout;

    @BindView(R.id.drawer)
    DrawerLayout menuLayout;

    //    @BindView(R.id.iv_camera)
//    ImageView ivCamera;
//
//    @BindView(R.id.iv_message)
//    ImageView ivMessage;
//
    @BindView(R.id.iv_video)
    ImageView ivVideo;


    private HashSet<DialogFragment> mDialogFragmentSet;
    //    HomeFragment homeFragment;
    HomePageFragment1 homePageFragment;
    ConversationFragment tuiConversationFragment;

    ContactFragment tuiContactFragment;
    MyFragment myFragment;

    int translationY = AdaptScreenUtils.pt2Px(340);

    private final int now_page = 0;
    // OnGetUnRead onGetUnRead;
    private final List<Fragment> mFragments = new ArrayList<>();
    private static boolean mBackKeyPressed = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_2;
    }


    @Override
    protected void initData() {
        if (MyUserInstance.getInstance().hasToken()) {
            updateUserInfo();
        }
    }

    public void showMenu() {
        menuLayout.openDrawer(GravityCompat.END);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra("index", -1);
        if (index > 0) {
            switchTab(index);
        }
    }

    private boolean showMenu = false;

    @Override
    protected void initView() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
        mDialogFragmentSet = new HashSet<>();
        hideTitle(true);

        ivVideo.setOnClickListener(v->{
            PictureSelector.create(this).openGallery(PictureMimeType.ofVideo())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(0);
        });
        ivCamera.setOnClickListener(v -> {
            hideMenu();
            String[] permissions;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            } else {
                permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
            }
            XXPermissions.with(this).permission(permissions).request(new OnPermissionCallback() {
                @Override
                public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                    startActivity(new Intent(HomeActivity.this, VideoCameraActivity.class));
                }
            });
        });
        mainLayout.setOnTouchListener((v, event) -> {
            if (showMenu) {
                hideMenu();
            }
            return false;
        });
        ivRelease.setOnClickListener(v -> {
            //弹出菜单
            if (showMenu) {//如果弹出了，缩回去
                hideMenu();
            } else {//弹出菜单
                showMenu = true;
                flMenus.setVisibility(View.VISIBLE);
                ObjectAnimator animator = ObjectAnimator.ofFloat(flMenus, View.TRANSLATION_Y, translationY, 0);
                animator.setDuration(500);
                animator.start();
                ivRelease.setImageResource(R.drawable.bottom_menu_end);
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(ivRelease, View.TRANSLATION_Y, 0, -55f);
                animator1.setDuration(500);
                animator1.start();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAndroidNativeLightStatusBar(false);
            }
        }, 1000);


        homePageFragment = new HomePageFragment1();
        tuiContactFragment = new ContactFragment();
        myFragment = new MyFragment();
        tuiConversationFragment = new ConversationFragment();
        mFragments.add(homePageFragment);
        mFragments.add(tuiConversationFragment);
        mFragments.add(tuiContactFragment);
        mFragments.add(myFragment);
        vpMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        vpMain.setOffscreenPageLimit(4);
        nav1.setOnClickListener(v -> {
//            dealWithFragment(0);
//            vpMain.setCurrentItem(0);
//            homePageFragment.reset();
            int count = homePageFragment.getChildFragmentManager().getBackStackEntryCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    homePageFragment.getChildFragmentManager().popBackStack();
                }
            }
            if (showMenu) {
                hideMenu();
//                showMenu = !showMenu;
            }
        });

        nav3.setOnClickListener(v -> {
//            dealWithFragment(1);
//            switchShortVideoTab(1);
//            vpMain.setCurrentItem(1);
            FragmentManager manager = homePageFragment.getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            int count = manager.getBackStackEntryCount();
            if (count > 0) {
                manager.popBackStack();
            }
            String tag = MyChatFragment.class.getName();

            Fragment myChatFragment = manager.findFragmentByTag(tag);
            if (myChatFragment == null) {
                myChatFragment = new MyChatFragment();
            }
            transaction.replace(R.id.fl_content, myChatFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
            if (showMenu) {
                hideMenu();
//                showMenu = !showMenu;
            }
        });
        nav2.setOnClickListener(v -> {
            FragmentManager manager = homePageFragment.getChildFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            String tag = ContactFragment.class.getName();
            int count = manager.getBackStackEntryCount();
            if (count > 0) {
                manager.popBackStack();
            }
            Fragment contactFragment = manager.findFragmentByTag(tag);
            if (contactFragment == null) {
                contactFragment = new ContactFragment();
            }
            transaction.replace(R.id.fl_content, contactFragment);
            transaction.addToBackStack(tag);
            transaction.commit();
            if (showMenu) {
                hideMenu();
//                showMenu = !showMenu;
            }
        });
        nav4.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuMyActivity.class));
            if (showMenu) {
                hideMenu();
//                showMenu = !showMenu;
            }
        });

        EventBus.getDefault().register(this);
//
    }

    public void hideMenu() {
        if (showMenu) {
            showMenu = false;
            ObjectAnimator animator = ObjectAnimator.ofFloat(flMenus, View.TRANSLATION_Y, 0f, translationY);
            animator.setDuration(500);
            animator.start();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(ivRelease, View.TRANSLATION_Y, -55, 0);
            animator1.setDuration(500);
            animator1.start();
            new Handler().postDelayed(() -> {
                flMenus.setVisibility(View.GONE);
                ivRelease.setImageResource(R.drawable.bottom_menu_add);
            }, 500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==0){
                List<LocalMedia> selectList_title = PictureSelector.obtainMultipleResult(data);
                if (selectList_title!=null){
                    File file = UriUtils.uri2File(Uri.parse(selectList_title.get(0).getPath()));
                    TrimVideoActivity.startActivity(HomeActivity.this, file.getAbsolutePath());
                }
            }
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageBus message) {
        if (!this.isFinishing()) {

            int num = Integer.valueOf(message.message);
            String result = "0";
            if (num > 99) {
                result = "99+";
            } else {
                result = String.valueOf(num);
            }
//            if (!result.equals("0")) {
//                tv_red_hot.setVisibility(View.VISIBLE);
//                tv_red_hot.setText(result);
//            } else {
//                tv_red_hot.setVisibility(View.GONE);
//            }
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUnreadCount(UnReadBus message) {
        homePageFragment.setUnReadNum(message.unread);
        tuiConversationFragment.setUnReadNum(message.unread);
        tuiContactFragment.setUnReadNum(message.unread);

//        if (message.unread > 0) {
//            tv_red_hot.setVisibility(View.VISIBLE);
//            tv_red_hot.setText(String.valueOf(message.unread));
//        } else {

//            tv_red_hot.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onError(Throwable throwable) {

    }


    public void updateUserInfo() {
        mPresenter.getUserInfo();
    }

    @Override
    public void setUserInfo(UserRegist bean) {
        MyUserInstance.getInstance().setUserInfo(bean);
        String temp = JSON.toJSONString(bean);
        Hawk.put("USER_REGIST", temp);
        TxImUtils.getSocketManager().imLogin();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //  initIMSDK();
    }


    public void addDialogFragment(DialogFragment dialogFragment) {
        if (mDialogFragmentSet != null && dialogFragment != null) {
            mDialogFragmentSet.add(dialogFragment);
        }
    }

    public void removeDialogFragment(DialogFragment dialogFragment) {
        if (mDialogFragmentSet != null && dialogFragment != null) {
            mDialogFragmentSet.remove(dialogFragment);
        }
    }


    private Dialog dialog;

    @Override
    public void showLoading() {
        hideLoading();
        dialog = Dialogs.createLoadingDialog(this);
        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (null != dialog) {
            dialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        hideMenu();
        if (vpMain.getCurrentItem() == 0) {
            int count = homePageFragment.getChildFragmentManager().getBackStackEntryCount();
            if (count > 0) {
                homePageFragment.getChildFragmentManager().popBackStack();
                return;
            }
        }
//        if (vpMain.getCurrentItem() == 1) {
//            int count = tuiConversationFragment.getChildFragmentManager().getBackStackEntryCount();
//            if (count > 0) {
//                tuiConversationFragment.getChildFragmentManager().popBackStack();
//                return;
//            }
//        }
        if (!mBackKeyPressed) {
            ToastUtils.showT("再按一次退出程序");
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序

            AppManager.getAppManager().finishAllActivity();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void switchTab(int i) {
        vpMain.setCurrentItem(i);
        if (i == 1) {
            switchShortVideoTab(0);
        }
    }

    public void switchShortVideoTab(Integer type) {
        tuiConversationFragment.setFragment(type);
        vpMain.setCurrentItem(1);
    }
}
