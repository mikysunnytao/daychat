package com.daychat.live.ui.act.chat.bean;

import com.daychat.live.model.entity.GiftInfo;

public class ChatMessageBean {

    private int header;

    private String message;

    //0普通消息，1：图片消息 2：视频消息 3：礼物消息
    private String messageType;

    private String time;

    private ChatMessageBean sender;

    private ChatMessageBean receiver;

    private GiftInfo giftInfo;

    private Boolean isSelf;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public ChatMessageBean getSender() {
        return sender;
    }

    public void setSender(ChatMessageBean sender) {
        this.sender = sender;
    }

    public ChatMessageBean getReceiver() {
        return receiver;
    }

    public void setReceiver(ChatMessageBean receiver) {
        this.receiver = receiver;
    }

    public GiftInfo getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(GiftInfo giftInfo) {
        this.giftInfo = giftInfo;
    }

    public Boolean getSelf() {
        return isSelf;
    }

    public void setSelf(Boolean self) {
        isSelf = self;
    }
}
