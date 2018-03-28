package com.fast.dev.search.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 热词实体
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月28日
 *
 */
public class HotWord implements Comparable<HotWord>,Serializable {

	private static final long serialVersionUID = 1L;

	@Indexed(unique = true)
	private String name;

	@Indexed
	private long hit;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the hit
	 */
	public long getHit() {
		return hit;
	}

	/**
	 * @param hit
	 *            the hit to set
	 */
	public void setHit(long hit) {
		this.hit = hit;
	}

	@Override
	public int compareTo(HotWord hotWords) {
		return Integer.parseInt(String.valueOf((hotWords.getHit() - this.hit)));
	}

	public HotWord() {
		// TODO Auto-generated constructor stub
	}

	public HotWord(String name, long hit) {
		super();
		this.name = name;
		this.hit = hit;
	}

}
