package com.fast.dht.model;

/**
 * 文件模型
 * 
 * @作者 练书锋
 * @时间 2018年3月3日
 *
 *
 */
public class FileModel {

	private String path;
	private long size;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public FileModel() {
		// TODO Auto-generated constructor stub
	}

	public FileModel(String path, long size) {
		super();
		this.path = path;
		this.size = size;
	}

}
