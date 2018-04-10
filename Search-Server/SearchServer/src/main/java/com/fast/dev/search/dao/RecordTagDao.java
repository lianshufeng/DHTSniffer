package com.fast.dev.search.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.fast.dev.component.mongodb.dao.impl.MongoDaoImpl;
import com.fast.dev.component.mongodb.util.EntityObjectUtil;
import com.fast.dev.search.domain.RecordTag;

@Component
public class RecordTagDao extends MongoDaoImpl<RecordTag> {

	/**
	 * 增加标签数量
	 * 
	 * @param tags
	 */
	public void inc(String... tags) {
		for (String tag : tags) {
			Query query = new Query();
			query.addCriteria(Criteria.where("name").is(tag));
			Update update = new Update();
			update.inc("count", 1);
			update.setOnInsert("name", tag);
			this.mongoTemplate.upsert(query, update, entityClass);
		}
	}

	/**
	 * 查询列表
	 * 
	 * @param names
	 */
	public RecordTag[] list(String... names) {
		Criteria criteriaIds = EntityObjectUtil.createQueryBatch("name", names);
		// 查询的结果集
		List<RecordTag> list = this.mongoTemplate.find(new Query(criteriaIds), entityClass);
		Map<String, RecordTag> sort = new HashMap<String, RecordTag>();
		for (RecordTag rt : list) {
			sort.put(rt.getName(), rt);
		}
		// 结果集
		List<RecordTag> reuslt = new ArrayList<>();
		for (String name : names) {
			RecordTag recordTag = sort.get(name);
			if (recordTag != null) {
				reuslt.add(recordTag);
			}
		}
		return reuslt.toArray(new RecordTag[0]);

	}

}
