package com.fast.dev.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.es.query.QueryRecord;
import com.fast.dev.es.query.QueryResult;
import com.fast.dev.search.dao.RecordDao;
import com.fast.dev.search.domain.Record;
import com.fast.dev.search.helper.YouDaoWordHelper;
import com.fast.dev.search.model.PushData;
import com.fast.dev.search.model.SearchRecord;
import com.fast.dev.search.model.SearchResult;
import com.fast.dev.search.util.FileTypeUtil;
import com.fast.dev.search.util.FormatUtil;

@Service
public class RecordService {

	@Autowired
	private RecordDao recordDao;

	@Autowired
	private YouDaoWordHelper wordHelper;

	// 线程池
	private static ExecutorService saveDataThreadPool = Executors.newFixedThreadPool(10);

	// 销毁线程池
	@PreDestroy
	private void shutdown() {
		saveDataThreadPool.shutdownNow();
	}

	/**
	 * 查询
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> page(String id) {
		return recordDao.get(id);
	}

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
	 * 保存数据
	 * 
	 * @param datas
	 */
	public Collection<String> save(Collection<PushData> datas) {
		final List<Record> records = new ArrayList<>();
		// 计次
		final CountDownLatch countDownLatch = new CountDownLatch(datas.size());
		for (final PushData data : datas) {
			// 启动线程池
			saveDataThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Record record = toRecord(data);
						if (record != null) {
							records.add(record);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						countDownLatch.countDown();
					}
				}
			});
		}
		// 等待执行完成
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this.recordDao.save(records);
	}

	/**
	 * 转换为实体记录
	 * 
	 * @param pushData
	 * @return
	 */
	private Record toRecord(PushData data) {
		// URL 为必填
		String url = data.getUrl();
		if (StringUtils.isEmpty(url)) {
			return null;
		}
		// 转换对象
		Record record = new Record();
		// 设置URL
		record.setUrl(url);
		// 处理标题
		setRecordTitle(record, data);
		// 设置发布时间
		setRecordTime(record, data);
		// 设置文件列表
		setRecordFiles(record, data);
		// 设置索引关键词
		setRecordIndex(record, data);
		return record;
	}

	/**
	 * 设置列表
	 * 
	 * @param record
	 * @param data
	 */
	private void setRecordFiles(final Record record, final PushData data) {
		record.setFiles(data.getFiles());
	}

	/**
	 * 设置索引
	 * 
	 * @param record
	 * @param data
	 */
	private void setRecordIndex(final Record record, final PushData data) {
		List<String> indexNames = new ArrayList<>();
		// 标题
		indexNames.add(toIndexNames(record.getTitle()));
		// URL
		indexNames.add(toIndexNames(FilenameUtils.getBaseName(data.getUrl())));
		// 文件列表
		if (data.getFiles() != null) {
			for (String filePath : data.getFiles().keySet()) {
				indexNames.add(toIndexNames(FilenameUtils.getBaseName(filePath)));
			}
		}
		// 转换为索引名
		StringBuilder sb = new StringBuilder();
		for (String indexName : indexNames) {
			sb.append(indexName + " ");
		}
		record.setIndex(sb.toString());
	}

	/**
	 * 分词规则,可调用翻译模块
	 * 
	 * @return
	 */
	private String toIndexNames(final String name) {
		return this.wordHelper.translation(name);
	}

	/**
	 * 设置记录时间
	 * 
	 * @param record
	 * @param data
	 */
	private void setRecordTime(final Record record, final PushData data) {
		// 发布时间
		record.setPublishTime(data.getTime() == null ? 0 : data.getTime());
		// 收藏时间
		record.setCreateTime(System.currentTimeMillis());
	}

	/**
	 * 规则:若标题为空,则取URL的文件名作为标题
	 * 
	 * @param record
	 * @param data
	 */
	private void setRecordTitle(final Record record, final PushData data) {
		String title = data.getTitle();
		if (!StringUtils.isEmpty(title)) {
			record.setTitle(title);
		} else {
			// 如果标题为空则用URL中的文件名作为标题
			String url = data.getUrl();
			record.setTitle(FilenameUtils.getBaseName(url));
		}
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

	public static void main(String[] args) throws Exception {
		PushData data = new PushData("Black.Mirror.3x02.Giochi.Pericolosi.ITA.ENG.720p.WEBMux.x264-Speranzah.mkv",
				"http://baidu.com/hao123.txt", null, null);
		System.out.println(JsonUtil.toJson(new RecordService().toRecord(data)));
	}

}
