package com.daychat.live.model;

import com.daychat.live.contract.LoginContract;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.CodeMsg;
import com.daychat.live.model.entity.UserConfig;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.net.RetrofitClient;

import io.reactivex.Flowable;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginModel implements LoginContract.Model {


    @Override
    public Flowable<BaseResponse<UserConfig>> getCommonConfig() {
        return RetrofitClient.getInstance().getApi().getCommonConfig();
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> userLogin(RequestBody body) {
        return RetrofitClient.getInstance().getApi().userLogin(body);
    }


    @Override
    public Flowable<BaseResponse<CodeMsg>> getCode(RequestBody phone) {
        return RetrofitClient.getInstance().getApi().getCode(phone);
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> userRegist(RequestBody body) {
        return RetrofitClient.getInstance().getApi().userRegist(body);
    }
    @Override
    public Flowable<BaseResponse<UserRegist>> changePwd(RequestBody body) {
        return RetrofitClient.getInstance().getApi().changePwd(body);
    }
    @Override
    public Flowable<BaseResponse> bindPhone(FormBody body) {
        return RetrofitClient.getInstance().getApi().bindPhone(body);
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> qqlogin(FormBody body) {
        return RetrofitClient.getInstance().getApi().qqlogin(body);
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> phoneLogin(FormBody body) {
        return RetrofitClient.getInstance().getApi().phoneLogin(body);
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> facebookLogin(FormBody build) {
        return RetrofitClient.getInstance().getApi().facebookLogin(build);
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> googleLogin(FormBody build) {
        return RetrofitClient.getInstance().getApi().googleLogin(build);
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> emailLogin(FormBody build) {
        return RetrofitClient.getInstance().getApi().emailLogin(build);
    }

    @Override
    public Flowable<BaseResponse> sendEmailCode(FormBody body) {
        return RetrofitClient.getInstance().getApi().sendEmailCode(body);
    }
}
