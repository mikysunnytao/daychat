package com.daychat.live.contract;

import com.daychat.live.base.BaseView;
import com.daychat.live.model.entity.BaseResponse;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

public interface UploadContract {
    interface Model {
        Flowable<BaseResponse> publish(RequestBody body);
    }

    interface View extends BaseView {
        @Override
        void showLoading();

        @Override
        void hideLoading();

        @Override
        void onError(Throwable throwable);

        default void publish(BaseResponse bean){}
    }

    interface Presenter {
        /**
         * 获取用户配置信息
         *
         */
        void publish(String title,String thumb_url,String play_url,String topic);
    }
}
