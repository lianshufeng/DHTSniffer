package com.fast.dev.search.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fast.dev.component.mongodb.dao.impl.MongoDaoImpl;
import com.fast.dev.search.domain.HotWord;
import com.mongodb.DBCollection;

/**
 * 热词Dao
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月28日
 *
 */
@Component
public class HotWordsDao extends MongoDaoImpl<HotWord> {

	public static final String PreCollectionName = "hotWorld_";
	public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMdd");

	// 创建的缓存
	private Vector<String> collectionBuildCache = new Vector<>();

	/**
	 * 增加热词
	 * 
	 * @param hitCount
	 * @param worlds
	 */
	public void hit(int hitCount, String... worlds) {
		if (worlds == null || worlds.length == 0) {
			return;
		}
		String collectionName = getCollectionName(System.currentTimeMillis());
		for (String world : worlds) {
			if (StringUtils.isEmpty(world)) {
				continue;
			}
			Query query = new Query(Criteria.where("name").is(world));
			Update update = new Update();
			update.inc("hit", hitCount);
			update.setOnInsert("name", world);
			this.mongoTemplate.upsert(query, update, entityClass, collectionName);
		}
	}

	/**
	 * 取热词列表， 取每天热词排名，然后依次相加，取出最高热词
	 * 
	 * @param maxSize
	 * @param day
	 * @return
	 */
	public List<HotWord> list(int maxSize, int day) {
		// 取出最近的时间
		List<Long> dayTimes = getLastDays(day);
		Map<String, Long> sources = new HashMap<>();
		// 循环查询每天并合并
		for (long time : dayTimes) {
			List<HotWord> hotWords = getHotWorldsTop(maxSize, time);
			mergetHotWords(sources, hotWords);
		}
		// 合并数据集合
		List<HotWord> hotWords = new ArrayList<>();
		for (Entry<String, Long> entry : sources.entrySet()) {
			hotWords.add(new HotWord(entry.getKey(), entry.getValue()));
		}
		// 排序
		Collections.sort(hotWords);
		return new ArrayList<>(hotWords.subList(0, maxSize <= hotWords.size() ? maxSize : hotWords.size()));
	}

	/**
	 * 合并热词
	 * 
	 * @param sources
	 * @param hw
	 */
	private void mergetHotWords(Map<String, Long> sources, List<HotWord> hotWords) {
		if (hotWords == null || hotWords.size() == 0) {
			return;
		}
		for (HotWord hw : hotWords) {
			Long hit = sources.get(hw.getName());
			if (hit == null) {
				sources.put(hw.getName(), hw.getHit());
			} else {
				sources.put(hw.getName(), hit + hw.getHit());
			}
		}

	}

	/**
	 * 获取指定top热词
	 * 
	 * @param maxSize
	 * @param collectionName
	 * @return
	 */
	private List<HotWord> getHotWorldsTop(int maxSize, long time) {
		String collectionName = formatCollectionName(time);
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "hit"));
		query.limit(maxSize);
		return this.mongoTemplate.find(query, entityClass, collectionName);
	}

	/**
	 * 获取最后几天的时间
	 * 
	 * @param count
	 * @return
	 */
	private static List<Long> getLastDays(int count) {
		List<Long> list = new ArrayList<>();
		Date nowDate = new Date(System.currentTimeMillis());
		list.add(nowDate.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		for (int i = 0; i < count - 1; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			list.add(calendar.getTime().getTime());
		}
		return list;
	}

	/**
	 * 取当前写入的表名
	 * 
	 * @return
	 */
	private String getCollectionName(long time) {
		String name = formatCollectionName(time);
		if (collectionBuildCache.contains(name)) {
			return name;
		}
		if (!this.mongoTemplate.collectionExists(name)) {
			DBCollection dbCollection = this.mongoTemplate.createCollection(name);
			dbCollection.createIndex("name");
			dbCollection.createIndex("hit");
		}
		collectionBuildCache.add(name);
		return name;
	}

	/**
	 * 指定时间格式的表名
	 * 
	 * @param time
	 * @return
	 */
	private static String formatCollectionName(long time) {
		return PreCollectionName + DateFormat.format(time);
	}

	/**
	 * 取出Dao对象
	 * 
	 * @return
	 */
	public MongoTemplate getMongoTemplate() {
		return this.mongoTemplate;
	}

}
