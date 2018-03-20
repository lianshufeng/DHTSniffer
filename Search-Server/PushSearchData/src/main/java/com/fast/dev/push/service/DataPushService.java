package com.fast.dev.push.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.fast.dev.core.util.bytes.BytesUtil;
import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.core.util.net.HttpClient;
import com.fast.dev.push.conf.HostServerConfig;
import com.fast.dev.push.conf.PushDataServiceConfig;
import com.fast.dev.push.factory.MongodbFactory;
import com.fast.dev.push.model.PushData;
import com.fast.dev.push.type.JobType;

public abstract class DataPushService {

	private static final Logger LOG = Logger.getLogger(DataPushService.class);

	// 读取状态
	private static final String ReadStat = "_readStat";
	// 读取时间
	private static final String ReadTime = "_readTime";

	@Autowired
	private HostServerConfig hostServerConfig;

	protected PushDataServiceConfig pushConfig;

	// 偏移每次读取量，失败就-1,成功+1
	private int readOffSetCount = 0;

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

		// 初始化需要使用的字段
		Query query = new Query();
		query.addCriteria(new Criteria().orOperator(Criteria.where(ReadStat).exists(false),
				Criteria.where(ReadTime).exists(false)));
		Update update = new Update();
		update.set(ReadStat, null);
		update.set(ReadTime, 0);
		this.mongoTemplate.updateMulti(query, update, Map.class, this.pushConfig.getCollectionName());

		// 增加索引
		initMongodbIndex(ReadStat);
		initMongodbIndex(ReadTime);

	}

	// 初始化索引
	private void initMongodbIndex(String itemName) {
		String indexName = itemName + "_Index";
		IndexOperations indexOperations = this.mongoTemplate.indexOps(this.pushConfig.getCollectionName());
		List<IndexInfo> nowIndexInfos = indexOperations.getIndexInfo();
		for (IndexInfo info : nowIndexInfos) {
			if (info.getName().equals(indexName)) {
				return;
			}
		}
		indexOperations.ensureIndex(new Index().named(indexName).on(itemName, Direction.ASC));
	}

	/**
	 * 读取记录
	 * 
	 * @param collectionName
	 * @param size
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected synchronized Collection<Map<String, Object>> readRecords() {

		int limit = this.pushConfig.getReadSize() + this.readOffSetCount;

		Query query = new Query();
		query.addCriteria(new Criteria().orOperator(Criteria.where(ReadStat).is(null),
				Criteria.where(ReadStat).is(JobType.Work)));
		query.with(new Sort(new Order(Direction.ASC, ReadTime)));
		query.limit(limit);

		Update update = new Update();
		update.set(ReadStat, JobType.Work);
		update.set(ReadTime, System.currentTimeMillis());

		List<Map<String, Object>> result = new ArrayList<>();
		for (int i = 0; i < limit; i++) {
			Map<String, Object> record = this.mongoTemplate.findAndModify(query, update, Map.class,
					this.pushConfig.getCollectionName());
			if (record != null) {
				result.add(record);
			}
		}
		return result;
	}

	/**
	 * 更新记录完成状态
	 * 
	 * @param id
	 */
	protected void updateRecordStat(String id, JobType jobType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(id));
		query.limit(1);

		Update update = new Update();
		update.set(ReadStat, jobType);

		this.mongoTemplate.updateFirst(query, update, Map.class, this.pushConfig.getCollectionName());
	}

	/**
	 * 批量传送数据
	 * 
	 * @param pushDatas
	 */
	@SuppressWarnings("unchecked")
	protected void post(Collection<PushData> pushDatas) {
		if (pushDatas == null || pushDatas.size() == 0) {
			return;
		}
		try {
			String content = null;
			try {
				content = JsonUtil.toJson(pushDatas);
				if (content.length() > 1024 * 1024 * 1.5 && pushDatas.size() > 1) {
					List<PushData> datas = new ArrayList<>(pushDatas);
					int toIndex = datas.size() / 2;
					post(datas.subList(0, toIndex));
					post(datas.subList(toIndex, datas.size() - 1));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			content = BytesUtil.binToHex(content.getBytes("UTF-8"));
			byte[] bin = String.format("token=%s&content=%s", hostServerConfig.getToken(), content).getBytes();
			HttpClient httpClient = new HttpClient();
			byte[] buff = httpClient.post(hostServerConfig.getHostUrl(), bin);
			String json = new String(buff, "UTF-8");
			LOG.info(this.getClass().getSimpleName() + " Post [" + pushDatas.size() + "] : " + json);
			Map<String, Map<String, String>> result = JsonUtil.toObject(json, Map.class);
			String stat = result.get("invokerResult").get("content");
			if ("finish".equalsIgnoreCase(stat)) {
				// 确认任务完成
				for (PushData pushData : pushDatas) {
					updateRecordStat(pushData.getId(), JobType.Finish);
				}
			}
			setReadOffSet(stat, pushDatas);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		setReadOffSet(null, pushDatas);
		return;
	}

	/**
	 * 成功传递的参数
	 */
	private synchronized void setReadOffSet(String stat, Collection<PushData> pushDatas) {
		// 取不到状态为服务器故障，不处理
		if (stat == null) {
			return;
		}

		// 保证最低读取一条
		if (pushDatas.size() == 0 && "finish".equalsIgnoreCase(stat)) {
			this.readOffSetCount = 1 - this.pushConfig.getReadSize();
			return;
		}
		// 1条也失败，这条可能永远发出去了，设置该记录错误
		if (!"finish".equalsIgnoreCase(stat) && pushDatas.size() == 1) {
			setRecordError(pushDatas);
			return;
		}

		// 发送成功则偏移+1
		if (pushDatas.size() > 0 && "finish".equalsIgnoreCase(stat)) {
			this.readOffSetCount++;
			if (this.readOffSetCount > 0) {
				this.readOffSetCount = 0;
			}
			return;
		}

		// 多条记录发送失败则下次自动读取少一条
		if (pushDatas.size() > 0 && !"finish".equalsIgnoreCase(stat)) {
			this.readOffSetCount--;
			if (this.pushConfig.getReadSize() + this.readOffSetCount < 1) {
				this.readOffSetCount = 1 - this.pushConfig.getReadSize();
			}
			return;
		}

	}

	/**
	 * 设置记录状态为错误
	 * 
	 * @param pushDatas
	 */
	private void setRecordError(Collection<PushData> pushDatas) {
		for (PushData pushData : pushDatas) {
			String id = pushData.getId();
			updateRecordStat(id, JobType.Error);
			LOG.info("error : " + id);
		}
	}

}
