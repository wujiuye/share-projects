package com.wujiuye.test;

import com.wujiuye.jyson.Jyson;
import org.junit.Test;

import java.util.List;

public class JysonTest {


    /**
     * @JCName(value = "data")
     */
    @Test
    public void testObject(){
        String jsonStr = "{\"data\":{\"favicon\":\"http://www.wujiuye.com/...\",\"nickname\":\"wujiuye\",\"sendtime\":\"2018-09-29 12:55:14\",\"message\":\"你好！\"}}";
        MessageModel1 msgModel1 = (MessageModel1) new Jyson().parseJson(jsonStr,MessageModel1.class);
        System.out.print(msgModel1.toString());
    }

    /**
     * @JCName(value = "data",type = 1)
     */
    @Test
    public void testArray(){
        String jsonStr = "{\"data\":[{\"favicon\":\"http://www.wujiuye.com/...\",\"nickname\":\"wujiuye\",\"sendtime\":\"2018-09-29 12:55:14\",\"message\":\"你好！\"}]}";
        List<MessageModel> msgModel = (List<MessageModel>) new Jyson().parseJson(jsonStr,MessageModel.class);
        System.out.print(msgModel.toString());
    }

}
