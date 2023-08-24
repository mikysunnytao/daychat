package com.daychat.live.ui.act;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daychat.live.R;
import com.daychat.live.base.BaseMvpActivity;
import com.daychat.live.contract.LoginContract;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.UserConfig;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.net.APIService;
import com.daychat.live.presenter.LoginPresenter;
import com.daychat.live.util.MyUserInstance;
import com.daychat.live.util.ToastUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.meiyinzb.nasinet.utils.AppManager;
import com.orhanobut.hawk.Hawk;
import com.tencent.qcloud.tuicore.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    private GoogleSignInClient googleSignInClient;


    private CallbackManager callbackManager;

    private ProfileTracker profileTracker;
    private String TAG = LoginActivity.class.getName();

    @OnClick({R.id.phone_login, R.id.tv_rule, R.id.facebook_login, R.id.google_login,R.id.email_login})
    public void onClick(View view) {
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.google_login:
//                MyUserInstance.getInstance().getWxPayBuilder(this).wxLogin();
                signIn();
                break;
            case R.id.facebook_login:
                boolean currentAccessTokenActive = AccessToken.isCurrentAccessTokenActive();
//                if (!currentAccessTokenActive){
                    List<String> permissions = new ArrayList<>();
                    permissions.add("public_profile");
                    permissions.add("openid");
                    LoginManager.getInstance().logIn(this,callbackManager,permissions);
//                }
                break;
            case R.id.phone_login:
                startActivity(new Intent(this, PhoneLoginActivity.class));
                break;
            case R.id.tv_rule:
//                Intent i_2 = new Intent(LoginActivity.this, WebShopActivity.class);
//                i_2.putExtra("jump_url", APIService.Privacy_1);
//                startActivity(i_2);
                break;
            case R.id.email_login:
                startActivity(new Intent(this, EmailLoginActivity.class));
                break;
        }
    }

    private ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            try {
                SignInCredential credential = Identity.getSignInClient(this).getSignInCredentialFromIntent(result.getData());
                ToastUtils.showT("Google login success id:" + credential.getId());
                Log.i(TAG, "google login get account info id:" + credential.getId());
                Log.i(TAG, "google login get account info googleIdToken:" + credential.getGoogleIdToken());
                Log.i(TAG, "google login get account info password:" + credential.getPassword());
                Log.i(TAG, "google login get account info givenName:" + credential.getGivenName());
                Log.i(TAG, "google login get account info familyName:" + credential.getFamilyName());
                Log.i(TAG, "google login get account info displayName:" + credential.getDisplayName());
                Log.i(TAG, "google login get account info profilePictureUri:" + credential.getProfilePictureUri());
                mPresenter.googleLogin(credential.getId(),credential.getDisplayName(),credential.getProfilePictureUri());
                SPUtils.getInstance().put("loginType","google");
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        LoginManager.getInstance().unregisterCallback(callbackManager);
        profileTracker.stopTracking();
        super.onDestroy();
    }

    private void signIn() {
        GetSignInIntentRequest request = new GetSignInIntentRequest.Builder()
                .setServerClientId("142126616100-9t5mig5c9pkcjqa75mbln5r59f2ntbpu.apps.googleusercontent.com")
                .build();
        Identity.getSignInClient(this).getSignInIntent(request).addOnSuccessListener(pendingIntent -> {
            launcher.launch(new IntentSenderRequest.Builder(pendingIntent).build());
        }).addOnFailureListener(e -> {
            Log.i(TAG, "google call login failed message:${it.message}");
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, options);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {

        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "Meta login success");
                Log.i(TAG, "Meta login account info userId:${result.accessToken.userId}" + loginResult.getAccessToken().getUserId());
                Log.i(TAG, "Meta login account info token:${result.accessToken.token}" + loginResult.getAccessToken().getToken());
                Log.i(TAG,loginResult.toString());
                Log.i(TAG, "Meta login account info applicationId:${result.accessToken.applicationId}" + loginResult.getAccessToken().getApplicationId());
                ToastUtils.showT("Meta login success userId:${result.accessToken.userId}" + loginResult.getAccessToken().getUserId());
                mPresenter.facebookLogin(loginResult.getAccessToken().getUserId(),loginResult.getAccessToken().getExpires());
                SPUtils.getInstance().put("loginType","facebook");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(@Nullable Profile profile, @Nullable Profile profile1) {
                if (profile!=null) {
                    Log.i(TAG, "Meta  onCurrentProfileChanged id:$id" + profile.getId());
                    Log.i(TAG, "Meta  onCurrentProfileChanged firstName:$firstName" + profile.getFirstName());
                    Log.i(TAG, "Meta  onCurrentProfileChanged middleName:$middleName" + profile.getMiddleName());
                    Log.i(TAG, "Meta  onCurrentProfileChanged lastName:$lastName" + profile.getLastName());
                    Log.i(TAG, "Meta  onCurrentProfileChanged name:$name" + profile.getName());
                    Log.i(TAG, "Meta  onCurrentProfileChanged pictureUri:$pictureUri" + profile.getPictureUri());
                }
            }
        };
        profileTracker.startTracking();
    }

    @Override
    protected void initView() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        hideTitle(true);
        JSONObject userRegist_string = (JSONObject) JSON.parse(Hawk.get("USER_REGIST"));
        UserRegist userRegist = JSON.toJavaObject(userRegist_string, UserRegist.class);
        JSONObject userConfig_string = (JSONObject) JSON.parse(Hawk.get("USER_CONFIG"));
        UserConfig userConfig = JSON.toJavaObject(userConfig_string, UserConfig.class);
        String loginType = SPUtils.getInstance().getString("loginType");
        if (loginType!=null){
            if (loginType.equals("facebook")){
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken!=null && !accessToken.isExpired();
                if (!isLoggedIn){
                    ToastUtils.showT("登录已失效");
                    return;
                }
            }
        }
        if (null != userRegist & null != userConfig) {
            MyUserInstance.getInstance().setUserInfo(userRegist);
            MyUserInstance.getInstance().setUserConfig(userConfig);

            AppManager.getAppManager().startActivity(HomeActivity.class);
            AppManager.getAppManager().finishActivity(LoginActivity.class);
            finish();
        } else {
            if (MyUserInstance.getInstance().loginMode()) {
                MyUserInstance.getInstance().setUserConfig(userConfig);

                AppManager.getAppManager().startActivity(HomeActivity.class);
                AppManager.getAppManager().finishActivity(LoginActivity.class);
                finish();
            }
        }
    }

    @Override
    public void userLogin(BaseResponse<UserRegist> bean) {
        if (bean.isSuccess()) {
            MyUserInstance.getInstance().setUserInfo(bean.getData());
            String temp = JSON.toJSONString(bean.getData());
            Hawk.put("USER_REGIST", temp);
            Hawk.put("userId",bean.getData().getId());
            AppManager.getAppManager().startActivity(HomeActivity.class);
            AppManager.getAppManager().finishActivity();
        } else {
            ToastUtils.showT(bean.getMsg());
        }
    }


    @Override
    public void onError(Throwable throwable) {

    }
}
