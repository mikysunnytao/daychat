package com.daychat.live.net;


import com.daychat.live.model.entity.AnchorHistory;
import com.daychat.live.model.entity.Banners;
import com.daychat.live.model.entity.BaseLiveInfo;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.BuyGuard;
import com.daychat.live.model.entity.CashOutHistory;
import com.daychat.live.model.entity.CheckAttend;
import com.daychat.live.model.entity.CodeMsg;
import com.daychat.live.model.entity.Comment;
import com.daychat.live.model.entity.ContributeRank;
import com.daychat.live.model.entity.Count;
import com.daychat.live.model.entity.GiftInfo;
import com.daychat.live.model.entity.Guardian;
import com.daychat.live.model.entity.GuardianInfo;
import com.daychat.live.model.entity.HomeAd;
import com.daychat.live.model.entity.HotLive;
import com.daychat.live.model.entity.Invite;

import com.daychat.live.model.entity.LiveConsume;
import com.daychat.live.model.entity.LiveInfo;
import com.daychat.live.model.entity.MatchList;
import com.daychat.live.model.entity.MomentDetail;
import com.daychat.live.model.entity.PersonalAnchorInfo;
import com.daychat.live.model.entity.ProfitLog;
import com.daychat.live.model.entity.QCloudData;
import com.daychat.live.model.entity.RoomManager;

import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.model.entity.Topic;
import com.daychat.live.model.entity.Trend;
import com.daychat.live.model.entity.UserConfig;
import com.daychat.live.model.entity.UserInfo;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.util.MyUserInstance;
import com.google.common.base.Functions;


import java.util.ArrayList;

import io.reactivex.Flowable;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author azheng
 * @date 2018/4/24.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public interface APIService {

    /**
     * 登陆
     *
     * @return
     */
     String baseUrl = "https://api.daydaychat.com/";



    String GetCommentReplys = "api/shortvideo/getCommentReplys";
    String GetCommentShort = "api/shortvideo/getComments";

    String LikeVideo = "api/shortvideo/likeVideo";
    String LikeMoment = "api/Moment/likeMoment";

    String EditUserInfo = "api/User/editUserInfo";

    String LikeComment = "api/shortvideo/likeComment";
    String AttentAnchor = "api/Anchor/attentAnchor";
    String APP_GET_USER_INFO = "api/user/getUserInfo";

    String GET_TempKeysForCos = "api/Config/getTempKeysForCos";

    String GetWxPayOrder = "api/recharge/getWxPayOrder";
    String GetAliPayOrder = "api/recharge/getAliPayOrder";
    String GetVipWxPayOrder = "api/vip/getWxPayOrder";
    String GetVipAliPayOrder = "api/vip/getAliPayOrder";
    String PayDeposit = "api/shop/payDeposit";

    String GetAliShopPayOrder = "api/shop/getAliPayOrder";

    String GetWxShopPayOrder = "api/shop/getWxPayOrder";


    String TXbaseUrl = "https://liveroom.qcloud.com/weapp/live_room/";
    //    String baseUrl = "http://192.168.0.100/";
    String Guild= MyUserInstance.getInstance().getUserConfig().getConfig().getSite_domain() +"/guild?";

    String Personal=MyUserInstance.getInstance().getUserConfig().getConfig().getSite_domain() +"/personal?";
    String Business=MyUserInstance.getInstance().getUserConfig().getConfig().getSite_domain() +"/cooperation";
    String About=MyUserInstance.getInstance().getUserConfig().getConfig().getSite_domain() +"/about";
    String Privacy_2=MyUserInstance.getInstance().getUserConfig().getConfig().getSite_domain() +"/privacy/2";
    String Privacy_1=MyUserInstance.getInstance().getUserConfig().getConfig().getSite_domain() +"/privacy/1";
    String GetTempUserKey = "api/user/getTempUserKey";





    //用户提交购买订单
    String SubmitOrder = "shop/order/submitOrder";

    //商家发布商品
    String PublishGoods = "shop/goods/publishGoods";


    @POST("api/config/getCommonConfig")
    Flowable<BaseResponse<UserConfig>> getUserConfig();


    @POST("api/shortvideo/getRandomList")
    Flowable<BaseResponse<ArrayList<ShortVideo>>> getRandomList(@Body RequestBody body);

    @POST("api/shortvideo/getComments")
    Flowable<BaseResponse<ArrayList<Comment>>> getComments(@Body RequestBody body);


    //登录
    @POST("/api/user/login")
    Flowable<BaseResponse<UserRegist>> userLogin(@Body RequestBody body);


    //发布短视频
    @POST("/api/shortvideo/publish")
    Flowable<BaseResponse> publish(@Body RequestBody body);

    @POST("/api/user/modifyMood")
    Flowable<BaseResponse> modifyMood(@Body RequestBody body);

    //获取验证码
    @POST("/api/user/sendCode")
    Flowable<BaseResponse<CodeMsg>> getCode(@Body RequestBody phone);

    //用户注册
    @POST("/api/user/regist")
    Flowable<BaseResponse<UserRegist>> userRegist(@Body RequestBody body);

    //修改密码
    @POST("/api/user/changePwd")
    Flowable<BaseResponse<UserRegist>> changePwd(@Body RequestBody body);

    //用户配置
    @POST("/api/config/getCommonConfig")
    Flowable<BaseResponse<UserConfig>> getCommonConfig();

    @POST("/api/Config/getTempKeysForCos")
    Flowable<BaseResponse<QCloudData>> getTempKeysForCos(@Body FormBody body);



    @POST("/api/shortvideo/getUserInfo")
    Flowable<BaseResponse<UserInfo>> getShortUserInfo(@Body FormBody build);

    @POST("/api/User/getUserInfo")
    Flowable<BaseResponse<UserRegist>> getUserInfo(@Body FormBody build);



    @POST("/api/Anchor/getAttentAnchors")
    Flowable<BaseResponse<ArrayList<UserRegist>>> getAttentAnchors(@Body FormBody build);

    @POST("/api/User/getFans")
    Flowable<BaseResponse<ArrayList<UserRegist>>> getFans(@Body FormBody build);



    @POST("/api/Anchor/search")
    Flowable<BaseResponse<ArrayList<UserRegist>>> searchAnchor(@Body FormBody body);


    @POST("/api/User/bindPhone")
    Flowable<BaseResponse> bindPhone(@Body FormBody body);


    @POST("/api/user/qqlogin")
    Flowable<BaseResponse<UserRegist>> qqlogin(@Body FormBody body);


    @POST("/api/user/phoneLogin")
    Flowable<BaseResponse<UserRegist>> phoneLogin(@Body FormBody body);


    @GET("/api/qniu/getToken")
    Flowable<BaseResponse<String>> getUploadToken();

    @POST("/api/user/facebookLogin")
    Flowable<BaseResponse<UserRegist>> facebookLogin(@Body FormBody build);

    @POST("/api/user/googleLogin")
    Flowable<BaseResponse<UserRegist>> googleLogin(@Body FormBody build);

    @POST("/api/user/loginByEmail")
    Flowable<BaseResponse<UserRegist>> emailLogin(@Body FormBody build);

    @POST("/api/user/send")
    Flowable<BaseResponse> sendEmailCode(@Body FormBody body);

    @POST("/api/user/updateHeader")
    Flowable<BaseResponse<String>> updateUserHeader(@Body FormBody body);

    @POST("/api/user/modifyNickname")
    Flowable<BaseResponse<String>> modifyNickname(@Body FormBody body);
}
