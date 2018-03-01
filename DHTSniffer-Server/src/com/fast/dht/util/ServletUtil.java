package com.fast.dht.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fast.dht.model.JQGridQuery;
import com.fast.dht.model.JQGridResult;

/**
 * 工具
 * 
 * @作者 练书锋
 * @时间 2018年3月1日
 *
 *
 */
public class ServletUtil {
	final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	/**
	 * 获取分页查询模型
	 * 
	 * @param request
	 * @return
	 */
	public static JQGridQuery getQueryModel(HttpServletRequest request) {
		int page = Integer.parseInt(request.getParameter("page"));
		int rows = Integer.parseInt(request.getParameter("rows"));
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		return new JQGridQuery(page, rows, sord, sidx);
	}

	/**
	 * 发送结果集合
	 * 
	 * @param response
	 * @throws Exception
	 */
	public static void sendResult(HttpServletRequest request, HttpServletResponse response, JQGridResult jqGridResult)
			throws Exception {
		ResponseUtil.write(request, response, jqGridResult);
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

	/**
	 * 格式化时间
	 * 
	 * @param time
	 * @return
	 */
	public static String formatSize(long size) {
		double val = (double) size / 1024 / 1024 / 1024;
		return new BigDecimal(val).setScale(2, BigDecimal.ROUND_HALF_UP) + " GB";
	}

}
