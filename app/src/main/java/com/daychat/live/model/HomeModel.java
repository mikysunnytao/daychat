package com.daychat.live.model;

import com.daychat.live.contract.HomeContract;
import com.daychat.live.model.entity.AnchorHistory;
import com.daychat.live.model.entity.Banners;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.model.entity.BuyGuard;
import com.daychat.live.model.entity.CashOutHistory;
import com.daychat.live.model.entity.CheckAttend;
import com.daychat.live.model.entity.Comment;
import com.daychat.live.model.entity.GiftInfo;
import com.daychat.live.model.entity.Guardian;
import com.daychat.live.model.entity.GuardianInfo;
import com.daychat.live.model.entity.HomeAd;
import com.daychat.live.model.entity.HotLive;
import com.daychat.live.model.entity.Invite;
import com.daychat.live.model.entity.MatchList;
import com.daychat.live.model.entity.MomentDetail;
import com.daychat.live.model.entity.PersonalAnchorInfo;
import com.daychat.live.model.entity.ProfitLog;
import com.daychat.live.model.entity.QCloudData;
import com.daychat.live.model.entity.RoomManager;
import com.daychat.live.model.entity.ShortVideo;
import com.daychat.live.model.entity.Topic;
import com.daychat.live.model.entity.Trend;
import com.daychat.live.model.entity.UserInfo;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.net.RetrofitClient;


import java.util.ArrayList;

import io.reactivex.Flowable;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class HomeModel implements HomeContract.Model {

    @Override
    public Flowable<BaseResponse<ArrayList<ShortVideo>>> getRandomList(RequestBody body) {
        return RetrofitClient.getInstance().getApi().getRandomList(body);
    }

    @Override
    public Flowable<BaseResponse<ArrayList<Comment>>> getComments(RequestBody body) {
        return RetrofitClient.getInstance().getApi().getComments(body);
    }



    @Override
    public Flowable<BaseResponse<UserInfo>> getShortUserInfo(FormBody body) {
        return RetrofitClient.getInstance().getApi().getShortUserInfo(body);
    }

    @Override
    public Flowable<BaseResponse<UserRegist>> getUserInfo(FormBody body) {
        return RetrofitClient.getInstance().getApi().getUserInfo(body);
    }



    @Override
    public Flowable<BaseResponse<ArrayList<UserRegist>>> getAttentAnchors(FormBody body) {
        return RetrofitClient.getInstance().getApi().getAttentAnchors(body);
    }

    @Override
    public Flowable<BaseResponse<ArrayList<UserRegist>>> getFans(FormBody body) {
        return RetrofitClient.getInstance().getApi().getFans(body);
    }



    @Override
    public Flowable<BaseResponse<ArrayList<UserRegist>>> searchAnchor(FormBody body) {
        return RetrofitClient.getInstance().getApi().searchAnchor(body);
    }




    @Override
    public Flowable<BaseResponse<QCloudData>> getTempKeysForCos(FormBody body) {
        return RetrofitClient.getInstance().getApi().getTempKeysForCos(body);
    }


}
