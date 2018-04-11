package com.fast.dev.search.controller.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fast.dev.core.model.InvokerResult;
import com.fast.dev.search.service.RecordTagService;

@Controller
@RequestMapping("manager")
public class ManagerResetController {

	@Autowired
	private RecordTagService recordTagService;

	@RequestMapping("reset/tag.json")
	public InvokerResult<Object> resetTag() {
		this.recordTagService.resetTag();
		return new InvokerResult<Object>("finish");
	}

}
