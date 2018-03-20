package com.fast.dev.search.model;

/**
 * 文件模型
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月20日
 *
 */
public class FileModel {

	private String path;
	private long size;

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	public FileModel(String path, long size) {
		super();
		this.path = path;
		this.size = size;
	}

	public FileModel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return String.format("{\"path\" : \"%s\" , \"size\" : %s }", path, size);
	}
}
