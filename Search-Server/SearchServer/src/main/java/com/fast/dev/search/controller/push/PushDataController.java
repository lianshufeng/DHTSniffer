package com.fast.dev.search.controller.push;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fast.dev.core.model.InvokerResult;
import com.fast.dev.core.util.bytes.BytesUtil;
import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.search.helper.PushDatahelper;
import com.fast.dev.search.model.PushData;
import com.fast.dev.search.type.PushResultType;

@Controller
@RequestMapping("store")
public class PushDataController {

	@Autowired
	private PushDatahelper pushDatahelper;

	@RequestMapping("push.json")
	public InvokerResult<PushResultType> push(HttpServletResponse response, String token, String content) {
		if (content == null) {
			return new InvokerResult<PushResultType>(PushResultType.ContentNull);
		}
		try {
			String json = new String(BytesUtil.hexToBin(content), "UTF-8");
			PushData[] pushDatas = JsonUtil.toObject(json, PushData[].class);
			pushDatahelper.push(pushDatas);
			return new InvokerResult<PushResultType>(PushResultType.Finish);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new InvokerResult<PushResultType>(PushResultType.Error);
	}

}
