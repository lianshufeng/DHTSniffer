package com.fast.dev.search.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.fast.dev.component.mongodb.dao.impl.MongoDaoImpl;
import com.fast.dev.search.domain.PushDataCache;

@Component
public class PushDataCacheDao extends MongoDaoImpl<PushDataCache> {

	/**
	 * 获取缓存数据
	 * 
	 * @param limit
	 * @return
	 */
	public List<PushDataCache> getCache(final int limit) {
		return this.mongoTemplate.findAllAndRemove(new Query().limit(limit), entityClass);
	}

}
