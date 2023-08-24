package com.daychat.live.ui.act;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.daychat.live.R;
import com.daychat.live.base.BaseActivity;
import com.daychat.live.interfaces.OnPicUpLoadPicsListener;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.net.RetrofitClient;
import com.daychat.live.net.RxScheduler;
import com.daychat.live.util.MyUserInstance;
import com.daychat.live.util.QNiuUploadUtils;
import com.daychat.live.util.ToastUtils;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.dialogs.InputDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qiniu.droid.media.Frame;
import com.tencent.imsdk.v2.V2TIMConversationListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.zs.easy.imgcompress.EasyImgCompress;
import com.zs.easy.imgcompress.listener.OnCompressSinglePicListener;
import com.zs.easy.imgcompress.util.GBMBKBUtil;

import net.csdn.roundview.CircleImageView;
import net.csdn.roundview.RoundFrameLayout;
import net.csdn.roundview.RoundTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.FormBody;

public class MenuMyActivity extends BaseActivity {

    @BindView(R.id.fl_wallet_bg)
    FrameLayout flWalletBg;

    @BindView(R.id.fl_message)
    RoundFrameLayout flMessage;

    @BindView(R.id.wallet)
    TextView tvWallet;

    @BindView(R.id.iv_close)
    ImageView ivClose;

    @BindView(R.id.iv_user_header)
    ImageView ivUserHeader;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;


    @BindView(R.id.iv_down_arrow)
    ImageView ivArrowDown;

    @BindView(R.id.cl_main)
    ConstraintLayout clMain;

    @BindView(R.id.rt_message_num)
    RoundTextView rtvMessageNum;

    @BindView(R.id.iv_header)
    CircleImageView ivHeader;

    @BindView(R.id.ll_change_mood)
    LinearLayout llMood;

    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    @BindView(R.id.tv_mood)
    TextView tvMood;

    @BindView(R.id.ll_my_work)
    LinearLayout llMyWork;

    @BindView(R.id.ll_my_like)
    LinearLayout llMyLike;

    @BindView(R.id.ll_friend_settings)
    LinearLayout llFriendSettings;

    @BindView(R.id.ll_language_settings)
    LinearLayout llLanguageSettings;

    @BindView(R.id.ll_other_settings)
    LinearLayout llOtherSettings;

    @BindView(R.id.ll_customer_service)
    LinearLayout llCustomerService;

