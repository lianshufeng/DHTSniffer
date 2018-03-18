package com.fast.dev.search.util;

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
			"~", "·", "，", "。", "、", "’", "；", "｛", "｝", "\t", "\n", "", "", "", "", "", "", "", "", ""

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

}
