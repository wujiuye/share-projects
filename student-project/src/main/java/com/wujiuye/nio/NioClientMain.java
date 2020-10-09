package com.wujiuye.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

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
public class NioClientMain{

    public static void main(String[] args) {
       NioClient nioClient = new NioClient("127.0.0.1",8088);
       new Thread(nioClient,"NioClient-MainThread").start();
        try {
            byte[] cmd = new byte[100];
            while (true) {
                int realRead = System.in.read(cmd, 0, cmd.length);
                String cmdStr = new String(cmd, 0, realRead, "UTF-8");
                if(cmdStr.contains("\\q")){
                    System.out.println("bye...");
                    nioClient.closeConnect();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
