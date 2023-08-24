package com.daychat.live.ui.act;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daychat.live.R;
import com.daychat.live.base.BaseMvpActivity;
import com.daychat.live.contract.LoginContract;
import com.daychat.live.eventbus.LoginChangeBus;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.UserConfig;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.presenter.LoginPresenter;
import com.daychat.live.util.CountDownUtil;
import com.daychat.live.util.MyUserInstance;
import com.daychat.live.util.RegUtils;
import com.daychat.live.util.ToastUtils;
import com.daychat.live.util.WordUtil;
import com.daychat.live.widget.Dialogs;
import com.meiyinzb.nasinet.utils.AppManager;
import com.orhanobut.hawk.Hawk;

import net.csdn.roundview.RoundTextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class EmailLoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    private static final int REGISTER_TAG = 1;
    private static final int FORGE_PASSWORD_TAG = 2;
    private Dialog dialog;
    @BindView(R.id.phone_number_et)
    EditText phone_number_et;
    @BindView(R.id.pwd_et)
    EditText pwd_et;

    @BindView(R.id.rt_getCode)
    RoundTextView rtSendCode;

    @OnClick({R.id.facebook_login, R.id.google_login, R.id.rl_back2,  R.id.login_button,R.id.rt_getCode})
    public void onClick(View view) {
        if(isFastClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.login_button:
                if (TextUtils.isEmpty(phone_number_et.getText())) {
                    ToastUtils.showT(WordUtil.getString(R.string.Enter_email));
                    return;
                }
                if (TextUtils.isEmpty(pwd_et.getText())) {
                    ToastUtils.showT(WordUtil.getString(R.string.Enter_verification_code));
                    return;
                }
                if (!RegUtils.isValidEmail(phone_number_et.getText().toString())){
                    ToastUtils.showT(WordUtil.getString(R.string.Enter_correct_email));
                    return;
                }
                mPresenter.emailLogin(phone_number_et.getText().toString(), pwd_et.getText().toString());
                break;
            case R.id.rl_back2:
                finish();
                break;
            case R.id.google_login:
                break;
            case R.id.facebook_login:

                break;
            case R.id.rt_getCode:
                if (phone_number_et.getText()==null || phone_number_et.getText().length()==0){
                    ToastUtils.showT(WordUtil.getString(R.string.Enter_email));
                    return;
                }
                if (!RegUtils.isValidEmail(phone_number_et.getText().toString())){
                    ToastUtils.showT(WordUtil.getString(R.string.Enter_correct_email));
                    return;
                }
                mPresenter.getEmailCode(phone_number_et.getText().toString());
                break;
        }


    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_email_login;
    }

    @Override
    protected void initData() {
        JSONObject userConfig_string = (JSONObject) JSON.parse(Hawk.get("USER_CONFIG"));
        UserConfig userConfig = JSON.toJavaObject(userConfig_string, UserConfig.class);
        MyUserInstance.getInstance().setUserConfig(userConfig);
        phone_number_et.setFocusable(true);
        phone_number_et.setFocusableInTouchMode(true);
        phone_number_et.requestFocus();

    }

    @Override
    protected void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        hideTitle(true);
        AppManager.getAppManager().finishOthersActivity(EmailLoginActivity.class);
    }

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
    public void onError(Throwable throwable) {

    }


    @Override
    public void userLogin(BaseResponse<UserRegist> bean) {
        if (bean.isSuccess()) {

          if(MyUserInstance.getInstance().loginMode()){
              MyUserInstance.getInstance().setUserInfo(bean.getData());
              String temp = JSON.toJSONString(bean.getData());
              Hawk.put("USER_REGIST", temp);
              Intent intent = new Intent();
              intent.putExtra("login_sucess", "1");
              setResult(1111, intent);
              AppManager.getAppManager().startActivity(HomeActivity.class);
              AppManager.getAppManager().finishActivity();
          }else{
              MyUserInstance.getInstance().setUserInfo(bean.getData());
              String temp = JSON.toJSONString(bean.getData());
              Hawk.put("USER_REGIST", temp);
              AppManager.getAppManager().startActivity(HomeActivity.class);
              AppManager.getAppManager().finishAllActivity();
          }
            EventBus.getDefault().post(LoginChangeBus.getInstance(""));
        } else {
            ToastUtils.showT(bean.getMsg());
        }
    }

    @Override
    public void sendCode() {
        new CountDownUtil(180000,1000,rtSendCode).start();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
