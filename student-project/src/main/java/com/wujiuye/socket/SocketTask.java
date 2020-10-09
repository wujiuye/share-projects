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
public class SocketTask implements Runnable {

    private Socket socket;
    private String myId;
    private OnClientQuit onClientQuit;

    public SocketTask(String id, Socket client, OnClientQuit onClientQuit) {
        this.socket = client;
        this.myId = id;
        this.onClientQuit = onClientQuit;
    }

    @Override
    public void run() {
        try {
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String msg = inputStream.readLine();
                switch (msg) {
                    case "quit":
                        inputStream.close();
                        outputStream.close();
                        this.onClientQuit.onClientQuit(this.myId);
                        return;
                    default:
                        String response = "接收到客户端["+myId+"]发来消息["+msg+"]，响应消息：{\"msg\":\""+msg+"\"}\n";//以行读取加\n
                        System.out.println(response);
                        outputStream.write(response);
                        outputStream.flush();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
