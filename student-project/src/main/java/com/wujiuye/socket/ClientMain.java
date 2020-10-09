package com.wujiuye.socket;

import java.io.*;
import java.net.Socket;

/**
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * <p>
 * 微信公众号id：code_skill
 * QQ邮箱：419611821@qq.com
 * 微信号：www_wujiuye_com
 * <p>
 * ======================^^^^^^^==============^^^^^^^============
 *
 * @ 作者       |   吴就业 www.wujiuye.com
 * ======================^^^^^^^==============^^^^^^^============
 * @ 创建日期      |   Created in 2018年12月22日
 * ======================^^^^^^^==============^^^^^^^============
 * @ 所属项目   |   lock
 * ======================^^^^^^^==============^^^^^^^============
 * @ 类功能描述    |
 * ======================^^^^^^^==============^^^^^^^============
 * @ 版本      |   ${version}
 * ======================^^^^^^^==============^^^^^^^============
 */
public class ClientMain {

    private Socket client;
    private int port = 8088;
    private static volatile boolean isQuit = false;

    public static void main(String[] args) {
        ClientMain client = new ClientMain();
        try {
            client.client = new Socket("127.0.0.1", client.port);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.client.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(client.client.getInputStream()));
            //客户端写线程
            new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isQuit) {
                            byte[] buff = new byte[1024];
                            int realLength = System.in.read(buff, 0, buff.length);
                            if (realLength > 0) {
                                String comd = new String(buff, 0, realLength, "UTF-8");
                                System.out.println("控制台输入命令：" + comd);
                                if (comd.equals("quit\n")) {
                                    System.out.println("bye!!!");
                                    isQuit = true;
                                    client.client.close();
                                    in.close();
                                    out.close();
                                    return;
                                }
                                out.write(comd);
                                out.flush();//将缓存区数据写到服务端
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            //客户端读线程
            new Thread() {
                @Override
                public void run() {
                    while (!isQuit) {
                        try {
                            String msg = in.readLine();
                            System.out.println("收到服务端确认====>" + msg);
                        } catch (IOException e) {
                            if(client.client.isClosed()){
                                return;
                            }
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
