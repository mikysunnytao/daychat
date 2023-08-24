package com.daychat.live.model;

import com.daychat.live.contract.UploadContract;
import com.daychat.live.model.entity.BaseResponse;
import com.daychat.live.net.RetrofitClient;

import io.reactivex.Flowable;
import okhttp3.RequestBody;

public class UploadModel implements UploadContract.Model {



    @Override
    public Flowable<BaseResponse> publish(RequestBody body) {
        return RetrofitClient.getInstance().getApi().publish(body);
    }
}
