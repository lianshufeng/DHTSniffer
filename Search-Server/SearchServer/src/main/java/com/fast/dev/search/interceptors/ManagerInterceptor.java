package com.fast.dev.search.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fast.dev.core.interceptor.UrlInterceptor;
import com.fast.dev.core.util.code.JsonUtil;

@Component
public class ManagerInterceptor implements UrlInterceptor {

	private String token;

	@Autowired
	@SuppressWarnings("unchecked")
	private void init() throws Exception {
		Map<String, Object> m = JsonUtil.loadToObject("manager.json", Map.class);
		this.token = String.valueOf(m.get("token"));
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return this.token.equals(request.getParameter("token"));
	}

	@Override
	public String[] addPathPatterns() {
		return new String[] { "/manager/**/*.json" };
	}

	@Override
	public String[] excludePathPatterns() {
		return null;
	}

	@Override
	public int level() {
		return -1;
	}

}
