package com.wujiuye.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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
public class SocketConnectPool implements OnClientQuit{

    private ConcurrentHashMap<String, Socket> clientArray;
    private AtomicInteger clientNumber;
    private ExecutorService executorService;

    public SocketConnectPool(){
        clientArray = new ConcurrentHashMap<>();
        executorService = Executors.newCachedThreadPool();
        clientNumber = new AtomicInteger(0);
    }

    public void stopService(){
        this.executorService.shutdownNow();
        this.clientArray.clear();
        this.clientArray = null;
    }

    public void addClient(Socket socket){
        String sid = "client"+clientNumber.incrementAndGet();
        System.out.println("客户端连接："+sid);
        this.clientArray.put(sid,socket);
        SocketTask socketTask = new SocketTask(sid,socket,this);
        //提交一个读任务
        executorService.submit(socketTask);
    }

    public void removeClient(String id){
        try {
            this.clientArray.get(id).close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            this.clientArray.remove(id);
        }
    }


    @Override
    public void onClientQuit(String id) {
        removeClient(id);
        System.out.println("客户端断开连接："+id);
    }
}
