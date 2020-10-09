package com.wujiuye.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

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
public class ServerMain {


    private static ServerSocket sServerSocket;
    private final static int port = 8088;
    private static SocketConnectPool connectPool = new SocketConnectPool();

    public static void main(String[] args) {
        try {
            sServerSocket = new ServerSocket(port);
            while (true){
                Socket client = sServerSocket.accept();
                connectPool.addClient(client);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(Thread.currentThread().getThreadGroup(),"监控线程"){
            @Override
            public void run() {
                try {
                    while (true) {
                        byte[] buff = new byte[1024];
                        int realLength = System.in.read(buff, 0, buff.length);
                        if (realLength > 0) {
                            String comd = new String(buff, 0,realLength,"UTF-8");
                            System.out.println("控制台输入命令："+comd);
                            if (comd.equals("quit")) {
                                sServerSocket.close();
                                connectPool.stopService();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
