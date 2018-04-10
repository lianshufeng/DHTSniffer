package com.fast.dev.search.tags.adapters.conf;

/**
 * 标签模糊匹配
 * 
 * @作者 练书锋
 * @时间 2018年4月9日
 *
 *
 */
public class LikeTagConfig {

	// 匹配,多个用半角逗号分割
	private String likes;
	// 排序
	private int sort;

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
