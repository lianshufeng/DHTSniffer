package com.fast.dht.db.dao;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
	 * 是否已存在该记录
	 * 
	 * @param hash
	 * @return
	 */
	public boolean exists(String hash) {
		Query query = createHashQuery(hash);
		return this.mongoTemplate.exists(query, Torrent.class);
	}
	
	
	/**
	 * 增加访问次数
	 * @param hash
	 */
	public void incAccessCount(String hash) {
		Query query = createHashQuery(hash);
		Update update = createUpdate();
		update.inc("accessCount", 1);
		this.mongoTemplate.updateFirst(query, update, Torrent.class);
	}
	

	/**
	 * 保存种子信息
	 * 
	 * @param hash
	 * @param torrent
	 */
	public void save(String hash, com.fast.dht.net.model.Torrent torrent) {

		Torrent entity = new Torrent();
		entity.setCreateTime(DBUtil.getTime());
		entity.setUpdateTime(DBUtil.getTime());
		entity.setHash(hash);
		entity.setAccessCount(1);

		BeanUtils.copyProperties(torrent, entity);
		if (torrent.getAnnounceList() != null) {
			entity.setAnnounces(torrent.getAnnounceList().toArray(new String[0]));
		}
		if (torrent.getCreationDate() != null) {
			entity.setCreationTime(torrent.getCreationDate().getTime());
		}
		this.mongoTemplate.save(entity);
	}

}
