package com.wujiuye.dubbo.zklock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分布式锁
 *
 * @author wjy
 */
public class ZKLock implements Watcher {

    private static final String LOCK_ZNODE = "/zklock";
    private static final String ZK_LOCK = "/wujiuye-zklock-";

    private ZKClient zkClient;
    private String zklockZnode;
    private String lastZnode = null;
    private Thread holdThread;

    public ZKLock(ZKClient zkClient) {
        this.zkClient = zkClient;
        //判断创建根节点
        createZKLockZnode();
    }

    /**
     * 创建根节点，如果当前不存在
     */
    private void createZKLockZnode() {
        try {
            Stat stat = zkClient.getClient().exists(LOCK_ZNODE, null);
            if (stat != null) {
                return;
            }
            zkClient.getClient().create(LOCK_ZNODE, "zklock".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);//持久节点
        } catch (KeeperException e) {
            if (e.code() == KeeperException.Code.NODEEXISTS)
                return;
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                System.out.println("节点：" + watchedEvent.getPath() + "，事件：" + watchedEvent.getType());
                if (watchedEvent.getPath().equals(LOCK_ZNODE + "/" + lastZnode)) {
                    System.out.println("监听的上一个节点删除了：" + watchedEvent.getPath());
                    synchronized (this){
                        //唤醒lock方法
                        this.notifyAll();
                    }
                }
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    private boolean isFistZNode() {
        try {
            //获取到的节点并不是顺序的，需要查找出节点序列号最小的那个
            List<String> znodeList = zkClient.getClient().getChildren(LOCK_ZNODE, null);
            if (znodeList == null || znodeList.size() == 0)
                return false;
            Collections.sort(znodeList);
            if (zklockZnode.equals(LOCK_ZNODE + "/" + znodeList.get(0))) {
                System.out.println("我是第一个节点........." + zklockZnode);
                return true;
            } else {
                lastZnode = null;
                for (int i = 0; i < znodeList.size(); i++) {
                    if ((LOCK_ZNODE + "/" + znodeList.get(i)).equals(zklockZnode))
                        break;
                    lastZnode = znodeList.get(i);
                }
                if (lastZnode != null) {
                    System.out.println("当前节点：【" + zklockZnode + "】的上一个节点是：【" + lastZnode + "】");
                    //给上一个节点添加事件监听
                    //java.lang.IllegalArgumentException: Path must start with / character 要以"/"开头
                    //有可能这个时候上一个节点已经删除了
                    for (; ; ) {
                        try {
                            zkClient.getClient().getData(LOCK_ZNODE + "/" + lastZnode, this, null);
                            break;
                        } catch (KeeperException e) {
                            if (e.code() == KeeperException.Code.NONODE) {
                                return true;//我是第一个节点
                            }
                        }
                    }
                }
                return false;
            }
        } catch (KeeperException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void lock() {
        //可重入
        if (Thread.currentThread() == holdThread) {
            return;
        }
        holdThread = Thread.currentThread();
        //创建锁节点
        try {
            zklockZnode = zkClient.getClient().create(LOCK_ZNODE + ZK_LOCK,
                    "zklock".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("成功创建节点：" + zklockZnode);
            if (isFistZNode()) {
                System.out.println("===============> 获取到锁：" + zklockZnode);
                return;
            }
            this.wait();
            System.out.println("===============> 获取到锁：" + zklockZnode);
            return;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void unlock() {
        if (zklockZnode == null)
            return;
        try {
            zkClient.getClient().delete(zklockZnode, -1);
            System.out.println("===============> 释放锁：" + zklockZnode);
            zklockZnode = null;
            holdThread = null;
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            System.out.println("释放锁异常：" + e.getMessage());
            if (e.code() == KeeperException.Code.NONODE)
                return;
            e.printStackTrace();
        }
    }

}
