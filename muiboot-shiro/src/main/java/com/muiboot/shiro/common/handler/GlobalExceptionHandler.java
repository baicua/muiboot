package com.muiboot.shiro.common.handler;

import com.muiboot.core.entity.ResponseBo;
import com.muiboot.core.exception.BusinessException;
import com.muiboot.rpc.ResponseDTO;
import com.muiboot.rpc.RpcException;
import com.muiboot.shiro.common.util.LogUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;
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
	private static final LogUtil log = LogUtil.getLoger(GlobalExceptionHandler.class);
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseBo handleException(Exception ex,HttpServletRequest req) {
		String url=req.getRequestURL().toString();
		log.error("操作异常",ex,url);
		return ResponseBo.error("操作失败，请联系管理员！");
	}
	@ExceptionHandler(value = RuntimeException.class)
	@ResponseBody
	public ResponseBo handleRuntimeException(RuntimeException ex,HttpServletRequest req) {
		String url=req.getRequestURL().toString();
		log.error("操作异常",ex,url);
		return ResponseBo.error("操作失败，请联系管理员！");
	}
	@ExceptionHandler(value = BusinessException.class)
	@ResponseBody
	public ResponseBo handleBusinessException(BusinessException ex, HttpServletRequest req) {
		String url=req.getRequestURL().toString();
		log.error("操作异常",ex,url);
		return ResponseBo.error(ex.getMessage());
	}
	@ExceptionHandler(value = RpcException.class)
	@ResponseBody
	public ResponseDTO handleRpcException(RpcException ex, HttpServletRequest req) {
		String url=req.getRequestURL().toString();
		log.error("操作异常",ex,url);
		return ResponseDTO.error(ex.getMessage());
	}
	@ExceptionHandler(value = AuthorizationException.class)
	@ResponseBody
	public ResponseBo handleAuthorizationException(AuthorizationException ex, HttpServletResponse res) throws IOException {
		res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"暂无权限，请联系管理员！");
		return ResponseBo.error("暂无权限，请联系管理员！");
	}

	@ExceptionHandler(value = ExpiredSessionException.class)
	public String handleExpiredSessionException() {
		return "login";
	}
}
