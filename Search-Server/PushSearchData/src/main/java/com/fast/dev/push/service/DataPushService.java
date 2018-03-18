package com.fast.dev.push.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.push.conf.PushDataServiceConfig;
import com.fast.dev.push.factory.MongodbFactory;
import com.mongodb.util.JSON;

public abstract class DataPushService {

	private static final String ReadedValue = "_readValue";

	protected PushDataServiceConfig pushConfig;

	/**
	 * 构建接口
	 * 
	 * @return
	 */
	public void build(PushDataServiceConfig pushConfig) {
		this.pushConfig = pushConfig;
	}

	protected MongoTemplate mongoTemplate;

	/**
	 * 执行
	 */
	public abstract void execute();

	/**
	 * 初始化数据库
	 */
	protected void initMongodb() {
		// 初始化mongodb
		if (mongoTemplate == null) {
			MongoDbFactory mongoDbFactory = MongodbFactory.mongoDbFactory(this.pushConfig.getMongo());
			mongoTemplate = MongodbFactory.mongoTemplate(mongoDbFactory);
		}
	}

	/**
	 * 读取记录
	 * 
	 * @param collectionName
	 * @param size
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected List<Map> readRecords(String collectionName, int size) {

		
//		try {
//			Query query2 = new Query();
//			query2.addCriteria(Criteria.where(ReadedValue).exists(true);
//			query2.limit(size);
//			System.out.println(JsonUtil.toJson(this.mongoTemplate.find(query2, Map.class,collectionName)));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		String uuid = UUID.randomUUID().toString();
		Query query = new Query();
		query.limit(size);
		query.addCriteria(Criteria.where(ReadedValue).exists(true));

		Update update = new Update();
		update.set(ReadedValue, uuid);
		this.mongoTemplate.updateMulti(query, update, collectionName);

		return this.mongoTemplate.find(new Query().addCriteria(Criteria.where(ReadedValue).is(uuid)), Map.class,
				collectionName);
	}

}
