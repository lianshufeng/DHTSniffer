package com.fast.dev.search.timer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fast.dev.search.dao.HotWordsDao;
import com.hp.hpl.sparta.ParseException;

@Component
@EnableScheduling
public class CleanTimeoutData {

	@Autowired
	private HotWordsDao hotWordsDao;

	private static final Logger LOG = Logger.getLogger(CleanTimeoutData.class);

	private static final String PreCollectionName = HotWordsDao.PreCollectionName;

	/**
	 * 删除旧数据
	 * 
	 * @throws ParseException
	 */
	@Scheduled(cron = "0 0 * * * ?")
	private void removeOldCollection() {
		MongoTemplate mongoTemplate = this.hotWordsDao.getMongoTemplate();
		long nowTime = System.currentTimeMillis();
		for (String name : mongoTemplate.getCollectionNames()) {
			if (name.length() > PreCollectionName.length()
					&& name.substring(0, PreCollectionName.length()).equals(PreCollectionName)) {
				String time = name.substring(PreCollectionName.length(), name.length());
				try {
					long dateTime = HotWordsDao.DateFormat.parse(time).getTime();
					// 大约15天
					if (nowTime - dateTime > 1000 * 60 * 60 * 24 * 15) {
						mongoTemplate.dropCollection(name);
						LOG.info(String.format("remove old hotword : %s", name));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
