package com.fast.dev.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fast.dev.component.cachemethod.annotations.CacheMethod;
import com.fast.dev.search.dao.RecordTagDao;
import com.fast.dev.search.domain.RecordTag;
import com.fast.dev.search.reset.ResetTaskManager;
import com.fast.dev.search.tags.TagManager;

@Service
public class RecordTagService {

	@Autowired
	private TagManager tagManager;

	@Autowired
	private RecordTagDao recordTagDao;

	@Autowired
	private ResetTaskManager resetTaskManager;

	/**
	 * 获取标签列表
	 * 
	 * @return
	 */
	@CacheMethod(collectionName = "Record_Tags", overflowToDisk = true, maxMemoryCount = 100, timeToIdleSeconds = 300, timeToLiveSeconds = 300)
	public RecordTag[] tags() {
		String[] tags = this.tagManager.tags();
		return this.recordTagDao.list(tags);
	}

	/**
	 * 重置所有标签到搜索引擎里
	 */
	public void resetTag() {
		int size = 300;
		this.resetTaskManager.execute(0, size, size*10l);
	}

}
