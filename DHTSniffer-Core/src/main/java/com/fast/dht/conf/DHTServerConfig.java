package com.fast.dht.conf;

import java.io.Serializable;

/**
 * DHT 服务的配置
 * 
 * @作者 练书锋
 * @时间 2018年2月27日
 *
 *
 */
public class DHTServerConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// mongodb的配置
	private MongodbConfig mongo;

	// 默认的路由节点点
	private String[] nodes = new String[] { "router.bittorrent.com:6881", "dht.transmissionbt.com:6881",
			"router.utorrent.com:6881", "router.bitcomet.com:6881", "dht.aelitis.com:6881" };

	// 最大的优秀节点数量
	private int maxGoodNodeCount = 2000;

	// 最大并行找节点数
	private int maxRunFindNodeCount = 10;

	// 默认端口
	private int port = 6882;

	public MongodbConfig getMongo() {
		return mongo;
	}

	public void setMongo(MongodbConfig mongo) {
		this.mongo = mongo;
	}

	public String[] getNodes() {
		return nodes;
	}

	public void setNodes(String[] nodes) {
		this.nodes = nodes;
	}

	public int getMaxGoodNodeCount() {
		return maxGoodNodeCount;
	}

	public void setMaxGoodNodeCount(int maxGoodNodeCount) {
		this.maxGoodNodeCount = maxGoodNodeCount;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMaxRunFindNodeCount() {
		return maxRunFindNodeCount;
	}

	public void setMaxRunFindNodeCount(int maxRunFindNodeCount) {
		this.maxRunFindNodeCount = maxRunFindNodeCount;
	}

	public DHTServerConfig() {
		// TODO Auto-generated constructor stub
	}

}
