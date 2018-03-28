package com.fast.dev.search.model;

import java.io.Serializable;
import java.util.Collection;

/**
 * 搜索结果集
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月16日
 *
 */
public class SearchResult implements Serializable{

	private static final long serialVersionUID = 1L;
	// 总数量量
	private long total;
	// 当前数据
	private Collection<SearchRecord> datas;

	/**
	 * @return the total
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(long total) {
		this.total = total;
	}

	/**
	 * @return the datas
	 */
	public Collection<SearchRecord> getDatas() {
		return datas;
	}

	/**
	 * @param datas
	 *            the datas to set
	 */
	public void setDatas(Collection<SearchRecord> datas) {
		this.datas = datas;
	}

}
