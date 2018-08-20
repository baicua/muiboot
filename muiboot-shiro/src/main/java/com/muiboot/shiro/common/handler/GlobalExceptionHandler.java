package com.muiboot.shiro.common.handler;

import com.muiboot.shiro.common.domain.ResponseBo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler(value = AuthorizationException.class)
	@ResponseBody
	public ResponseBo handleAuthorizationException() {
		return ResponseBo.error("暂无权限，请联系管理员！");
	}

	@ExceptionHandler(value = ExpiredSessionException.class)
	public String handleExpiredSessionException() {
		return "login";
	}
}
