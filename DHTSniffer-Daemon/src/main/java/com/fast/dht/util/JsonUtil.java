package com.fast.dht.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具
 * 
 * @作者 练书锋
 * @联系 oneday@vip.qq.com
 * @时间 2014年5月17日
 */
public class JsonUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 转换到json字符串
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object object) throws Exception {
		return objectMapper.writeValueAsString(object);
	}

	/**
	 * 转换为对象
	 * 
	 * @param json
	 * @param cls
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject(String json, Class<T> cls) throws Exception {
		return objectMapper.readValue(json, cls);
	}

}
