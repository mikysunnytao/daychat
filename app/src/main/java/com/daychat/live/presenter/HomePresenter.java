package com.daychat.live.presenter;

import com.daychat.live.contract.HomeContract;
import com.daychat.live.model.entity.GiftInfo;
import com.daychat.live.model.entity.QCloudData;
import com.google.gson.internal.LinkedTreeMap;
import com.daychat.live.model.HomeModel;
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
import com.daychat.live.model.entity.Trend;
import com.daychat.live.model.entity.UserInfo;
import com.daychat.live.model.entity.UserRegist;
import com.daychat.live.net.RxScheduler;
import com.daychat.live.ui.act.LoginActivity;
import com.daychat.live.util.MyUserInstance;
import com.meiyinzb.nasinet.utils.AppManager;
import com.orhanobut.hawk.Hawk;


import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    private final HomeContract.Model model;


    public HomePresenter() {
        model = new HomeModel();
    }




    @Override
    public void getRandomList(String page,Integer type) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }

        //具体实现
        model.getRandomList(new FormBody.Builder().add("page", page)
                        .add("video_type", String.valueOf(type))
                        .add("size", "20").add("uid", MyUserInstance.getInstance().getUserinfo().getId()).add("token", MyUserInstance.getInstance().getUserinfo().getToken()).build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<ArrayList<ShortVideo>>>() {
                    @Override
                    public void accept(BaseResponse<ArrayList<ShortVideo>> bean) throws Exception {
                        mView.getRandomList(bean);
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
    public void getComments(String lastid, String videoid) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        FormBody formBody;
        if (!lastid.equals("")) {
            formBody = new FormBody.Builder().add("videoid", videoid).add("size", "20").add("lastid", lastid).add("uid", MyUserInstance.getInstance().getUserinfo().getId()).add("token", MyUserInstance.getInstance().getUserinfo().getToken()).build();
        } else {
            formBody = new FormBody.Builder().add("videoid", videoid).add("size", "20").add("uid", MyUserInstance.getInstance().getUserinfo().getId()).add("token", MyUserInstance.getInstance().getUserinfo().getToken()).build();
        }


        //具体实现
        model.getComments(formBody)
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<ArrayList<Comment>>>() {
                    @Override
                    public void accept(BaseResponse<ArrayList<Comment>> bean) throws Exception {
                        mView.getComments(bean);
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
    public void getShortUserInfo(String authorid) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        //具体实现
        model.getShortUserInfo(new FormBody.Builder().add("authorid", authorid).build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<UserInfo>>() {
                    @Override
                    public void accept(BaseResponse<UserInfo> bean) throws Exception {
                        mView.setShortUserInfo(bean.getData());
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
    public void getUserInfo() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        //具体实现
        model.getUserInfo(new FormBody.Builder().add("uid", MyUserInstance.getInstance().getUserinfo().getId())
                        .add("token", MyUserInstance.getInstance().getUserinfo().getToken())
                        .build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<UserRegist>>() {
                    @Override
                    public void accept(BaseResponse<UserRegist> bean) throws Exception {
                        mView.hideLoading();
                        if (bean.isSuccess()) {
                            mView.setUserInfo(bean.getData());

                        }

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
    public void getAttentAnchors(int page) {
        if (!isViewAttached()) {
            return;
        }
        //具体实现
        model.getAttentAnchors(new FormBody.Builder().add("page", page + "")
                        .add("uid", MyUserInstance.getInstance().getUserinfo().getId())
                        .add("token", MyUserInstance.getInstance().getUserinfo().getToken())
                        .add("size", "20").build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<ArrayList<UserRegist>>>() {
                    @Override
                    public void accept(BaseResponse<ArrayList<UserRegist>> bean) throws Exception {
                        if (bean.isSuccess()) {
                            mView.setAttentUser(bean.getData());
                        } else {
                            mView.showMgs(bean.getMsg());
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

    @Override
    public void getFans(int page) {
        if (!isViewAttached()) {
            return;
        }
        //具体实现
        model.getFans(new FormBody.Builder().add("page", page + "")
                        .add("uid", MyUserInstance.getInstance().getUserinfo().getId())
                        .add("token", MyUserInstance.getInstance().getUserinfo().getToken())
                        .add("size", "20").build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<ArrayList<UserRegist>>>() {
                    @Override
                    public void accept(BaseResponse<ArrayList<UserRegist>> bean) throws Exception {
                        if (bean.isSuccess()) {
                            mView.setAttentUser(bean.getData());
                        } else {
                            mView.showMgs(bean.getMsg());
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


    @Override
    public void searchAnchor(int page, String keyword) {
        if (!isViewAttached()) {
            return;
        }
        //具体实现
        model.searchAnchor(new FormBody.Builder().add("page", page + "")
                        .add("uid", MyUserInstance.getInstance().getUserinfo().getId())
                        .add("token", MyUserInstance.getInstance().getUserinfo().getToken())
                        .add("keyword", keyword)
                        .add("size", "20").build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<ArrayList<UserRegist>>>() {
                    @Override
                    public void accept(BaseResponse<ArrayList<UserRegist>> bean) throws Exception {
                        if (bean.isSuccess()) {
                            mView.setAttentUser(bean.getData());
                        } else {
                            mView.showMgs(bean.getMsg());
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


    @Override
    public void getTempKeysForCos() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        //具体实现

        model.getTempKeysForCos(new FormBody.Builder()
                        .add("platform", "2")
                        .add("uid", MyUserInstance.getInstance().getUserinfo().getId()).add("token", MyUserInstance.getInstance().getUserinfo().getToken()).build())
                .compose(RxScheduler.Flo_io_main())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<BaseResponse<QCloudData>>() {
                    @Override
                    public void accept(BaseResponse<QCloudData> bean) throws Exception {
                        mView.getTempKeysForCos(bean.getData());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.onError(throwable);

                    }
                });
    }


}
