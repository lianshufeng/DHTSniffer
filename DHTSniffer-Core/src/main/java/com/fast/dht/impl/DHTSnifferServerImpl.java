package com.fast.dht.impl;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fast.dht.DHTSnifferServer;
import com.fast.dht.conf.DHTServerConfig;
import com.fast.dht.db.dao.InfoHashDao;
import com.fast.dht.db.dao.TorrentDao;
import com.fast.dht.factory.MongodbFactory;
import com.fast.dht.impl.listen.DHTOnAnnouncePeerListener;
import com.fast.dht.impl.listen.DHTOnGetPeersListener;
import com.fast.dht.net.server.DHTServer;

public class DHTSnifferServerImpl extends DHTSnifferServer {

	private static final Logger LOG = Logger.getLogger(DHTSnifferServerImpl.class);

	// 配置服务
	private DHTServerConfig dhtServerConfig;
	// DHT具体实现
	DHTServer dhtServer = null;
	// 数据库操作持久对象
	public InfoHashDao infoHashDao;
	
	public TorrentDao torrentDao;

	/**
	 * 配置
	 * 
	 * @param dhtServerConfig
	 */
	public void config(DHTServerConfig dhtServerConfig) {
		this.dhtServerConfig = dhtServerConfig;
	}

	@Override
	public void start() {
		createMongodb();
		createDHTServer();
		this.dhtServer.start();
		LOG.info("DHT Sniffer Working");
	}

	@Override
	public void stop() {
		this.dhtServer.stop();
		LOG.info("DHT Sniffer Stop");
	}

	/**
	 * 创建数据库
	 */
	private void createMongodb() {
		// mongodb的工厂
		MongoDbFactory mongoDbFactory = MongodbFactory.mongoDbFactory(this.dhtServerConfig.getMongo());
		MongoTemplate mongoTemplate = MongodbFactory.mongoTemplate(mongoDbFactory);
		// GridFsTemplate gridFsTemplate =
		// MongodbFactory.gridFsTemplate(mongoDbFactory);
		infoHashDao = new InfoHashDao(mongoTemplate);
		torrentDao = new TorrentDao(mongoTemplate);
	}

	/**
	 * 创建DHT服务器务
	 */
	private void createDHTServer() {
		// 实例化DHT服务器
		this.dhtServer = new DHTServer("0.0.0.0", this.dhtServerConfig.getPort(), this.dhtServerConfig);
		
		// 设置监听
		this.dhtServer.setOnAnnouncePeerListener(new DHTOnAnnouncePeerListener(this));
		this.dhtServer.setOnGetPeersListener(new DHTOnGetPeersListener(this));

		// 设置初始节点
		this.dhtServer.setBootstrapNodes(getBootstrapNodes());
	}

	/**
	 * 获取启动节点
	 * 
	 * @return
	 */
	private List<InetSocketAddress> getBootstrapNodes() {
		List<InetSocketAddress> ips = new ArrayList<InetSocketAddress>();
		for (String node : this.dhtServerConfig.getNodes()) {
			String[] nodeHost = node.split(":");
			InetSocketAddress inetSocketAddress = null;
			if (nodeHost != null && nodeHost.length == 1) {
				inetSocketAddress = new InetSocketAddress(nodeHost[0], 6881);
			} else if (nodeHost.length > 1) {
				inetSocketAddress = new InetSocketAddress(nodeHost[0], Integer.parseInt(nodeHost[1]));
			}
			if (inetSocketAddress != null) {
				ips.add(inetSocketAddress);
			}
		}
		return ips;
	}

}
