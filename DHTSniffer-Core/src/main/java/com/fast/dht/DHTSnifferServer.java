package com.fast.dht;

import com.fast.dht.conf.DHTServerConfig;
import com.fast.dht.impl.DHTSnifferServerImpl;

public abstract class DHTSnifferServer {

	/**
	 * 构造服务器对象
	 * 
	 * @return
	 */
	public static DHTSnifferServer build(DHTServerConfig dhtServerConfig) {
		DHTSnifferServerImpl dhtSnifferServer = new DHTSnifferServerImpl();
		dhtSnifferServer.config(dhtServerConfig);
		return dhtSnifferServer;
	}

	/**
	 * 开启嗅探服务
	 */
	public abstract void start();

	/**
	 * 关闭嗅探服务
	 */
	public abstract void stop();

}
