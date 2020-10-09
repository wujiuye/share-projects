# Jyson
json解析工具，依赖org.json。这是一个将json解析为java对象的工具。旧版本于2016年上传，但由于当时以android链接库的形式提交的，所以删了。


###解析对象

#####编写json对应的java类
#######注意@JCName注解不能少，@JCName的value对应json字符串中的字段，type=0解析的结果返回为一个对象，type=1解析的结果返回为一个列表。

######字段不加@JFName注解那么就需要字段名与json中的字段名相同。
```
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

```
#####使用方法
```
/**
     * @JCName(value = "data")
     */
    @Test
    public void testObject(){
        String jsonStr = "{\"data\":{\"favicon\":\"http://www.wujiuye.com/...\",\"nickname\":\"wujiuye\",\"sendtime\":\"2018-09-29 12:55:14\",\"message\":\"你好！\"}}";
        MessageModel1 msgModel1 = (MessageModel1) new Jyson().parseJson(jsonStr,MessageModel1.class);
        System.out.print(msgModel1.toString());
    }
```

###解析数组
```
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

```
#####使用方法

```
 /**
     * @JCName(value = "data",type = 1)
     */
    @Test
    public void testArray(){
        String jsonStr = "{\"data\":[{\"favicon\":\"http://www.wujiuye.com/...\",\"nickname\":\"wujiuye\",\"sendtime\":\"2018-09-29 12:55:14\",\"message\":\"你好！\"}]}";
        List<MessageModel> msgModel = (List<MessageModel>) new Jyson().parseJson(jsonStr,MessageModel.class);
        System.out.print(msgModel.toString());
    }
```


###版本介绍

| 版本号 | 改进的地方 | 日期 |
|-----|-----|------|
| 1.0-SNAPSHOT | ------   | --/--/-- |
| 2.0-SNAPSHOT | 支持数组的解析   | 2018/09/28 |


###作者

>关注我
微信公众号：全栈攻城狮之道
欢迎关注我，一起学习，一起进步!
