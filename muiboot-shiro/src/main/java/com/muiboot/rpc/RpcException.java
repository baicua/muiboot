package com.muiboot.rpc;

/**
 * Created by 75631 on 2019/1/5.
 */
public class RpcException  extends Exception  {
    public RpcException(String message){
        super(message);
    }

    public RpcException(Throwable cause)
    {
        super(cause);
    }

    public RpcException(String message,Throwable cause)
    {
        super(message,cause);
    }
}
