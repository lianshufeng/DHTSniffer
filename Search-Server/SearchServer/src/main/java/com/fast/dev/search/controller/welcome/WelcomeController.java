package com.fast.dev.search.controller.welcome;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fast.dev.core.util.net.RequestUtil;
import com.fast.dev.search.controller.SuperController;

/**
 * 首页控制器
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年2月5日
 *
 */
@Controller
public class WelcomeController extends SuperController {

	/**
	 * 首页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(path = { "/", "" })
	public void space(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String root = RequestUtil.getUrl(request);
		response.sendRedirect(root + "/index.html");
	}

	@RequestMapping(path = { "index.html" })
	public ModelAndView index(HttpServletRequest request) {
		return new ModelAndView("index.htl");
	}

}
