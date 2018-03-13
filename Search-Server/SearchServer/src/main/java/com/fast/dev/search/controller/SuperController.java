package com.fast.dev.search.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * boss系统默认页面
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年2月5日
 *
 */
@RequestMapping("/")
public abstract class SuperController {

	/**
	 * 构建视图模型
	 * 
	 * @param view
	 * @return
	 */
	protected static final ModelAndView modelAndView(String viewName) {
		String view = "pages/" + viewName + ".htl";
		ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.addObject("viewName", viewName);
		return modelAndView;
	}

	/**
	 * 功能主页
	 * 
	 * @return
	 */
	@RequestMapping(path = { "{viewName}.html" })
	public ModelAndView request(HttpServletRequest request, @PathVariable String viewName) {
		return modelAndView(viewName);
	}
}