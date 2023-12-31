package com.daychat.live.contract;

import com.daychat.live.base.BaseView;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.CodeMsg;
import com.daychat.live.model.entity.UserConfig;
import com.daychat.live.model.entity.UserRegist;

import io.reactivex.Flowable;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public interface LoginContract {
    interface Model {
        Flowable<BaseResponse<UserConfig>> getCommonConfig();
        Flowable<BaseResponse<UserRegist>> userLogin(RequestBody body);
        Flowable<BaseResponse<CodeMsg>> getCode(RequestBody body);
        Flowable<BaseResponse<UserRegist>> userRegist(RequestBody body);
        Flowable<BaseResponse<UserRegist>> changePwd(RequestBody body);
        Flowable<BaseResponse> bindPhone(FormBody body);
        Flowable<BaseResponse<UserRegist>> qqlogin(FormBody body);

        Flowable<BaseResponse<UserRegist>> phoneLogin(@Body FormBody body);

        Flowable<BaseResponse<UserRegist>> facebookLogin(@Body FormBody build);

        Flowable<BaseResponse<UserRegist>> googleLogin(@Body FormBody build);

        Flowable<BaseResponse<UserRegist>> emailLogin(@Body FormBody build);

        Flowable<BaseResponse> sendEmailCode(@Body FormBody builder);

    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

       default void getCommonConfig(BaseResponse<UserConfig> bean){}
        default  void userLogin(BaseResponse<UserRegist> bean){}
        default void getCode(BaseResponse<CodeMsg> bean){}
        default void userRegist(BaseResponse<UserRegist> bean){}
        default void bindPhone(BaseResponse response) {
        }
        default void phoneLogin(UserRegist response) {
        }

        default void sendCode(){

        }

    }

    interface Presenter {
        /**
         * 获取用户配置信息
         *
         */
        void getCommonConfig();
        void userLogin(String phone,String pwd);
        void getCode(String phone);
        void userRegist(String phone,String pwd,String smscode,String invite_code);
        void changePwd(String phone,String pwd,String smscode);
        void bindPhone(String mobile,String pwd,String code);
        void qqlogin(String unionid,String nick_name,String gender,String icon);
         void phoneLogin(String account,String smscode,String invite_code) ;
    }
}
