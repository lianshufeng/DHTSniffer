package com.fast.dht.torrent.dht;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fast.dht.torrent.conf.TorrentConfig;
import com.fast.dht.torrent.util.TorrentReader;
import com.frostwire.jlibtorrent.SessionManager;
import com.frostwire.jlibtorrent.SessionParams;
import com.frostwire.jlibtorrent.SettingsPack;
import com.frostwire.jlibtorrent.swig.settings_pack;

@Component
public class DHTServer {

	@Autowired
	private TorrentConfig torrentConfig;

	private static final Logger LOG = Logger.getLogger(DHTServer.class);

	SessionManager sessionManager;
	SessionParams params;

	long currentTotalNode = 0;

	@Autowired
	private void init() {
		sessionManager = new SessionManager();
		SettingsPack sp = new SettingsPack();

		sp.setString(settings_pack.string_types.dht_bootstrap_nodes.swigValue(), "dht.aelitis.com:6881");
		sp.setString(settings_pack.string_types.dht_bootstrap_nodes.swigValue(), "router.utorrent.com:6881");
		sp.setString(settings_pack.string_types.dht_bootstrap_nodes.swigValue(), "router.bittorrent.com:6881");
		sp.setString(settings_pack.string_types.dht_bootstrap_nodes.swigValue(), "dht.transmissionbt.com:6881");
		params = new SessionParams(sp);
		sessionManager.start(params);

		waitMoreNodes();

	}

	/**
	 * 请求种子文件
	 */
	public TorrentReader query(final String hash) {
		String magnet = createMagnet(hash);
		return queryTask(magnet);
	}

	private TorrentReader queryTask(final String magnet) {
		LOG.info(String.format("request : %s ", magnet));
		byte[] data = this.sessionManager.fetchMagnet(magnet, (int) (this.torrentConfig.getTaskTimeout() / 1000));
		LOG.info(String.format(" [ %s ]   : %s", magnet, data != null));
		if (data == null) {
			return null;
		}
		try {
			return new TorrentReader(data, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建磁力连
	 * 
	 * @return
	 */
	private String createMagnet(String hash) {
		return String.format("magnet:?xt=urn:btih:%s", hash);
	}

	private void waitMoreNodes() {
		long nodes = sessionManager.stats().dhtNodes();
		if (nodes < this.torrentConfig.getMinNodesCount()) {
			final CountDownLatch signal = new CountDownLatch(1);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					long nodes = sessionManager.stats().dhtNodes();
					if (currentTotalNode != nodes) {
						currentTotalNode = nodes;
						LOG.info(String.format("dhtNodes : %s", currentTotalNode));
					}
					if (nodes >= torrentConfig.getMinNodesCount()) {
						if (signal.getCount() != 0) {
							signal.countDown();
						}
					}
				}
			}, 0, 1000);
			LOG.info("wait more node ....");
			try {
				signal.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
