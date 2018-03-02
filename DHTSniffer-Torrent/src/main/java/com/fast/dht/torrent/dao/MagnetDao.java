package com.fast.dht.torrent.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.fast.dht.db.dao.TorrentDao;
import com.fast.dht.db.domain.InfoHash;
import com.fast.dht.db.domain.Torrent;
import com.fast.dht.factory.MongodbFactory;
import com.fast.dht.model.FileModel;
import com.fast.dht.torrent.conf.TorrentConfig;
import com.fast.dht.torrent.util.TorrentReader;
import com.fast.dht.util.BytesUtil;

@Component
public class MagnetDao {

	private MongoTemplate mongoTemplate;

	private TorrentDao torrentDao;

	@Autowired
	private void init(TorrentConfig torrentConfig) {
		MongoDbFactory mongoDbFactory = MongodbFactory.mongoDbFactory(torrentConfig.getMongo());
		mongoTemplate = MongodbFactory.mongoTemplate(mongoDbFactory);
		torrentDao = new TorrentDao(mongoTemplate);
	}

	/**
	 * 获取一个磁力连接的hash
	 * 
	 * @return
	 */
	public synchronized String getOneMagnet() {
		Query query = new Query();
		query.with(new Sort(Direction.ASC, "getInfoCount"));
		query.limit(1);
		Update update = new Update();
		update.inc("getInfoCount", 1);
		InfoHash infoHash = this.mongoTemplate.findAndModify(query, update, InfoHash.class);
		return infoHash == null ? null : infoHash.getHash();
	}

	/**
	 * 保存到磁盘
	 * 
	 * @param reader
	 */
	public void save(TorrentReader reader) {
		if (reader == null) {
			return;
		}
		String hash = BytesUtil.binToHex(reader.getInfoHash()).toLowerCase();
		Torrent torrent = new Torrent();
		torrent.setHash(hash);
		if (reader.getCreationDate() != null) {
			torrent.setCreationTime(reader.getCreationDate().getTime());
		}
		torrent.setName(reader.getName());
		List<FileModel> files = new ArrayList<>();
		long size = 0;
		for (TorrentReader.TorrentFile torrentFile : reader.getFiles()) {
			String fileName = torrentFile.file.getPath();
			files.add(new FileModel(fileName, torrentFile.size));
			size += torrentFile.size;
		}
		torrent.setFiles(files.toArray(new FileModel[files.size()]));
		torrent.setSize(size);
		this.torrentDao.update(torrent);
	}

}
