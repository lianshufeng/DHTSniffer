package com.fast.dht.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fast.dht.dao.DataDao;
import com.fast.dht.db.domain.InfoHash;
import com.fast.dht.model.JQGridQuery;
import com.fast.dht.model.JQGridResult;
import com.fast.dht.util.ServletUtil;

/**
 * Servlet implementation class QueryInfoHash
 */
@WebServlet("/query/infohash")
public class QueryInfoHash extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryInfoHash() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		action(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void action(HttpServletRequest request, HttpServletResponse response) {
		JQGridQuery jqGridQuery = ServletUtil.getQueryModel(request);
		JQGridResult jqGridResult = DataDao.list(InfoHash.class, jqGridQuery);
		try {
			// 数据清洗与过滤
			cleanData(jqGridResult);
			ServletUtil.sendResult(request, response, jqGridResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 数据清洗
	 */
	private static void cleanData(JQGridResult jqGridResult) {
		Object[] rows = jqGridResult.getRows();
		if (rows == null || rows.length == 0) {
			return;
		}
		for (int i = 0; i < rows.length; i++) {
			// 取出数据
			InfoHash infoHash = (InfoHash) rows[i];
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("createTime", ServletUtil.formatTime(infoHash.getCreateTime()));
			data.put("hash", infoHash.getHash());
			data.put("updateHost", infoHash.getUpdateHost());
			data.put("accessCount", infoHash.getAccessCount());
			rows[i] = data;
		}

	}

}
