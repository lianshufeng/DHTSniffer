package com.fast.dht.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;

import com.fast.dht.boot.DHTSnifferBoot;
import com.fast.dht.db.domain.SuperEntity;
import com.fast.dht.impl.DHTSnifferServerImpl;
import com.fast.dht.model.JQGridQuery;
import com.fast.dht.model.JQGridResult;

/**
 * 数据查询工具工具
 * 
 * @作者 练书锋
 * @时间 2018年3月1日
 *
 *
 */
public class DataDao {

	static MongoTemplate mongoTemplate = null;

	/**
	 * 查询数据
	 * 
	 * @param cls
	 * @param jqGridQuery
	 * @return
	 */
	public static JQGridResult list(Class<? extends SuperEntity> cls, JQGridQuery jqGridQuery) {
		init();
		return query(cls, jqGridQuery);
	}

	private static JQGridResult query(Class<? extends SuperEntity> entityClass, JQGridQuery jqGridQuery) {
		Direction direction = Direction.DESC;
		String indexName = "createTime";

		if (!StringUtils.isEmpty(jqGridQuery.getSord())) {
			direction = Direction.valueOf(jqGridQuery.getSord().toUpperCase());
		}
		if (!StringUtils.isEmpty(jqGridQuery.getSidx())) {
			indexName = jqGridQuery.getSidx();
		}
		// 分页查询
		Query query = new Query();
		query.skip((jqGridQuery.getPage() - 1) * jqGridQuery.getRows());
		query.limit(jqGridQuery.getRows());
		query.with(new Sort(direction, indexName));

		List<? extends SuperEntity> list = mongoTemplate.find(query, entityClass);
		// 数据
		Object[] rows = list.toArray(new Object[list.size()]);
		// 总记录数
		long records = mongoTemplate.count(new Query(), entityClass);
		int page = jqGridQuery.getPage();
		// 总页数
		int total = 0;
		if (records % jqGridQuery.getRows() == 0) {
			total = (int) (records / jqGridQuery.getRows());
		} else {
			total = (int) (records / jqGridQuery.getRows()) + 1;
		}

		JQGridResult jqGridResult = new JQGridResult();
		jqGridResult.setRecords(records);
		jqGridResult.setRows(rows);
		jqGridResult.setPage(page);
		jqGridResult.setTotal(total);

		return jqGridResult;
	}

	/**
	 * 初始化查询
	 */
	private static void init() {
		if (mongoTemplate == null) {
			if (DHTSnifferBoot.applicationContext != null) {
				DHTSnifferServerImpl dhtSnifferServerImpl = DHTSnifferBoot.applicationContext
						.getBean(DHTSnifferServerImpl.class);
				if (dhtSnifferServerImpl != null) {
					mongoTemplate = dhtSnifferServerImpl.infoHashDao.mongoTemplate;
				}
			}
		}
	}

}
