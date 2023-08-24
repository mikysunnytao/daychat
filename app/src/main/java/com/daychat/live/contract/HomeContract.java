package com.daychat.live.contract;

import com.daychat.live.base.BaseView;
import com.daychat.live.model.entity.AnchorHistory;
import com.daychat.live.model.entity.BuyGuard;
import com.daychat.live.model.entity.CashOutHistory;
import com.daychat.live.model.entity.CheckAttend;
import com.daychat.live.model.entity.GiftInfo;
import com.daychat.live.model.entity.Guardian;
import com.daychat.live.model.entity.GuardianInfo;
import com.daychat.live.model.entity.Invite;
import com.daychat.live.model.entity.ProfitLog;
import com.daychat.live.model.entity.QCloudData;
import com.daychat.live.model.entity.RoomManager;
import com.daychat.live.model.entity.Banners;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.ChatGiftBean;
import com.daychat.live.model.entity.Comment;
import com.daychat.live.model.entity.HomeAd;
import com.daychat.live.model.entity.HotLive;
import com.daychat.live.model.entity.MatchList;
import com.daychat.live.model.entity.MomentDetail;
import com.daychat.live.model.entity.PersonalAnchorInfo;
import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.model.entity.Topic;
import com.daychat.live.model.entity.Trend;
import com.daychat.live.model.entity.UserInfo;
import com.daychat.live.model.entity.UserRegist;


import java.util.ArrayList;

import io.reactivex.Flowable;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;

public interface HomeContract {
    interface Model {

        Flowable<BaseResponse<ArrayList<ShortVideo>>> getRandomList(RequestBody body);

        Flowable<BaseResponse<ArrayList<Comment>>> getComments(RequestBody body);



        Flowable<BaseResponse<UserInfo>> getShortUserInfo(FormBody body);

        Flowable<BaseResponse<UserRegist>> getUserInfo(FormBody body);



        Flowable<BaseResponse<ArrayList<UserRegist>>> getAttentAnchors(FormBody body);

        Flowable<BaseResponse<ArrayList<UserRegist>>> getFans(FormBody body);


        Flowable<BaseResponse<ArrayList<UserRegist>>> searchAnchor(FormBody body);



        Flowable<BaseResponse<QCloudData>> getTempKeysForCos(FormBody body);


    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);


        default void getRandomList(BaseResponse<ArrayList<ShortVideo>> bean) {
        }

        default void getComments(BaseResponse<ArrayList<Comment>> bean) {
        }


        default void setShortUserInfo(UserInfo bean) {
        }

        default void setUserInfo(UserRegist bean) {
        }




        default void showMgs(String msg) {
        }



        default void setAttentUser(ArrayList<UserRegist> bean) {
        }



        default void getTempKeysForCos(QCloudData data) {

        }


    }

    interface Presenter {
        /**
         * 获取用户配置信息
         */


        void getRandomList(String page,Integer type);


        void getComments(String lastid, String videoid);

        void getShortUserInfo(String authorid);

        void getUserInfo();



        void getAttentAnchors(int page);

        void getFans(int page);


        void searchAnchor(int page, String keyword);


        void getTempKeysForCos();


    }
}
