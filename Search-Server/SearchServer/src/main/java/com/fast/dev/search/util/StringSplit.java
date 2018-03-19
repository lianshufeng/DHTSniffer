package com.fast.dev.search.util;

import java.util.Collection;

/**
 * 字符串分割
 * 
 * @作者 练书锋
 * @时间 2018年3月18日
 *
 *
 */
public class StringSplit {

	private static final String[] splits = new String[] { " ", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_",
			"+", "\\-", "=", "！", "@", "#", "￥", "%", "……", "&", "*", "（", "）", "——", "+", "\\[", "\\]", ";", "'", "\\",
			",", ".", "/", "\\{", "\\}", "|", ":", "\"", "<", ">", "?", "【", "】", "|", "：", "“", "《", "》", "？", "`",
			"~", "·", "，", "。", "、", "’", "；", "｛", "｝", "\t", "\n", "๑ ", "〓", "｜", "◆", "", "", "", "", ""

	};

	private static final String str = getSplitStr();

	/**
	 * 获取分割字符串
	 * 
	 * @return
	 */
	private static String getSplitStr() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String sp : splits) {
			sb.append(sp + " ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 分割除了符号意外的字符串
	 * 
	 * @param source
	 * @return
	 */
	public static String[] split(final String source) {
		if (source == null) {
			return null;
		}
		return source.split(str);
	}

	/**
	 * 合并数组
	 * 
	 * @param spaceStr
	 * @param strArray
	 * @return
	 */
	public static String join(String spaceStr, Collection<String> list) {
		StringBuilder sb = new StringBuilder();
		for (String str : list) {
			sb.append(str);
			sb.append(spaceStr);
		}
		return sb.toString();
	}

}
