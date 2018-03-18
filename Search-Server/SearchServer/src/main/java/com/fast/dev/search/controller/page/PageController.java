package com.fast.dev.search.controller.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fast.dev.search.service.RecordService;

@Controller
@RequestMapping("page")
public class PageController {

	@Autowired
	private RecordService recordService;

	@RequestMapping("{id}.html")
	public ModelAndView view(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView("page.htl");
		modelAndView.addObject("record", this.recordService.page(id));
		return modelAndView;
	}

}
