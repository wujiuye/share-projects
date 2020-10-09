package com.wujiuye.insertpile.ipevent;

import com.wujiuye.insertpile.ipevent.event.BusinessCallLinkEvent;
import com.wujiuye.insertpile.ipevent.event.FuncRuntimeEvent;
import com.wujiuye.insertpile.logs.IBusinessCallLinkLog;
import com.wujiuye.insertpile.logs.IFuncRuntimeLog;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通过它来管理注册log实现类，进行相应的事件回调
 *
 * @author wjy
 */
public final class InsertPileManager implements Runnable {


    /**
     * 基于事件驱动，插桩的代码会调用相应的xxxEvent实例的方法，
     * xxxEvent实例会发送一个事件到InsertPileManager，
     * 由InsertPileManager将事件分发到监听器去执行
     */
    public static class EventType {
        public static final int CALL_LINK_EVENT = 1;
        public static final int RUN_TIME_EVENT = 2;
    }

    public static InsertPileManager getInstance() {
        return InsertPileManagerHolder.manager;
    }

    private static final class InsertPileManagerHolder {
        private static final InsertPileManager manager = new InsertPileManager();
    }

    private Vector<IBusinessCallLinkLog> businessCallLinkLogVector;
    private Vector<IFuncRuntimeLog> funcRuntimeLogVector;

    private ExecutorService executorService;
    private final static int THREAD_COUNT = 20;
    private final static String THREAD_NAME = "wjy-insert-pile-logsava-thread-";
    private final static AtomicInteger atomicInteger = new AtomicInteger(0);

    private InsertPileManager() {
        businessCallLinkLogVector = new Vector<>();
        funcRuntimeLogVector = new Vector<>();
        executorService = new ThreadPoolExecutor(THREAD_COUNT,
                THREAD_COUNT,
                60, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(200),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, THREAD_NAME + atomicInteger.getAndIncrement());
                    }
                }, new RejectedExecutionHandler() {

            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //拒绝策略
            }
        });
    }

    public void addBusinessCallLinkLog(IBusinessCallLinkLog businessCallLinkLog) {
        this.businessCallLinkLogVector.add(businessCallLinkLog);
    }

    public void addFuncRuntimeLog(IFuncRuntimeLog funcRuntimeLog) {
        this.funcRuntimeLogVector.add(funcRuntimeLog);
    }

    /**
     * 事件队列,ConcurrentLinkedQueue线程安全，保证在多线程环境下也能使用
     */
    private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>();

    /**
     * 将事件发送事件队列就行了，具体的处理将由线程池去分配，
     * 这样做的目的是不耗时，不阻塞业务代码的执行
     *
     * @param event
     */
    public void notifyEvent(Event event) {
        eventQueue.offer(event);
        executorService.submit(this);
    }


    @Override
    public void run() {
        for (; ; ) {
            if (eventQueue.peek() == null)
                break;
            Event event = eventQueue.poll();
            switch (event.getEventType()) {
                case EventType.CALL_LINK_EVENT:
                    handleBusinessCallLinkEvent(event);
                    break;
                case EventType.RUN_TIME_EVENT:
                    handleFuncRuntimeEvent(event);
                    break;
            }
        }
    }


    /**
     * 处理调用链事件
     * @param event
     */
    private void handleBusinessCallLinkEvent(Event event) {
        for (int i = 0; i < businessCallLinkLogVector.size(); i++) {
            if (BusinessCallLinkEvent.Type.getTypeWithCode(event.getType()) == BusinessCallLinkEvent.Type.Before) {
                businessCallLinkLogVector.get(i)
                        .savaBusinessFuncCallLog(
                                event.getSessionId(),
                                event.getEventParam().getClassName(),
                                event.getEventParam().getFuncName(),
                                event.getEventParam().getFuncAgrs()
                        );
            } else if (BusinessCallLinkEvent.Type.getTypeWithCode(event.getType()) == BusinessCallLinkEvent.Type.ERROR) {
                businessCallLinkLogVector.get(i)
                        .savaBusinessFuncCallErrorLog(
                                event.getSessionId(),
                                event.getEventParam().getClassName(),
                                event.getEventParam().getFuncName(),
                                event.getEventParam().getThrowable()
                        );
            }
        }
    }

    /**
     * 处理方法运行事件事件
     * @param event
     */
    private void handleFuncRuntimeEvent(Event event) {
        for (int i = 0; i < funcRuntimeLogVector.size(); i++) {
            if (FuncRuntimeEvent.Type.getTypeWithCode(event.getType()) == FuncRuntimeEvent.Type.Before) {
                funcRuntimeLogVector.get(i)
                        .savaFuncStartRuntimeLog(
                                event.getSessionId(),
                                event.getEventParam().getClassName(),
                                event.getEventParam().getFuncName(),
                                event.getEventParam().getStartTime()
                        );
            } else if (FuncRuntimeEvent.Type.getTypeWithCode(event.getType()) == FuncRuntimeEvent.Type.After) {
                funcRuntimeLogVector.get(i)
                        .savaFuncEndRuntimeLog(
                                event.getSessionId(),
                                event.getEventParam().getClassName(),
                                event.getEventParam().getFuncName(),
                                event.getEventParam().getEndTime()
                        );
            }
        }
    }
}
