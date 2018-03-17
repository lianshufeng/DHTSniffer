package com.fast.dev.search.helper;

import java.util.UUID;

import javax.annotation.PreDestroy;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fast.dev.search.model.PushData;

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

	private final static String CacheName = "PushDataCache";
	private final static CacheManager CacheMgr = CacheManager.create();
	private static final Ehcache DataCache = new Cache(CacheName, 0, true, true, 0, 0, true, 60);

	static {
		CacheMgr.addCache(DataCache);
	}

	@PreDestroy
	private void shutdown() {
		CacheMgr.shutdown();
	}

	/**
	 * 保存数据
	 * 
	 * @param data
	 */
	public void push(PushData data) {
		DataCache.put(new Element(UUID.randomUUID(), data));
	}

	@Scheduled(cron = "*/3 * * * * ?")
	public void executeTask() {
		System.out.println(DataCache.getSize());
	}

	private void task() {

	}

}
