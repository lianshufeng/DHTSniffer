package com.fast.dev.search.tags.adapters.bean;

/**
 * 标签模糊匹配
 * 
 * @作者 练书锋
 * @时间 2018年4月9日
 *
 *
 */
public class LikeTag {
	// 名称
	private String[] names;
	// 匹配,多个用半角逗号分割
	private String[] likes;
	// 排序
	private int sort;

	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public String[] getLikes() {
		return likes;
	}

	public void setLikes(String[] likes) {
		this.likes = likes;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public LikeTag(String[] names, String[] likes, int sort) {
		super();
		this.names = names;
		this.likes = likes;
		this.sort = sort;
	}

	public LikeTag() {
		// TODO Auto-generated constructor stub
	}

}
