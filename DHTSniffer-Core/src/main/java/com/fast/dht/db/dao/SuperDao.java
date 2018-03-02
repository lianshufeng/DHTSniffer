package com.fast.dht.db.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fast.dht.db.util.DBUtil;

public abstract class SuperDao {

	public MongoTemplate mongoTemplate;

	public SuperDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * 创建更新的hash
	 * 
	 * @param updateHost
	 * @return
	 */
	protected Update createUpdate() {
		Update update = new Update();
		update.set("updateTime", DBUtil.getTime());
		update.setOnInsert("createTime", DBUtil.getTime());
		return update;
	}

	/**
	 * 创建hash查询的查询对象
	 * 
	 * @param hash
	 * @return
	 */
	protected Query createHashQuery(String hash) {
		return new Query().addCriteria(Criteria.where("hash").is(hash));
	}

	/**
	 * 是否已存在
	 * 
	 * @param hash
	 * @return
	 */
	public boolean exists(String hash, Class<?> entityClass) {
		Query query = createHashQuery(hash);
		return this.mongoTemplate.exists(query, entityClass);
	}

}
