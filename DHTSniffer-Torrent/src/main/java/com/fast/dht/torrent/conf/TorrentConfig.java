package com.fast.dht.torrent.conf;

import java.io.Serializable;

import com.fast.dht.conf.MongodbConfig;

public class TorrentConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// mongodb的配置
	private MongodbConfig mongo;

	// 最大同时并发数量
	private int maxRunTaskCount = 10;

	// 任务完成，休眠时间
	private int taskSleepTime = 1000;

	// 最低需要节点数量就能工作
	private int minNodesCount = 10;

	// 任务超时时间
	private long taskTimeout = 60000;

	public MongodbConfig getMongo() {
		return mongo;
	}

	public void setMongo(MongodbConfig mongo) {
		this.mongo = mongo;
	}

	public int getMaxRunTaskCount() {
		return maxRunTaskCount;
	}

	public void setMaxRunTaskCount(int maxRunTaskCount) {
		this.maxRunTaskCount = maxRunTaskCount;
	}

	public int getTaskSleepTime() {
		return taskSleepTime;
	}

	public void setTaskSleepTime(int taskSleepTime) {
		this.taskSleepTime = taskSleepTime;
	}

	public int getMinNodesCount() {
		return minNodesCount;
	}

	public void setMinNodesCount(int minNodesCount) {
		this.minNodesCount = minNodesCount;
	}

	public long getTaskTimeout() {
		return taskTimeout;
	}

	public void setTaskTimeout(long taskTimeout) {
		this.taskTimeout = taskTimeout;
	}

}
