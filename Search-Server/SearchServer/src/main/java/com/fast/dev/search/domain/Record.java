package com.fast.dev.search.domain;

import java.util.Map;

/**
 * 记录
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月16日
 *
 */
public class Record {
	// 标题
	private String title;
	// 索引关键词
	private String index;
	// 原始下载地址
	private String url;
	// 文件列表
	private Map<String, Long> files;
	// 文件尺寸
	private long totalSize;
	// 创建时间
	private long createTime;
	// 当前数据点击次数
	private long hitCount;

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
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the files
	 */
	public Map<String, Long> getFiles() {
		return files;
	}

	/**
	 * @param files
	 *            the files to set
	 */
	public void setFiles(Map<String, Long> files) {
		this.files = files;
	}

	/**
	 * @return the totalSize
	 */
	public long getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize
	 *            the totalSize to set
	 */
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the hitCount
	 */
	public long getHitCount() {
		return hitCount;
	}

	/**
	 * @param hitCount
	 *            the hitCount to set
	 */
	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}

}
