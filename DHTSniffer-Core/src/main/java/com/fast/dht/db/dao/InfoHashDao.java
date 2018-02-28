package com.fast.dht.db.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fast.dht.db.domain.InfoHash;

/**
 * 继承父类
 * 
 * @作者 练书锋
 * @时间 2018年2月28日
 *
 *
 */
public class InfoHashDao extends SuperDao {

	public InfoHashDao(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

	/**
	 * 更新主机
	 * 
	 * @param hash
	 * @param updateHost
	 */
	public void updateHash(String hash, String updateHost) {
		Query query = new Query().addCriteria(Criteria.where("hash").is(hash));
		Update update = createUpdateHash(updateHost);
		super.mongoTemplate.upsert(query, update, InfoHash.class);
	}

	/**
	 * 创建更新的hash
	 * 
	 * @param updateHost
	 * @return
	 */
	private Update createUpdateHash(String updateHost) {
		Update update = createUpdate();
		update.inc("accessCount", 1);
		update.set("updateHost", updateHost);
		return update;
	}

}
