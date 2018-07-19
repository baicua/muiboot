package com.baicua.shiro.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.baicua.shiro.common.annotation.Log;
import com.baicua.shiro.common.util.HttpContextUtils;
import com.baicua.shiro.common.util.IPUtils;
import com.baicua.shiro.system.domain.SysLog;
import com.baicua.shiro.system.domain.User;
import com.baicua.shiro.system.service.LogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.baicua.shiro.common.util.AddressUtils;

/**
 * AOP 记录用户操作日志
 * @link https://mrbird.cc/Spring-Boot-AOP%20log.html
 * @author MrBird
 */
@Aspect
@Component
public class LogAspect {

	@Autowired
	private LogService logService;

	@Autowired
	ObjectMapper mapper;

	@Pointcut("@annotation(com.baicua.shiro.common.annotation.Log)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws JsonProcessingException {
		Object result = null;
		long beginTime = System.currentTimeMillis();
		try {
			// 执行方法
			result = point.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 保持日志
		saveLog(point, time);
		return result;
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time) throws JsonProcessingException {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		SysLog log = new SysLog();
		Log logAnnotation = method.getAnnotation(Log.class);
		if (logAnnotation != null) {
			// 注解上的描述
			log.setOperation(logAnnotation.value());
		}

		// 请求的类名
		String className = joinPoint.getTarget().getClass().getName();
		// 请求的方法名
		String methodName = signature.getName();
		log.setMethod(className + "." + methodName + "()");
		// 请求的方法参数值
		Object[] args = joinPoint.getArgs();
		// 请求的方法参数名称
		LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
		String[] paramNames = u.getParameterNames(method);
		if (args != null && paramNames != null) {
			StringBuilder params = new StringBuilder();
			for (int i = 0; i < args.length; i++) {
				params.append("  ").append(paramNames[i]).append(": ").append(this.mapper.writeValueAsString(args[i]));
			}
			log.setParams(params.toString());
		}
		// 获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		// 设置IP地址
		log.setIp(IPUtils.getIpAddr(request));
		log.setUsername(user.getUsername());
		log.setTime(time);
		log.setCreateTime(new Date());
		log.setLocation(AddressUtils.getRealAddressByIP(log.getIp(), mapper));
		// 保存系统日志
		this.logService.save(log);
	}
}
