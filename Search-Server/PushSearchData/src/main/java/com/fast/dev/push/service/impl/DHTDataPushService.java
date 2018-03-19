package com.fast.dev.push.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fast.dev.push.model.PushData;
import com.fast.dev.push.service.DataPushService;

/**
 * DHT 数据推送
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月18日
 *
 */
@Component
@Scope("prototype")
public class DHTDataPushService extends DataPushService {

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
	@SuppressWarnings("unchecked")
	private Collection<PushData> toPushData(Collection<Map<String, Object>> result) {
		Collection<PushData> pushDatas = new ArrayList<>();
		for (Map<String, Object> item : result) {
			PushData data = new PushData();
			data.setId(String.valueOf(item.get("_id")));
			Object hash = item.get("hash");
			if (hash == null) {
				continue;
			}
			String url = "magnet:?xt=urn:btih:" + hash;

			Object title = item.get("name");
			Object time = item.get("creationTime");
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

			// 文件列表
			Object files = item.get("files");
			if (files != null) {
				Map<String, Long> fileMap = new HashMap<>();
				// 转换为文件列表
				for (Map<String, Long> m : (Collection<Map<String, Long>>) files) {
					fileMap.put(String.valueOf(m.get("path")), (long) m.get("size"));
				}
				data.setFiles(fileMap);
			}

			pushDatas.add(data);
		}
		return pushDatas;
	}

}
