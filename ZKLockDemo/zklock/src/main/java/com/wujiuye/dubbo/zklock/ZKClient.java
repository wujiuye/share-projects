package com.wujiuye.dubbo.zklock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZKClient implements Watcher {

    private ZooKeeper zooKeeper;

    public ZKClient(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, 500, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ZooKeeper getClient(){
        return zooKeeper;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType()== Event.EventType.None){
            System.out.println("连接成功了...........");
            return ;
        }
    }
}
