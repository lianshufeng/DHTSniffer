package com.fast.dht.db.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @作者 练书锋
 * @时间 2018年2月28日
 *
 *
 */
@Document
public class InfoHash extends SuperEntity {

	// 访问次数
	@Indexed
	private int accessCount;

	// 更新信息的主机
	@Indexed
	private String updateHost;

	// 获取总次数
	@Indexed
	private long getInfoCount;

	public int getAccessCount() {
		return accessCount;
	}

	public void setAccessCount(int accessCount) {
		this.accessCount = accessCount;
	}

	public String getUpdateHost() {
		return updateHost;
	}

	public void setUpdateHost(String updateHost) {
		this.updateHost = updateHost;
	}

	public long getGetInfoCount() {
		return getInfoCount;
	}

	public void setGetInfoCount(long getInfoCount) {
		this.getInfoCount = getInfoCount;
	}

}
