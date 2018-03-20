package com.fast.dev.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fast.dev.search.dao.PushDataCacheDao;
import com.fast.dev.search.domain.PushDataCache;
import com.fast.dev.search.model.PushData;
import com.fast.dev.search.service.RecordService;

/**
 * 上传数据的工具,缓存处理
 * 
 * @作者 练书锋
 * @时间 2018年3月18日
 *
 *
 */
@Component
@EnableScheduling
public class PushDatahelper {
	private static final Logger LOG = Logger.getLogger(PushDatahelper.class);

	@Autowired
	private RecordService recordService;

	@Autowired
	private PushDataCacheDao dataCacheDao;

	/**
	 * 保存数据
	 * 
	 * @param data
	 */
	public void push(PushData... datas) {
		for (PushData data : datas) {
			PushDataCache pushDataCache = new PushDataCache();
			BeanUtils.copyProperties(data, pushDataCache);
			this.dataCacheDao.save(pushDataCache);
		}
	}

	@Scheduled(cron = "*/10 * * * * ?")
	public void executeTask() {
		
		
//		this.dataCacheDao.(new Query().limit(500));
		
//		List<?> keys = DataCache.getKeys();
//		if (keys != null && keys.size() > 0 && task(keys)) {
//			DataCache.removeAll(keys);
//		}
	}

	/**
	 * 开始任务
	 * 
	 * @param keys
	 * @return
	 */
	private boolean task(List<?> keys) {
		LOG.info("Push Data : " + keys.size());

		try {
			// 缓存
			List<PushData> datas = new ArrayList<PushData>();

//			for (Element element : DataCache.getAll(keys).values()) {
//				datas.add((PushData) element.getObjectValue());
//				if (datas.size() >= 500) {
//					recordService.save(new ArrayList<>(datas));
//					datas.clear();
//				}
//			}
			
			
			// 保存剩余的数据
			if (datas.size() > 0) {
				recordService.save(datas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
