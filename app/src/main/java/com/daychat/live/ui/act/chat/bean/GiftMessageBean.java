package com.daychat.live.ui.act.chat.bean;

import com.daychat.live.model.entity.GiftInfo;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.qcloud.tuikit.tuichat.bean.message.TUIMessageBean;

public class GiftMessageBean extends TUIMessageBean {

    private GiftInfo giftInfo;

    public GiftInfo getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(GiftInfo giftInfo) {
        this.giftInfo = giftInfo;
    }

    @Override
    public String onGetDisplayString() {
        return null;
    }

    @Override
    public void onProcessMessage(V2TIMMessage v2TIMMessage) {

    }
}
