package com.fast.dev.search.util;

import java.util.regex.Pattern;

/**
 * 中文工具
 * 
 * @作者 练书锋
 * @时间 2018年4月9日
 *
 *
 */
public class ChineseUtil {
	/**
	 * 匹配格式
	 */
	private final static Pattern PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

	/**
	 * 是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContain(String str) {
		return PATTERN.matcher(str).find();
	}

}
