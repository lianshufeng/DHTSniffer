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
import com.fast.dht.db.domain.Torrent;
import com.fast.dht.model.JQGridQuery;
import com.fast.dht.model.JQGridResult;
import com.fast.dht.net.model.Info;
import com.fast.dht.util.JsonUtil;
import com.fast.dht.util.ServletUtil;

/**
 * Servlet implementation class QueryTorrent
 */
@WebServlet("/query/torrent")
public class QueryTorrent extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryTorrent() {
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
		JQGridResult jqGridResult = DataDao.list(Torrent.class, jqGridQuery);
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
			Torrent torrent = (Torrent) rows[i];
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("createTime", ServletUtil.formatTime(torrent.getCreateTime()));
			data.put("hash", torrent.getHash());
			data.put("type", torrent.getType());
			data.put("accessCount", torrent.getAccessCount());
			Info info = torrent.getInfo();
			if (info != null) {
				data.put("name", info.getName());
				try {
					data.put("files", JsonUtil.toJson(info.getFiles()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				data.put("length", ServletUtil.formatSize(info.getLength()));
			}

			rows[i] = data;
		}

	}

}
