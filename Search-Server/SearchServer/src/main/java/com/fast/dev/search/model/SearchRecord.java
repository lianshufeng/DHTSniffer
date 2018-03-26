package com.fast.dev.search.model;

public class SearchRecord {
	// 记录ID
	private String id;
	// 文件类型
	private String[] type;
	// 文件总大小
	private String size;
	// 文件总数量
	private int fileCount;
	// 收录时间
	private String time;
	// 标题
	private String title;
	// 相关性
	private String like;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String[] getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String[] type) {
		this.type = type;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @return the fileCount
	 */
	public int getFileCount() {
		return fileCount;
	}

	/**
	 * @param fileCount
	 *            the fileCount to set
	 */
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the like
	 */
	public String getLike() {
		return like;
	}

	/**
	 * @param like
	 *            the like to set
	 */
	public void setLike(String like) {
		this.like = like;
	}

	// id: 1,
	// types: ['压缩', '视频'],
	// title: 'test1',
	// size: '12.4G',
	// fileCount: 25,
	// time: '2017-09-06'
}
