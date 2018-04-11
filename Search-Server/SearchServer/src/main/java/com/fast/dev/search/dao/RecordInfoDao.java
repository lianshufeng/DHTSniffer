package com.fast.dev.search.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.fast.dev.component.mongodb.dao.impl.MongoDaoImpl;
import com.fast.dev.search.domain.RecordInfo;

@Component
public class RecordInfoDao extends MongoDaoImpl<RecordInfo> {

	@Autowired
	private void init() {
		if (!this.mongoTemplate.collectionExists(entityClass)) {
			this.mongoTemplate.createCollection(entityClass);
		}
	}

	/**
	 * 是否存在
	 * 
	 * @param url
	 * @return
	 */
	public boolean urlExits(String url) {
		return this.mongoTemplate.exists(new Query().addCriteria(Criteria.where("url").is(url)), entityClass);
	}

	/**
	 * 查询指定的时间段数据
	 * 
	 */
	public List<RecordInfo> findFromTime(long time, int limit) {
		Query query = new Query();
		query.addCriteria(Criteria.where("createTime").gt(time));
		query.limit(limit);
		query.with(new Sort(Direction.ASC, "createTime"));
		return	this.mongoTemplate.find(query, entityClass); 
	}

}
