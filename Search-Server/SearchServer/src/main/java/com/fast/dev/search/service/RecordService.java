package com.fast.dev.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fast.dev.es.query.QueryRecord;
import com.fast.dev.es.query.QueryResult;
import com.fast.dev.search.dao.RecordDao;
import com.fast.dev.search.model.SearchRecord;
import com.fast.dev.search.model.SearchResult;
import com.fast.dev.search.util.FileTypeUtil;
import com.fast.dev.search.util.FormatUtil;

@Service
public class RecordService {

	@Autowired
	private RecordDao recordDao;

	/**
	 * 搜索
	 * 
	 * @param wd
	 * @param page
	 * @param size
	 */
	public SearchResult search(String wd, int page, int size, String preTag, String postTag) {
		// 开始记录数
		int from = (page - 1) * size;
		QueryResult queryResult = this.recordDao.search(wd, from, size, preTag, postTag);
		// 数据转换到视图层
		return toSearchResult(queryResult);
	}

	/**
	 * 查询结果集转换为视图
	 * 
	 * @param queryResult
	 * @return
	 */
	private static SearchResult toSearchResult(final QueryResult queryResult) {
		SearchResult searchResult = new SearchResult();
		searchResult.setTotal(queryResult.getTotal());
		List<SearchRecord> records = new ArrayList<>();
		for (QueryRecord queryRecord : queryResult.getRecords()) {
			records.add(toSearchRecord(queryRecord));
		}
		searchResult.setDatas(records);
		return searchResult;
	}

	/**
	 * 把查询到的结果转换到视图层
	 * 
	 * @param queryRecord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static SearchRecord toSearchRecord(final QueryRecord queryRecord) {
		Map<String, Object> source = queryRecord.getSource();
		Map<String, Collection<String>> highLight = queryRecord.getHighLight();

		SearchRecord searchRecord = new SearchRecord();
		searchRecord.setId(queryRecord.getId());
		// 标题
		searchRecord.setTitle(String.valueOf(highLight.get("title").toArray()[0]));

		// 文件总长度
		if (source.containsKey("totalSize")) {
			long totalSize = Long.valueOf(String.valueOf(source.get("totalSize")));
			searchRecord.setSize(FormatUtil.formatSize(totalSize));
		}

		// 文件创建时间
		if (source.containsKey("createTime")) {
			long createTime = Long.valueOf(String.valueOf(source.get("createTime")));
			searchRecord.setTime(FormatUtil.formatTime(createTime));
		}

		// 文件数量
		if (source.containsKey("files")) {
			Map<String, Long> files = (Map<String, Long>) source.get("files");
			searchRecord.setFileCount(files.size());
			List<String> fileTypes = new ArrayList<>();
			// 取出有哪些文件类型
			for (String fileName : files.keySet()) {
				String fileType = FileTypeUtil.query(fileName);
				if (fileType != null && !fileTypes.contains(fileType)) {
					fileTypes.add(fileType);
				}
			}
			searchRecord.setType(fileTypes.toArray(new String[0]));
		} else {
			searchRecord.setFileCount(0);
		}
		return searchRecord;
	}

}
