package com.daychat.live.contract;

import com.daychat.live.base.BaseView;

public interface HomePageContract {
    interface Model {

    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);



    }

    interface Presenter {

    }
}
