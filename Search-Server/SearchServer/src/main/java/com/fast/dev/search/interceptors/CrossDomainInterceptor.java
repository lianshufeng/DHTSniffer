package com.fast.dev.search.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.fast.dev.core.interceptor.UrlInterceptor;

/**
 * 增加跨域的支持
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月16日
 *
 */
@Component
public class CrossDomainInterceptor implements UrlInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");
		// response.setHeader("Access-Control-Allow-Headers",
		// "Content-Type,Content-Length, Authorization, Accept,X-Requested-With");
		// response.setHeader("Access-Control-Allow-Methods",
		// "PUT,POST,GET,DELETE,OPTIONS");
		// String method = request.getMethod();
		// if (method.equals("OPTIONS")) {
		// response.setStatus(200);
		// return false;
		// }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	@Override
	public String[] addPathPatterns() {
		return new String[] { "/**/*.json" };
	}

	@Override
	public String[] excludePathPatterns() {
		return null;
	}

	@Override
	public int level() {
		return 0;
	}

}
