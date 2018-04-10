package com.fast.dev.search.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fast.dev.search.dao.RecordDao;
import com.fast.dev.search.dao.RecordHitDao;
import com.fast.dev.search.domain.RecordHit;

@Component
@EnableScheduling
public class RecordHitHelper {

	private static final Logger LOG = Logger.getLogger(RecordHitHelper.class);

	@Autowired
	private RecordHitDao recordHitDao;

	@Autowired
	private RecordDao recordDao;

	@Scheduled(cron = "0 0 0-23 * * ?")
	public void hitTimer() {
		triggerUpdateHitData();
	}

	@Scheduled(cron = "0 30 0-23 * * ?")
	public void reduceHitTimer() {
		int inc = 2;
		reduceHit(inc);
		LOG.info("reduceHit : " + inc);
	}

	/**
	 * 全局减少命中权重值
	 * 
	 * @param count
	 */
	public void reduceHit(int count) {
		this.recordHitDao.reduceHit(count);
	}

	/**
	 * 更新记录次数
	 * 
	 * @return
	 */
	public long updateRecordHit() {
		List<RecordHit> list = this.recordHitDao.getHitRecord(50);
		if (list.size() == 0) {
			return 0;
		}
		Map<String, Long> updateMap = new HashMap<>();
		for (RecordHit recordHit : list) {
			updateMap.put(recordHit.getId(), recordHit.getHit());
		}
		this.recordDao.setHit(updateMap);
		LOG.info("update hit : " + list.size());
		return list.size();
	}

	/**
	 * 触发更新同步事件
	 */
	public void triggerUpdateHitData() {
		while (updateRecordHit() > 0) {
		}
	}

}
