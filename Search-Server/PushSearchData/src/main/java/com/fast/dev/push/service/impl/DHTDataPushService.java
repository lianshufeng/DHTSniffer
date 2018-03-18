package com.fast.dev.push.service.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
		initMongodb();
		
	}


}
