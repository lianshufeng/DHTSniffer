package com.fast.dev.push.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.core.util.net.HttpClient;
import com.fast.dev.push.conf.HostServerConfig;
import com.fast.dev.push.conf.PushDataServiceConfig;
import com.fast.dev.push.factory.MongodbFactory;
import com.fast.dev.push.model.PushData;

public abstract class DataPushService {

	private static final Logger LOG = Logger.getLogger(DataPushService.class);

	private static final String ReadedValue = "_readValue";

	@Autowired
	private HostServerConfig hostServerConfig;

	protected PushDataServiceConfig pushConfig;

	/**
	 * 构建接口
	 * 
	 * @return
	 */
	public void build(PushDataServiceConfig pushConfig) {
		this.pushConfig = pushConfig;
		initMongodb();
	}

	protected MongoTemplate mongoTemplate;

	/**
	 * 执行
	 */
	public abstract void execute();

	/**
	 * 初始化数据库
	 */
	private void initMongodb() {
		MongoDbFactory mongoDbFactory = MongodbFactory.mongoDbFactory(this.pushConfig.getMongo());
		mongoTemplate = MongodbFactory.mongoTemplate(mongoDbFactory);
	}

	/**
	 * 读取记录
	 * 
	 * @param collectionName
	 * @param size
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	protected Collection<Map<String, Object>> readRecords(int size) {

		String uuid = UUID.randomUUID().toString();
		Query query = new Query();
		query.addCriteria(Criteria.where(ReadedValue).exists(false));

		Update update = new Update();
		update.set(ReadedValue, uuid);

		List<Map<String, Object>> result = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			Map<String, Object> record = this.mongoTemplate.findAndModify(query, update, Map.class,
					this.pushConfig.getCollectionName());
			if (record != null) {
				result.add(record);
			}
		}
		return result;
	}

	/**
	 * 批量传送数据
	 * 
	 * @param pushDatas
	 */
	protected void post(Collection<PushData> pushDatas) {
		try {
			String jsonContent = JsonUtil.toJson(pushDatas);
			byte[] bin = String.format("token=%s&content=%s", hostServerConfig.getToken(), jsonContent).getBytes();
			byte[] buff = new HttpClient().post(hostServerConfig.getHostUrl(), bin);
			LOG.info("post : " + pushDatas.size() + "  -> " + new String(buff));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
