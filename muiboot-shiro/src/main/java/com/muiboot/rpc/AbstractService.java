package com.muiboot.rpc;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 75631 on 2019/1/5.
 */
public abstract class AbstractService {
    protected final static AtomicInteger threads=new AtomicInteger(0);
    static ThreadLocal<Integer> threads_size = new ThreadLocal();
    private final static int LIMIT=500;
    protected final static void increment() throws RpcException{
        threads_size.set(threads.incrementAndGet());
        if(threads_size.get()>LIMIT){
            throw new RpcException("请求频繁，请稍后再试");
        }
    }
    protected final static void decrement(){
        threads.decrementAndGet();
        threads_size.remove();
    }
}
