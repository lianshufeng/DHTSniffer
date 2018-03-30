package com.fast.dev.search.util;

import org.springframework.util.Base64Utils;

/**
 * 常用下载工具的URL编码
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月30日
 *
 */
public class DownLoadUrlEncode {

	/**
	 * 迅雷
	 * 
	 * @param url
	 * @return
	 */
	public static String xunlei(String url) {
		return "thunder://" + Base64Utils.encodeToString(("AA" + url + "ZZ").getBytes());
	}

	/**
	 * QQ旋风
	 * 
	 * @param url
	 * @return
	 */
	public static String xuanfeng(String url) {
		return "qqdl://" + Base64Utils.encodeToString(url.getBytes());
	}

	/**
	 * 快车
	 * 
	 * @param url
	 * @return
	 */
	public static String kuaiche(String url) {
		return "Flashget://" + Base64Utils.encodeToString(("[FLASHGET]" + url + "[FLASHGET]").getBytes());
	}


}
