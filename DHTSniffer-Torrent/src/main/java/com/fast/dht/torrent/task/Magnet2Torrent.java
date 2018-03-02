package com.fast.dht.torrent.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fast.dht.torrent.TorrentMain;
import com.fast.dht.torrent.conf.TorrentConfig;
import com.fast.dht.torrent.dao.MagnetDao;
import com.fast.dht.torrent.dht.DHTServer;
import com.fast.dht.torrent.util.TorrentReader;

@Component
@Scope("prototype")
public class Magnet2Torrent implements Runnable {
//	private static final Logger LOG = Logger.getLogger(Magnet2Torrent.class);

	@Autowired
	private MagnetDao magnetDao;

	@Autowired
	private TorrentConfig torrentConfig;

	@Autowired
	private DHTServer dhtServer;

	@Override
	public void run() {
		String hash = getHash();
		// 执行任务
		try {
			task(hash);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// 下一个任务
			next();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下一个任务
	 */
	private void next() {
		try {
			Thread.sleep(torrentConfig.getTaskSleepTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		TorrentMain.fixedThreadPool.execute(this);
	}

	/**
	 * 开始任务
	 */
	private void task(String hash) {
		TorrentReader torrentReader = this.dhtServer.query(hash);
		if (torrentReader != null) {
			this.magnetDao.save(torrentReader);
		}
	}

	/**
	 * 获取磁力连接的hash
	 * 
	 * @return
	 */
	private String getHash() {
		return this.magnetDao.getOneMagnet();
	}

}
