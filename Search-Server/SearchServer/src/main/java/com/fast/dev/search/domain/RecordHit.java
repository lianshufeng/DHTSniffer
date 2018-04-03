package com.fast.dev.search.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fast.dev.component.mongodb.dao.domain.SuperEntity;

@Document
public class RecordHit extends SuperEntity {

	// 计次
	private long hit;

	// 是否已更新
	@Indexed
	private boolean update;

	public long getHit() {
		return hit;
	}

	public void setHit(long hit) {
		this.hit = hit;
	}

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

}
