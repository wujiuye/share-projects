package com.wujiuye.test;

import com.wujiuye.jyson.JCName;
import com.wujiuye.jyson.JFName;

import java.util.Date;

@JCName(value = "data")//对应json字符串中的data字段
public class MessageModel1 {

    /**
     * 消息发送者的头像
     */
    private String  favicon;
    /**
     * 消息发送者的昵称
     */
    private String  nickname;
    /**
     * 消息发送时间
     */
    private Date sendtime;
    /**
     * 消息内容
     */
    private String  message;

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageModel1{" +
                "favicon='" + favicon + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sendtime=" + sendtime +
                ", message='" + message + '\'' +
                '}';
    }
}