    @BindView(R.id.ll_privacy)
    LinearLayout llPrivacy;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);
        unbinder = ButterKnife.bind(this);


        getUserInfo();
        initListener();
    }

    private void modifyNickname(String nickname) {
        FormBody body = new FormBody.Builder().add("userid", MyUserInstance.getInstance().getUserinfo().getId()).add("nickName", nickname).build();
        RetrofitClient.getInstance().getApi().modifyNickname(body).compose(RxScheduler.Flo_io_main()).as(bindAutoDispose()).subscribe(baseResponse -> {
            ToastUtils.showT("修改成功");
            tvNickname.setText(nickname);
            MyUserInstance.getInstance().getUserinfo().setNick_name(nickname);
        }, throwable -> {
            Log.i("error", "" + throwable.getMessage());
        });

    }

    private void modifyMood(String mood) {

        FormBody body = new FormBody.Builder().add("userid", MyUserInstance.getInstance().getUserinfo().getId()).add("mood", mood).build();
        RetrofitClient.getInstance().getApi().modifyMood(body).compose(RxScheduler.Flo_io_main()).as(bindAutoDispose()).subscribe(baseResponse -> {
            ToastUtils.showT("修改成功");
            tvMood.setText(mood);
            MyUserInstance.getInstance().getUserinfo().setPersonal_mood(mood);
        }, throwable -> {

        });
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

    private void getUserInfo() {
        UserRegist userInfo = MyUserInstance.getInstance().getUserinfo();
        if (userInfo != null) {
            Glide.with(this).load(userInfo.getAvatar()).into(ivUserHeader);
            Glide.with(this).load(userInfo.getAvatar()).into(ivHeader);
            tvNickname.setText(userInfo.getNick_name());
            tvMood.setText(userInfo.getPersonal_mood());
        }
    }

    private void initListener() {
        tvNickname.setOnClickListener(v -> {
            InputDialog.show("修改昵称", "原昵称：" + tvNickname.getText().toString(), "确定", "取消").setOkButton((dialog, v1, inputStr) -> {
                modifyNickname(inputStr);
                return false;
            });

        });

        llMyWork.setOnClickListener(v -> {
            BottomDialog.show("내 작품", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    FrameLayout flContent = v.findViewById(R.id.fl_content);
                    View content = LayoutInflater.from(MenuMyActivity.this).inflate(R.layout.content_work_setting, null);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    flContent.addView(content, params);
                }
            });
        });

        llMyLike.setOnClickListener(v -> {
            BottomDialog.show("즐겨찾기", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    FrameLayout flContent = v.findViewById(R.id.fl_content);
                    View content = LayoutInflater.from(MenuMyActivity.this).inflate(R.layout.content_like_setting, null);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    flContent.addView(content, params);
                }
            });
        });
        llFriendSettings.setOnClickListener(v -> {
            BottomDialog.show("친구설정", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    FrameLayout flContent = v.findViewById(R.id.fl_content);
                    View content = LayoutInflater.from(MenuMyActivity.this).inflate(R.layout.content_friend_setting, null);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    flContent.addView(content, params);
                }
            });
        });
        llLanguageSettings.setOnClickListener(v -> {
            BottomDialog.show("언어설정", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    FrameLayout flContent = v.findViewById(R.id.fl_content);
                    View content = LayoutInflater.from(MenuMyActivity.this).inflate(R.layout.content_language_setting, null);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    flContent.addView(content, params);
                }
            });
        });
        llOtherSettings.setOnClickListener(v -> {
            BottomDialog.show("기타설정", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    FrameLayout flContent = v.findViewById(R.id.fl_content);
                    View content = LayoutInflater.from(MenuMyActivity.this).inflate(R.layout.content_other_setting, null);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    flContent.addView(content, params);
                }
            }).setAllowInterceptTouch(false);
        });
        llCustomerService.setOnClickListener(v -> {
            BottomDialog.show("고객센터", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    FrameLayout flContent = v.findViewById(R.id.fl_content);
                    View content = LayoutInflater.from(MenuMyActivity.this).inflate(R.layout.content_customer_service, null);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    flContent.addView(content, params);
                }
            });
        });
        llPrivacy.setOnClickListener(v -> {
            BottomDialog.show("개인정보 및 보안", "", new OnBindView<BottomDialog>(R.layout.dialog_my_work) {
                @Override
                public void onBind(BottomDialog dialog, View v) {
                    FrameLayout flContent = v.findViewById(R.id.fl_content);
                    View content = LayoutInflater.from(MenuMyActivity.this).inflate(R.layout.content_privacy, null);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    flContent.addView(content, params);
                }
            }).setAllowInterceptTouch(false);
        });
        llMood.setOnClickListener(v -> {
            CustomDialog.show(new OnBindView<CustomDialog>(R.layout.dialog_change_mood) {
                @Override
                public void onBind(CustomDialog dialog, View v) {
                    TextView tvModify = v.findViewById(R.id.btn_modify);
                    EditText etMood = v.findViewById(R.id.et_mood);
                    etMood.setText(tvMood.getText().toString());
                    tvModify.setOnClickListener(view -> {
                        //请求后台修改
                        modifyMood(etMood.getText().toString());
                        dialog.dismiss();
                    });
                }
            }, CustomDialog.ALIGN.CENTER);
        });
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View child = scrollView.getChildAt(0);
                if (child.getHeight() <= scrollY + scrollView.getHeight()) {
                    ivArrowDown.setVisibility(View.GONE);
                } else {
                    ivArrowDown.setVisibility(View.VISIBLE);
                }
            }
        });
        flWalletBg.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuWalletActivity.class));
            overridePendingTransition(R.anim.activity_anim_in_right, R.anim.activity_anim_out_right);
            finish();
        });
        flMessage.setOnClickListener(v -> {
            startActivity(new Intent(this, MenuMessageActivity.class));
            overridePendingTransition(R.anim.activity_anim_in_right, R.anim.activity_anim_out_right);
            finish();
        });
        ivClose.setOnClickListener(v -> {
            finish();
        });
        clMain.setOnClickListener(v -> {
            finish();
        });
        ivUserHeader.setOnClickListener(v -> {
            PictureSelector.create(this).openGallery(PictureMimeType.ofImage()).selectionMode(PictureConfig.SINGLE)
                    .maxSelectNum(1)
                    .enableCrop(true)
                    .isCamera(true)
                    .forResult(0);
        });

        V2TIMManager.getConversationManager().addConversationListener(new V2TIMConversationListener() {
            @Override
            public void onTotalUnreadMessageCountChanged(long totalUnreadCount) {
                if (totalUnreadCount > 0) {
                    rtvMessageNum.setVisibility(View.VISIBLE);
                    rtvMessageNum.setText(String.valueOf(totalUnreadCount));
                } else {
                    rtvMessageNum.setVisibility(View.GONE);
                }
            }
        });

        V2TIMManager.getConversationManager().getTotalUnreadMessageCount(new V2TIMValueCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                if (aLong > 0) {
                    rtvMessageNum.setVisibility(View.VISIBLE);
                    rtvMessageNum.setText(String.valueOf(aLong));
                } else {
                    rtvMessageNum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    List<LocalMedia> selectList_title = PictureSelector.obtainMultipleResult(data);
                    ArrayList<String> temp_title = new ArrayList<>();
                    for (int i = 0; i < selectList_title.size(); i++) {
                        temp_title.add(selectList_title.get(i).getCutPath());
                    }
                    Glide.with(this).applyDefaultRequestOptions(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)).load(temp_title.get(0)).into(ivUserHeader);
                    EasyImgCompress.withSinglePic(this, temp_title.get(0))
                            .maxPx(1200)
                            .maxSize(50)
                            .enableLog(true)
                            .setOnCompressSinglePicListener(new OnCompressSinglePicListener() {
                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onSuccess(File file) {
                                    Log.i("EasyImgCompress", "onSuccess size = " + GBMBKBUtil.getSize(file.length()) + " getAbsolutePath= " + file.getAbsolutePath());
                                    uploadAvatar(file.getAbsolutePath());
                                }

                                @Override
                                public void onError(String error) {

                                }
                            }).start();

                    break;
            }
        }
    }

    private void uploadAvatar(String pic) {
        upLoadImage(pic);
    }

    private void upLoadImage(String imagePath) {
        ArrayList<String> pics = new ArrayList<>();
        pics.add(imagePath);
        new QNiuUploadUtils().uploadPics(pics, false, new OnPicUpLoadPicsListener() {
            @Override
            public void onPicUrls(String result, String blurResult) {
                save(result);
            }

            @Override
            public void onError(Throwable exception) {
                Log.e("MenuMyActivity", "上传失败");
            }

        });
    }

    private void save(String url) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("header", url);
        builder.add("userId", MyUserInstance.getInstance().getUserinfo().getId());
        RetrofitClient.getInstance().getApi().updateUserHeader(builder.build()).compose(RxScheduler.Flo_io_main())
                .as(bindAutoDispose())
                .subscribe(stringBaseResponse -> {
                    ToastUtils.showT("更新成功");
                    MyUserInstance.getInstance().getUserinfo().setAvatar(stringBaseResponse.getData());
                    Glide.with(MenuMyActivity.this).load(stringBaseResponse.getData()).into(ivUserHeader);
                    Glide.with(MenuMyActivity.this).load(stringBaseResponse.getData()).into(ivHeader);
                }, throwable -> {
                    ToastUtils.showT("出现错误" + throwable.getMessage());
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        ObjectAnimator animator = ObjectAnimator.ofFloat(flWalletBg, "rotation", 0f, -5.2f);
        animator.setDuration(1000);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        flWalletBg.setPivotX(width);
        flWalletBg.setPivotY(height);
        animator.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(flMessage, "rotation", 0f, -10f);
        animator1.setDuration(1000);
        flMessage.setPivotX(width);
        flMessage.setPivotY(height);
        animator1.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
