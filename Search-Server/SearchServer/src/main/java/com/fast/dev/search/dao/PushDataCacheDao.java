package com.fast.dev.search.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fast.dev.component.mongodb.dao.impl.MongoDaoImpl;
import com.fast.dev.search.domain.PushDataCache;

@Component
public class PushDataCacheDao extends MongoDaoImpl<PushDataCache> {

	
	public List<PushDataCache> getAndRemove(int limit) {
		return null;
	}

}
