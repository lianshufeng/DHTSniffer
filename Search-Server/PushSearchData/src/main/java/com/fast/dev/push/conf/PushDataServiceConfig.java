package com.fast.dev.push.conf;

public class PushDataServiceConfig {

	// 表名
	private String collectionName;

	// mongodb配置
	private MongodbConfig mongo;

	// 执行代码
	private String className;

	// 表达式
	private String corn;

	// 每次读取数据量
	private int readSize = 10;

	/**
	 * @return the mongo
	 */
	public MongodbConfig getMongo() {
		return mongo;
	}

	/**
	 * @param mongo
	 *            the mongo to set
	 */
	public void setMongo(MongodbConfig mongo) {
		this.mongo = mongo;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the corn
	 */
	public String getCorn() {
		return corn;
	}

	/**
	 * @param corn
	 *            the corn to set
	 */
	public void setCorn(String corn) {
		this.corn = corn;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public int getReadSize() {
		return readSize;
	}

	public void setReadSize(int readSize) {
		this.readSize = readSize;
	}

	public PushDataServiceConfig() {
		// TODO Auto-generated constructor stub
	}

}
