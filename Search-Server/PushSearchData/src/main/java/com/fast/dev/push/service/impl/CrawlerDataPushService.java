package com.fast.dev.push.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.push.service.DataPushService;

/**
 * 网站数据推送
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月18日
 *
 */
@Component
@Scope("prototype")
public class CrawlerDataPushService extends DataPushService {

	@Override
	@SuppressWarnings("rawtypes")
	public void execute() {
		initMongodb();
		List<Map> result = readRecords("resources", 1);
		try {
			System.out.println(JsonUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
