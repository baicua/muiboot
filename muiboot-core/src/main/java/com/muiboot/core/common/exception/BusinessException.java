package com.muiboot.core.common.exception;

/**
 * <p>Description: 业务异常</p>
 *
 * @author jin
 * @version 1.0 2018/10/10
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BusinessException(String message){
		super(message);
	}
	
	public BusinessException(Throwable cause)
	{
		super(cause);
	}
	
	public BusinessException(String message,Throwable cause)
	{
		super(message,cause);
	}
}
