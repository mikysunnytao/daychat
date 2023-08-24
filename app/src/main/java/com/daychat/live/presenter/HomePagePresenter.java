package com.daychat.live.presenter;

import com.daychat.live.contract.HomeContract;
import com.daychat.live.contract.HomePageContract;
import com.daychat.live.model.HomeModel;

public class HomePagePresenter extends BasePresenter<HomePageContract.View> implements HomePageContract.Presenter {

    private final HomeContract.Model model;

    public HomePagePresenter() {
        model = new HomeModel();
    }




}
