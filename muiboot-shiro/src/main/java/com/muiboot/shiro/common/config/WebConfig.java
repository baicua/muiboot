package com.muiboot.shiro.common.config;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.muiboot.shiro.common.xss.XssFilter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
/**
* <p>Description: XSS拦截</p>
* @version 1.0 2018/10/12
* @author jin
*/
@Configuration
public class WebConfig {

	/**
	 * XssFilter Bean
	 */
	@Bean
	public FilterRegistrationBean xssFilterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new XssFilter());
		filterRegistrationBean.setOrder(1);
		filterRegistrationBean.setEnabled(true);
		filterRegistrationBean.addUrlPatterns("/*");
		Map<String, String> initParameters = new HashMap<>();
		initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*,/toolkit/*");
		initParameters.put("isIncludeRichText", "true");
		filterRegistrationBean.setInitParameters(initParameters);
		return filterRegistrationBean;
	}

	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper;
	}

}