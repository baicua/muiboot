package com.muiboot.shiro.common.handler;

import com.muiboot.shiro.common.domain.ResponseBo;
import com.muiboot.shiro.common.util.LogUtil;
import com.muiboot.shiro.common.util.exec.ExecutorsUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final LogUtil log = LogUtil.getLoger(GlobalExceptionHandler.class);
	private ExecutorService exeService= ExecutorsUtil.getInstance().getMultilThreadExecutor();
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseBo handleException(Exception ex,HttpServletRequest req) {
		exeService.execute(new Runnable() {
			@Override
			public void run() {
				log.error("操作异常",ex,req);
			}
		});
		return ResponseBo.error("操作失败，请联系管理员！");
	}
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
