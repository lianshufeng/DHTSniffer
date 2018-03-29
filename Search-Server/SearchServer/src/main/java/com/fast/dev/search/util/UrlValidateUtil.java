package com.fast.dev.search.util;

public class UrlValidateUtil {

	private static final String MagnetTag = "magnet:?";

	/**
	 * 检查URL合法性
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validate(String url) {
		// 只判断磁力
		if (url.length() > MagnetTag.length() && url.substring(0, MagnetTag.length()).equalsIgnoreCase(MagnetTag)) {
			return validateMagnet(url);
		}
		return true;
	}

	/**
	 * 校验磁力
	 * 
	 * @param url
	 * @return
	 */
	public static boolean validateMagnet(String url) {
		String magnetInfo = url.substring(MagnetTag.length(), url.length());
		for (String uri : magnetInfo.split("&")) {
			String[] items = uri.split("=");
			if (items != null && items.length > 1) {
				if (items[0].equalsIgnoreCase("xt")) {
					String[] xts = StringSplit.split(items[1]);
					return xts[xts.length - 1].length() == 40;
				}
			}

		}

		return false;
	}

}
