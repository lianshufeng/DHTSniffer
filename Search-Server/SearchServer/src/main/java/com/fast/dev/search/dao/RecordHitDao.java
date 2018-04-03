package com.fast.dev.search.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.fast.dev.component.mongodb.dao.impl.MongoDaoImpl;
import com.fast.dev.search.domain.RecordHit;
import com.mongodb.WriteResult;

@Component
public class RecordHitDao extends MongoDaoImpl<RecordHit> {

	/**
	 * 增量更新 inc
	 * 
	 * @param id
	 * @param inc
	 */
	public boolean hitInc(String id, int inc) {
		Query query = new Query().addCriteria(Criteria.where("_id").is(id));
		Update update = new Update();
		update.inc("hit", inc);
		update.set("update", false);
		WriteResult writeResult = this.mongoTemplate.upsert(query, update, entityClass);
		return writeResult.getN() > 0;
	}

	/**
	 * 获取记录
	 * 
	 * @return
	 */
	public synchronized List<RecordHit> getHitRecord(int count) {
		List<RecordHit> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Query query = new Query();
			query.addCriteria(Criteria.where("update").is(false));
			Update update = new Update();
			update.set("update", true);
			RecordHit recordHit = this.mongoTemplate.findAndModify(query, update, entityClass);
			if (recordHit == null) {
				break;
			}
			list.add(recordHit);
		}
		return list;
	}

	

}
