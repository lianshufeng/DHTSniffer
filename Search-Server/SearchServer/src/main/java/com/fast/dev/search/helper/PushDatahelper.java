package com.fast.dev.search.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fast.dev.core.util.code.JsonUtil;
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
		for (PushData content : datas) {
			try {
				PushDataCache pushDataCache = new PushDataCache();
				pushDataCache.setContent(JsonUtil.toJson(content));
				this.dataCacheDao.save(pushDataCache);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "*/10 * * * * ?")
	public void executeTask() {
		List<PushDataCache> dataCaches = this.dataCacheDao.getCache(1000);
		if (dataCaches != null && dataCaches.size() > 0) {
			List<PushData> datas = new ArrayList<>();
			for (PushDataCache dataCache : dataCaches) {
				try {
					datas.add(JsonUtil.toObject(dataCache.getContent(), PushData.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			task(datas);
		}
	}

	/**
	 * 开始任务
	 * 
	 * @param keys
	 * @return
	 */
	private boolean task(List<PushData> datas) {
		LOG.info("Push Data : " + datas.size());
		try {
			this.recordService.save(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
