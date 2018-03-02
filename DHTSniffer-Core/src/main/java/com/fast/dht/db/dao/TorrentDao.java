package com.fast.dht.db.dao;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.fast.dht.db.domain.Torrent;
import com.fast.dht.db.util.DBUtil;

/**
 * 种子文件的操作对象
 * 
 * @作者 练书锋
 * @时间 2018年2月28日
 *
 *
 */
public class TorrentDao extends SuperDao {

	public TorrentDao(MongoTemplate mongoTemplate) {
		super(mongoTemplate);
	}

	/**
	 * 保存种子信息
	 * 
	 * @param hash
	 * @param torrent
	 */
	public void update(Torrent torrent) {
		String hash = torrent.getHash();
		if (exists(hash, Torrent.class)) {
			this.mongoTemplate.remove(createHashQuery(hash), Torrent.class);
		}

		torrent.setCreateTime(DBUtil.getTime());
		torrent.setUpdateTime(DBUtil.getTime());
		this.mongoTemplate.save(torrent);
	}

}
