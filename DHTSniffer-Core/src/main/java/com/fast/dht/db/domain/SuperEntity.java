package com.fast.dht.db.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public abstract class SuperEntity {

	// 主键
	@Id
	private String id;

	// 创建时间
	@Indexed
	private long createTime;

	// 更新时间
	@Indexed
	private long updateTime;

	@Indexed(unique = true)
	private String hash;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
