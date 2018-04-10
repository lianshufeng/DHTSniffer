package com.fast.dev.search.tags.adapters;

import java.util.Collection;
import java.util.Map;

/**
 * 标签适配器
 * 
 * @作者 练书锋
 * @时间 2018年4月9日
 *
 *
 */
public abstract class TagAdapter {

	/**
	 * 进行匹配并返回匹配成功的标签
	 * 
	 * @param values
	 * @return
	 */
	public abstract Collection<String> match(String... values);

	/**
	 * 标签名称
	 */
	public abstract Map<String, Integer> tagOrders();

}
