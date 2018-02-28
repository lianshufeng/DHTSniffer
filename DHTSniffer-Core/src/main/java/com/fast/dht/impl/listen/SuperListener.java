package com.fast.dht.impl.listen;

import com.fast.dht.db.dao.InfoHashDao;
import com.fast.dht.db.dao.TorrentDao;
import com.fast.dht.impl.DHTSnifferServerImpl;

/**
 * 监听器的父类
 * 
 * @作者 练书锋
 * @时间 2018年2月28日
 *
 *
 */
public abstract class SuperListener {

	// 服务器的实现对象
	protected DHTSnifferServerImpl dhtSnifferServerImpl;

	// 种子数据的Dao
	protected InfoHashDao infoHashDao;
	protected TorrentDao torrentDao;

	/**
	 * 构造方法
	 * 
	 * @param dhtSnifferServerImpl
	 */
	public SuperListener(DHTSnifferServerImpl dhtSnifferServerImpl) {
		this.dhtSnifferServerImpl = dhtSnifferServerImpl;
		this.infoHashDao = dhtSnifferServerImpl.infoHashDao;
		this.torrentDao = dhtSnifferServerImpl.torrentDao;
	}

}
