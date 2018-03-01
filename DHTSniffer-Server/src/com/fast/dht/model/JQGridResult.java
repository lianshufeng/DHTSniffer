package com.fast.dht.model;

/**
 * jqgrid返回的结果集
 * 
 * @作者 练书锋
 * @时间 2018年3月1日
 *
 *
 */
public class JQGridResult {
	// 总记录数
	private long records;
	// 当前页面
	private int page;
	// 总页数
	private int total;
	// 具体数据
	private Object[] rows;

	public long getRecords() {
		return records;
	}

	public void setRecords(long records) {
		this.records = records;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Object[] getRows() {
		return rows;
	}

	public void setRows(Object[] rows) {
		this.rows = rows;
	}

}
