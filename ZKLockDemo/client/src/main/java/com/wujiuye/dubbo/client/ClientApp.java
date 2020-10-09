package com.wujiuye.dubbo.client;

import com.wujiuye.dubbo.interfaces.GoodsService;
import com.wujiuye.dubbo.interfaces.OrderService;
import com.wujiuye.dubbo.interfaces.UserService;
import com.wujiuye.dubbo.pojo.Goods;
import com.wujiuye.dubbo.pojo.Order;
import com.wujiuye.dubbo.pojo.User;
import com.wujiuye.dubbo.sub.BaseSubService;
import com.wujiuye.dubbo.sub.SubGoodsService;
import com.wujiuye.dubbo.sub.SubOrderService;
import com.wujiuye.dubbo.sub.SubUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ClientApp {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        BaseSubService<UserService> subService = new SubUserService();
//        UserService userService = subService.getService();
//        User user = new User("wujiuye","12345678780","123456");
//        userService.savaUser(user);
//        System.out.println(userService.getUser("wujiuye"));


        BaseSubService<GoodsService> goodsSubService = new SubGoodsService();
        GoodsService goodsService = goodsSubService.getService();
        BaseSubService<OrderService> orderSubService = new SubOrderService();
        OrderService orderService = orderSubService.getService();

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future> futures = new ArrayList<>();
        for(int i=0;i<10;i++){
            futures.add(executorService.submit(new Callable<Object>() {
                @Override
                public Object call() {
                    Goods goods = goodsService.queryGoods("nokia");
                    Order order = orderService.placeOrder(goods.getGoodsName(),1);
                    return order;
                }
            }));
        }
        executorService.shutdown();
        for (Future future:futures){
            try {
                Order order = (Order) future.get();
                if(order!=null) {
                    System.out.println("订单id：" + order.getOrderId() + ",剩余总数：" + order.getGoods().getCount());
                }
            }catch (Exception e){
                System.err.println(e.getMessage());
            }

        }
    }

}
