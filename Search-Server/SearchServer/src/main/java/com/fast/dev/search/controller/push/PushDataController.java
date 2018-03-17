package com.fast.dev.search.controller.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fast.dev.core.model.InvokerResult;
import com.fast.dev.search.helper.PushDatahelper;
import com.fast.dev.search.model.PushData;

@Controller
@RequestMapping("store")
public class PushDataController {

	@Autowired
	private PushDatahelper pushDatahelper;

	@RequestMapping("push.json")
	public InvokerResult<Object> push() {
		PushData data = new PushData("test" + System.currentTimeMillis(), "url", System.currentTimeMillis(), null);
		pushDatahelper.push(data);
		return new InvokerResult<Object>("finish");
	}

}
