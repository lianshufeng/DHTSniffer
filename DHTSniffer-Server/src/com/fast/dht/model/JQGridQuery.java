package com.fast.dht.model;

/**
 * 查询模型
 * 
 * @作者 练书锋
 * @时间 2018年3月1日
 *
 *
 */
public class JQGridQuery {
	// 页码
	private int page;
	// 每页显示数量
	private int rows;
	private String sord;
	private String sidx;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSord() {
		return sord;
	}

	public void setSord(String sord) {
		this.sord = sord;
	}

	public String getSidx() {
		return sidx;
	}

	public void setSidx(String sidx) {
		this.sidx = sidx;
	}

	public JQGridQuery(int page, int rows, String sord, String sidx) {
		super();
		this.page = page;
		this.rows = rows;
		this.sord = sord;
		this.sidx = sidx;
	}

	public JQGridQuery() {
		// TODO Auto-generated constructor stub
	}

}
