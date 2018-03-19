package com.fast.dev.push.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fast.dev.push.model.PushData;
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
	public void execute() {
		Collection<Map<String, Object>> result = readRecords();
		if (result != null && result.size() > 0) {
			// 转换并推送数据
			super.post(toPushData(result));
		}
	}

	/**
	 * 转换到推送的数据格式
	 * 
	 * @param result
	 * @return
	 */
	private Collection<PushData> toPushData(Collection<Map<String, Object>> result) {
		Collection<PushData> pushDatas = new ArrayList<>();
		for (Map<String, Object> item : result) {
			PushData data = new PushData();
			data.setId(String.valueOf(item.get("_id")));
			Object url = item.get("url");
			Object title = item.get("title");
			Object time = item.get("publishTime");
			if (url == null) {
				continue;
			}
			// URL
			data.setUrl(String.valueOf(url));
			// 标题
			if (title != null) {
				data.setTitle(String.valueOf(title));
			}
			// 发布时间
			if (time != null) {
				data.setTime((long) time);
			}
			pushDatas.add(data);
		}
		return pushDatas;
	}

}
