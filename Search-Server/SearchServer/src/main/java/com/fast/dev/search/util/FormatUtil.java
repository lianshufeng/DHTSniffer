package com.fast.dev.search.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {

	final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	/**
	 * 格式尺寸
	 * 
	 * @param time
	 * @return
	 */
	public static String formatSize(long size) {
		if (size <= 0) {
			return null;
		}
		double val = (double) size / 1024 / 1024 / 1024;
		return new BigDecimal(val).setScale(3, BigDecimal.ROUND_HALF_UP) + " GB";
	}

	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @return
	 */
	public static String formatTime(long time) {
		return dateFormat.format(new Date(time));
	}
}
