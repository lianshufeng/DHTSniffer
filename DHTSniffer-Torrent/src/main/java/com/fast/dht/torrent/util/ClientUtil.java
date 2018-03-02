package com.fast.dht.torrent.util;

import java.util.UUID;

public class ClientUtil {

	/**
	 * 创建客户端的UUID
	 * 
	 * @return
	 */
	public static String build() {
		return UUID.randomUUID().toString();
	}
}
