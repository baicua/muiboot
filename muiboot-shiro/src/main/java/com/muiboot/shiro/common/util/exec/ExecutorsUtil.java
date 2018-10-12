package com.muiboot.shiro.common.util.exec;

import java.util.concurrent.*;

/**
* <p>Description: 线程池处理</p>
* @version 1.0 2018/10/12
* @author jin
*/
public class ExecutorsUtil {
    private final static int MAX_MULTIL_POOL_SIZE = 5;
    private final static int MAX_SCHEDULED_POOL_SIZE = 10;
    private final static int QUEUE_SIZE=10;
    private   ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();//单线程
    private   ExecutorService multilThreadExecutor = new ThreadPoolExecutor(5, MAX_MULTIL_POOL_SIZE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());//多线程
    private   ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(MAX_SCHEDULED_POOL_SIZE);//任务调度线程池
    private   ExecutorService singleFixedExecutor =new ThreadPoolExecutor(1, 1,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(QUEUE_SIZE));//单线程
    private static ExecutorsUtil instance;

    public static ExecutorsUtil getInstance(){
        if (instance==null){
            synchronized (ExecutorsUtil.class){
                if (instance==null){
                    instance=new ExecutorsUtil();
                }
            }
        }
        return instance;
    }
    public void execute(Runnable command){
        singleThreadExecutor.execute(command);
    }

    public ExecutorService getSingleThreadExecutor() {
        return singleThreadExecutor;
    }

    public ExecutorService getMultilThreadExecutor() {
        return multilThreadExecutor;
    }

    public ScheduledExecutorService getScheduledThreadPool() {
        return scheduledThreadPool;
    }

}
