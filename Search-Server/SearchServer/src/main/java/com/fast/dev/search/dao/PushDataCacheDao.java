package com.fast.dev.search.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.fast.dev.component.mongodb.dao.impl.MongoDaoImpl;
import com.fast.dev.component.mongodb.helper.time.DBTimerHelper;
import com.fast.dev.search.domain.PushDataCache;

@Component
public class PushDataCacheDao extends MongoDaoImpl<PushDataCache> {

	@Autowired
	private DBTimerHelper dbTimerHelper;

	/**
	 * 获取缓存数据
	 * 
	 * @param limit
	 * @return
	 */
	public synchronized List<PushDataCache> getCache(final int limit) {
		List<PushDataCache> caches = new ArrayList<>();
		for (int i = 0; i < limit; i++) {
			Update update = new Update();
			update.set("lastAccessTime", this.dbTimerHelper.getDBTime());
			PushDataCache pushDataCache = this.mongoTemplate.findAndModify(
					new Query(createReadCacheCriteria()).with(new Sort(Direction.ASC, "lastAccessTime")), update,
					entityClass);
			if (pushDataCache != null) {
				caches.add(pushDataCache);
			}
		}
		return caches;
	}

	/**
	 * 完成读取则删除该记录释放磁盘存储空间
	 * 
	 * @param ids
	 */
	public void finishCache(String... ids) {
		super.remove(ids);
	}

	/**
	 * 创建读取缓存的条件对象
	 * 
	 * @return
	 */
	private Criteria createReadCacheCriteria() {
		return Criteria.where("lastAccessTime").lt(this.dbTimerHelper.getDBTime() - 60000);
	}

}
