package com.muiboot.shiro.common.handler;

import com.muiboot.core.entity.ResponseBo;
import com.muiboot.core.exception.BusinessException;
import com.muiboot.rpc.ResponseDTO;
import com.muiboot.rpc.RpcException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>Description: 全局异常处理</p>
 *
 * @author jin
 * @version 1.0 2018/10/10
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseBo handleException(Exception ex,HttpServletRequest req) {
		log.error("操作异常",ex);
		return ResponseBo.error("操作失败，请联系管理员！");
	}
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseBody
	public ResponseBo handleRuntimeException(RuntimeException ex,HttpServletRequest req) {
        log.error("操作异常",ex);
		return ResponseBo.error("操作失败，请联系管理员！");
	}
	@ExceptionHandler(value = BusinessException.class)
	@ResponseBody
	public ResponseBo handleBusinessException(BusinessException ex, HttpServletRequest req) {
        log.error("操作异常",ex);
		return ResponseBo.error(ex.getMessage());
	}
	@ExceptionHandler(value = RpcException.class)
	@ResponseBody
	public ResponseDTO handleRpcException(RpcException ex, HttpServletRequest req) {
        log.error("操作异常",ex);
		return ResponseDTO.error(ex.getMessage());
	}
	@ExceptionHandler(value = AuthorizationException.class)
	@ResponseBody
	public ResponseBo handleAuthorizationException(AuthorizationException ex, HttpServletResponse res) throws IOException {
        log.error("暂无权限，请联系管理员！",ex);
	    res.sendError(HttpServletResponse.SC_FORBIDDEN,"暂无权限，请联系管理员！");
		return ResponseBo.error("暂无权限，请联系管理员！");
	}

	@ExceptionHandler(value = ExpiredSessionException.class)
	public String handleExpiredSessionException() {
		return "login";
	}
}
