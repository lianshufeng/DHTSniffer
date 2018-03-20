package com.fast.dev.search.controller.page;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fast.dev.search.domain.RecordInfo;
import com.fast.dev.search.service.RecordService;

@Controller
@RequestMapping("page")
public class PageController {

	@Autowired
	private RecordService recordService;

	@RequestMapping("{id}.html")
	public ModelAndView view(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView("page.htl");
		RecordInfo recordInfo = this.recordService.page(id);
		if (recordInfo != null) {
			try {
				modelAndView.addObject("record", PropertyUtils.describe(recordInfo));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return modelAndView;
	}

}
