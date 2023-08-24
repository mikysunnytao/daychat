package com.tencent.qcloud.tuikit.tuichat.bean;

import com.tencent.imsdk.v2.V2TIMMessage;

public class RecentChatBean {

    private boolean isShowMenu;

    private V2TIMMessage message;

    public boolean isShowMenu() {
        return isShowMenu;
    }

    public void setShowMenu(boolean showMenu) {
        isShowMenu = showMenu;
    }

    public V2TIMMessage getMessage() {
        return message;
    }

    public void setMessage(V2TIMMessage message) {
        this.message = message;
    }
}
