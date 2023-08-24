package com.daychat.live.presenter;

import android.net.Uri;

import com.daychat.live.contract.LoginContract;
import com.daychat.live.model.LoginModel;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.CodeMsg;
import com.daychat.live.model.entity.UserConfig;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.net.RxScheduler;
import com.daychat.live.util.MyUserInstance;
import com.daychat.live.util.ToastUtils;

import java.util.Date;

import io.reactivex.functions.Consumer;
import okhttp3.FormBody;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {


    private final LoginContract.Model model;

    public LoginPresenter() {
        model = new LoginModel();
    }


    public void getCommonConfig() {
        if (!isViewAttached()) {
            return;
        }


        model.getCommonConfig()
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<UserConfig>>() {
                    @Override
                    public void accept(BaseResponse<UserConfig> bean) throws Exception {
                        mView.getCommonConfig(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });


    }


    public void userLogin(String phone, String pwd) {
        if (!isViewAttached()) {
            return;
        }

        mView.showLoading();
        model.userLogin(new FormBody.Builder().add("account", phone)
                        .add("platform", "2")
                        .add("pwd", pwd).build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<UserRegist>>() {

                    @Override
                    public void accept(BaseResponse<UserRegist> bean) throws Exception {
                        mView.userLogin(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }


    public void getCode(String phone) {
        if (!isViewAttached()) {
            return;
        }

        model.getCode(new FormBody.Builder().add("mobile", phone).build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<CodeMsg>>() {

                    @Override
                    public void accept(BaseResponse<CodeMsg> bean) throws Exception {
                        mView.getCode(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }


    public void userRegist(String phone, String pwd, String smscode, String invite_code) {
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.userRegist(new FormBody.Builder()
                        .add("account", phone)
                        .add("pwd", pwd)
                        .add("smscode", smscode)
                        .add("invite_code", invite_code)

                        .build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<UserRegist>>() {

                    @Override
                    public void accept(BaseResponse<UserRegist> bean) throws Exception {
                        mView.userRegist(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }

    public void changePwd(String phone, String pwd, String smscode) {
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        model.changePwd(new FormBody.Builder().add("mobile", phone).add("pwd", pwd).add("smscode", smscode).build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<UserRegist>>() {

                    @Override
                    public void accept(BaseResponse<UserRegist> bean) throws Exception {
                        mView.userRegist(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void bindPhone(String mobile, String pwd, String smscode) {
        if (!isViewAttached()) {
            return;
        }

        model.bindPhone(new FormBody.Builder().add("mobile", mobile)
                        .add("pwd", pwd)
                        .add("uid", MyUserInstance.getInstance().getUserinfo().getId())
                        .add("token", MyUserInstance.getInstance().getUserinfo().getToken())
                        .add("smscode", smscode).build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse>() {

                    @Override
                    public void accept(BaseResponse bean) throws Exception {
                        mView.bindPhone(bean);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);

                    }
                });
    }

    @Override
    public void qqlogin(String unionid, String nick_name, String gender, String icon) {
        if (!isViewAttached()) {
            return;
        }

        model.qqlogin(new FormBody.Builder().add("unionid", unionid)
                        .add("nick_name", nick_name)
                        .add("gender", gender)
                        .add("icon", icon).build())
                .compose(RxScheduler.Flo_io_main())
                .subscribe(new Consumer<BaseResponse<UserRegist>>() {

                    @Override
                    public void accept(BaseResponse<UserRegist> bean) throws Exception {
                        mView.userLogin(bean);
                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void phoneLogin(String account, String smscode, String invite_code) {
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("platform", "2");
        builder.add("account", account);
        builder.add("smscode", smscode);

        model.phoneLogin(builder.build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<UserRegist>>() {

                    @Override
                    public void accept(BaseResponse<UserRegist> bean) throws Exception {
                        if (bean.isSuccess()) {
                            mView.phoneLogin(bean.getData());
                        }

                        mView.hideLoading();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);
                        mView.hideLoading();
                    }
                });
    }

    public void facebookLogin(String userId, Date expires) {
        mView.showLoading();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("facebook_account", userId);
        builder.add("platform", "2");
        builder.add("facebook_expire", String.valueOf(expires.getTime()));
        model.facebookLogin(builder.build()).compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(bean -> {
                    if (bean.isSuccess()) {
                        mView.userLogin(bean);
                    }
                    mView.hideLoading();
                }, throwable -> {
                    mView.hideLoading();
                });
    }


    public void googleLogin(String id, String displayName, Uri profilePictureUri) {
        mView.showLoading();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("google_account",id);
        builder.add("google_account_name",displayName);
        builder.add("platform", "2");
        builder.add("google_profile",profilePictureUri.toString());
        model.googleLogin(builder.build()).compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(bean -> {
                    if (bean.isSuccess()) {
                        mView.userLogin(bean);
                    }
                    mView.hideLoading();
                }, throwable -> {
                    mView.hideLoading();
                });
    }

    public void emailLogin(String email, String code) {
        mView.showLoading();
        FormBody.Builder builder= new FormBody.Builder();
        builder.add("email",email);
        builder.add("code",code);
        builder.add("platform","2");
        model.emailLogin(builder.build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(bean -> {
                    if (bean.isSuccess()){
                        mView.userLogin(bean);
                    }
                    mView.hideLoading();
                }, throwable -> {
                    ToastUtils.showT(throwable.getMessage());
                    mView.hideLoading();
                });


    }

    public void getEmailCode(String email) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("email",email);
        mView.showLoading();
        model.sendEmailCode(builder.build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(response -> {
                    if (response.isSuccess()){
                        ToastUtils.showT("성공적으로 전송");
                        mView.sendCode();
                    }
                    mView.hideLoading();
                }, throwable -> {
                    mView.hideLoading();
                });

    }
}
