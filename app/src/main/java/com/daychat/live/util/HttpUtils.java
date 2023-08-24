package com.daychat.live.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daychat.live.net.APIService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.daychat.live.ui.act.LoginActivity;
import com.meiyinzb.nasinet.utils.AppManager;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class HttpUtils {

    private static HttpUtils mSingleMode;
    private Context mContext;

    private HttpUtils() {

    }

    public static synchronized HttpUtils getInstance() {
        if (mSingleMode == null) {
            mSingleMode = new HttpUtils();
        }

        return mSingleMode;
    }




    public boolean swtichStatus(JSONObject data){
        switch (data.getString("status")){
            case "0":

                return true;

            case "2":
                ToastUtils.showT("当前账号在其他地方登陆");
                Hawk.remove("USER_REGIST");
                MyUserInstance.getInstance().setUserInfo(null);
                AppManager.getAppManager().startActivity(LoginActivity.class);
              //  AppManager.getAppManager().finishAllActivity();

                return false;
            default:
                ToastUtils.showT(data.getString("msg"));
                return false;
        }


    }


    //截取一个文件加载显示时传入的一个本地完整路径
    public static String subFileFullName(String fileName,String fileUrl){
        String cutName=fileName+fileUrl.substring(fileUrl.lastIndexOf("."));  //这里获取的是  恰似寒光遇骄阳.txt
        return cutName;
    }

    //获取用户信息
    public void getCommentReplys(String commentid,String lastid,StringCallback stringCallback) {
        List a=new ArrayList();
        if(lastid.equals("")){
            OkGo.<String>post(APIService.baseUrl + APIService.GetCommentReplys)
                    .tag(this)
                    .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                    .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                    .params("commentid",  commentid)

                    .params("size", "20")
                    .execute(stringCallback);
        }else{
            OkGo.<String>post(APIService.baseUrl + APIService.GetCommentReplys)
                    .tag(this)
                    .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                    .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                    .params("commentid",  commentid)
                    .params("lastid",  lastid)
                    .params("size", "20")
                    .execute(stringCallback);
        }

    }
    public  void cleanAll(){
        OkGo.getInstance().cancelAll();
    }
    //获取用户信息
    public void getTempUserKey(String temp_uid,StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.GetTempUserKey)
                .params("temp_uid", temp_uid)
                .tag(this)
                .execute(stringCallback);
    }





    //获取用户信息
    public void getCommentShort(String videoid,String lastid,StringCallback stringCallback) {
        if(lastid.equals("")){
            OkGo.<String>post(APIService.baseUrl + APIService.GetCommentShort)
                    .tag(this)
                    .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                    .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                    .params("videoid",  videoid)
                    .params("size", "20")
                    .execute(stringCallback);
        }else{
            OkGo.<String>post(APIService.baseUrl + APIService.GetCommentShort)
                    .tag(this)
                    .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                    .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                    .params("videoid",  videoid)
                    .params("lastid",  lastid)
                    .params("size", "20")
                    .execute(stringCallback);
        }

    }







    //获取用户信息
    public void likeVideo(String videoid,String type,StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.LikeVideo)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token", MyUserInstance.getInstance().getUserinfo().getToken())
                .params("videoid", videoid)
                .params("type", type)
                .execute(stringCallback);
    }


    public void likeMoment(String momentid,StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.LikeMoment)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token", MyUserInstance.getInstance().getUserinfo().getToken())
                .params("momentid", momentid)

                .execute(stringCallback);
    }





    //编辑用户资料3
    public void editUserInfo(String avatar, String nick_name,String signature,String gender,String height,String weight,String constellation,String city,String age,String career,String photos, StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.EditUserInfo)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("nick_name", nick_name)
                .params("avatar", avatar)
                .params("gender", gender)
                .params("height", height)
                .params("weight", weight)
                .params("constellation", constellation)
                .params("city", city)
                .params("age", age)
                .params("career", career)
                .params("photos", photos)
                //个性签名
                .params("signature", signature)
                .execute(stringCallback);
    }

    //获取用户信息
    public void likeComment(String commentid,StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.LikeComment)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("commentid",  commentid)

                .execute(stringCallback);


    }


    //获取用户信息
    public void attentAnchor(String anchorid,String type,StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.AttentAnchor)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("anchorid",  anchorid)
                .params("type",  type)
                .execute(stringCallback);


    }

    //获取用户信息
    public void getUserInfo(StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.APP_GET_USER_INFO)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .execute(stringCallback);
    }


    //腾讯上传零时
    public void getTempKeysForCos(StringCallback stringCallback) {

        OkGo.<String>post(APIService.baseUrl + APIService.GET_TempKeysForCos)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .tag(this)
                .execute(stringCallback);
    }



    public void getWxPayOrder(String itemid,StringCallback stringCallback) {
        OkGo.<String>post(APIService.baseUrl + APIService.GetWxPayOrder)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("itemid", itemid)

                .execute(stringCallback);
    }

    public void getAliPayOrder(String itemid,StringCallback stringCallback) {
        OkGo.<String>post(APIService.baseUrl + APIService.GetAliPayOrder)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("itemid", itemid)

                .execute(stringCallback);
    }


    public void getVipWxPayOrder(String level,StringCallback stringCallback) {
        OkGo.<String>post(APIService.baseUrl + APIService.GetVipWxPayOrder)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("level", level)

                .execute(stringCallback);
    }


    public void getVipAliPayOrder(String level,StringCallback stringCallback) {
        OkGo.<String>post(APIService.baseUrl + APIService.GetVipAliPayOrder)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("level", level)

                .execute(stringCallback);
    }

    //开店支付保证金
    public void payDeposit(String pay_channel,StringCallback stringCallback) {
        OkGo.<String>post(APIService.baseUrl + APIService.PayDeposit)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("pay_channel",pay_channel)
                .execute(stringCallback);
    }

    public void getAliShopPayOrder(String order_no,String total_fee,StringCallback stringCallback) {
        OkGo.<String>post(APIService.baseUrl + APIService.GetAliShopPayOrder)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("order_no",order_no)
                .params("total_fee",total_fee)
                .execute(stringCallback);
    }

    public void getWxShopPayOrder(String order_no,String total_fee,StringCallback stringCallback) {
        OkGo.<String>post(APIService.baseUrl + APIService.GetWxShopPayOrder)
                .tag(this)
                .params("uid", MyUserInstance.getInstance().getUserinfo().getId())
                .params("token",  MyUserInstance.getInstance().getUserinfo().getToken())
                .params("order_no",order_no)
                .params("total_fee",total_fee)
                .execute(stringCallback);
    }



    public JSONObject check(Response<String> response) {
        if (response != null) {
            JSONObject jsonObject = JSON.parseObject(response.body());
            return jsonObject;

        }else{
            return null;
        }

    }





}