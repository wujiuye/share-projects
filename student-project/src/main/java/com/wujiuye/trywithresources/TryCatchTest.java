package com.wujiuye.trywithresources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author wujiuye
 * @version 1.0 on 2019/8/25 {描述：
 * javac TryCatchTest.java
 * javap -c -s -l TryCatchTest | javap -c TryCatchTest
 * -l 输出行及局部变量表。
 * -s 输出内部类型签名
 * }
 */
public class TryCatchTest {

    public String tryOpenFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            return br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("hhhhhh");
        }
        return null;
    }

    public static void main(String[] args) {
        new TryCatchTest().tryOpenFile("/Users/wjy/Des/words");
    }

}
