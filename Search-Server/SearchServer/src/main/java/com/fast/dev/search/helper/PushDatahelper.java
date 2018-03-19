package com.fast.dev.search.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fast.dev.search.model.PushData;
import com.fast.dev.search.service.RecordService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

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

	private final static String CacheName = "PushDataCache";
	private final static CacheManager CacheMgr = CacheManager.create();
	private static final Ehcache DataCache = new Cache(CacheName, 0, true, true, 0, 0, true, 60);

	static {
		CacheMgr.addCache(DataCache);
	}

	@Autowired
	private RecordService recordService;

	/**
	 * 必须正常退出才能持久到磁盘上
	 */
	@PreDestroy
	private void shutdown() {
		CacheMgr.shutdown();
	}

	/**
	 * 保存数据
	 * 
	 * @param data
	 */
	public void push(PushData... datas) {
		for (PushData data : datas) {
			DataCache.put(new Element(UUID.randomUUID(), data));
		}
	}

	@Scheduled(cron = "*/10 * * * * ?")
	public void executeTask() {
		List<?> keys = DataCache.getKeys();
		if (keys != null && keys.size() > 0 && task(keys)) {
			DataCache.removeAll(keys);
		}
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
			for (Element element : DataCache.getAll(keys).values()) {
				datas.add((PushData) element.getObjectValue());
				if (datas.size() >= 500) {
					recordService.save(new ArrayList<>(datas));
					datas.clear();
				}
			}
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
