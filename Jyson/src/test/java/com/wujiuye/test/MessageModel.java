package com.wujiuye.test;

import com.wujiuye.jyson.JCName;
import com.wujiuye.jyson.JFName;

import java.util.Date;

@JCName(value = "data",type = 1)//对应json字符串中的data字段
public class MessageModel {

    /**
     * 消息发送者的头像
     */
    @JFName("favicon")//对应json字符串中的favicon字段
    private String  senderUserFavicon;
    /**
     * 消息发送者的昵称
     */
    @JFName("nickname")//对应json字符串中的nickname字段
    private String  senderUserNickName;
    /**
     * 消息发送时间
     */
    @JFName("sendtime")//对应json字符串中的sendtime字段
    private Date senderMsgTime;
    /**
     * 消息内容
     */
    @JFName("message")//对应json字符串中的message字段
    private String  senderMsgContext;

    public String getSenderUserFavicon() {
        return senderUserFavicon;
    }
    public void setSenderUserFavicon(String senderUserFavicon) {
        this.senderUserFavicon = senderUserFavicon;
    }
    public String getSenderUserNickName() {
        return senderUserNickName;
    }
    public void setSenderUserNickName(String senderUserNickName) {
        this.senderUserNickName = senderUserNickName;
    }
    public Date getSenderMsgTime() {
        return senderMsgTime;
    }
    public void setSenderMsgTime(Date senderMsgTime) {
        this.senderMsgTime = senderMsgTime;
    }
    public String getSenderMsgContext() {
        return senderMsgContext;
    }
    public void setSenderMsgContext(String senderMsgContext) {
        this.senderMsgContext = senderMsgContext;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "senderUserFavicon='" + senderUserFavicon + '\'' +
                ", senderUserNickName='" + senderUserNickName + '\'' +
                ", senderMsgTime=" + senderMsgTime +
                ", senderMsgContext='" + senderMsgContext + '\'' +
                '}';
    }
}
