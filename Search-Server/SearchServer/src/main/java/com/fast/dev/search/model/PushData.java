package com.fast.dev.search.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 推送的数据实体,URL必填
 * 
 * @作者 练书锋
 * @时间 2018年3月18日
 *
 *
 */
public class PushData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 标题
	String title;
	// 下载地址,必填！！
	String url;
	// 发布时间
	Long time;
	// 文件列表
	Map<String, Long> files;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Map<String, Long> getFiles() {
		return files;
	}

	public void setFiles(Map<String, Long> files) {
		this.files = files;
	}

	public PushData(String title, String url, Long time, Map<String, Long> files) {
		super();
		this.title = title;
		this.url = url;
		this.time = time;
		this.files = files;
	}

	public PushData() {
		// TODO Auto-generated constructor stub
	}
}
