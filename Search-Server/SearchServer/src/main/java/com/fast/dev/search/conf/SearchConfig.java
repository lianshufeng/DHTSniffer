package com.fast.dev.search.conf;

import com.fast.dev.es.conf.ESConfig;

/**
 * 继承ES默认的配置
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月16日
 *
 */
public class SearchConfig extends ESConfig {

	private String db;

	/**
	 * @return the db
	 */
	public String getDb() {
		return db;
	}

	/**
	 * @param db
	 *            the db to set
	 */
	public void setDb(String db) {
		this.db = db;
	}

}
