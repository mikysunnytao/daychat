package com.daychat.live.ui.uiinterfae;

import com.daychat.live.model.entity.GiftInfo;


import java.util.ArrayList;

public interface ShowGift{
    void show(GiftInfo data);
    void setGift(ArrayList<GiftInfo> giftList);
}