package com.fast.dev.search.tags;

import java.util.Set;

/**
 * 标签管理器
 * 
 * @作者 练书锋
 * @时间 2018年4月9日
 *
 *
 */
public interface TagManager {

	/**
	 * 通过匹配数据返回有效的标签
	 * 
	 * @param values
	 * @return
	 */
	public Set<String> match(String... values);

	/**
	 * 获取所有的标签
	 * 
	 * @return
	 */
	public String[] tags();

}
