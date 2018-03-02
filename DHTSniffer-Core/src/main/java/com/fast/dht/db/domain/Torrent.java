package com.fast.dht.db.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fast.dht.model.FileModel;

@Document
public class Torrent extends SuperEntity {

	// 种子创建时间
	@Indexed
	private long creationTime;

	// 种子的名称
	@Indexed
	private String name;

	// 文件列表
	private FileModel[] files;

	// 占用空间
	private long size;

	public long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FileModel[] getFiles() {
		return files;
	}

	public void setFiles(FileModel[] files) {
		this.files = files;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
