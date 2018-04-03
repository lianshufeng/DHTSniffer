package com.fast.dev.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.PreDestroy;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fast.dev.component.cachemethod.annotations.CacheMethod;
import com.fast.dev.component.cachemethod.annotations.CacheParameter;
import com.fast.dev.es.query.QueryRecord;
import com.fast.dev.es.query.QueryResult;
import com.fast.dev.search.dao.HotWordsDao;
import com.fast.dev.search.dao.PushDataCacheDao;
import com.fast.dev.search.dao.RecordDao;
import com.fast.dev.search.dao.RecordInfoDao;
import com.fast.dev.search.domain.HotWord;
import com.fast.dev.search.domain.Record;
import com.fast.dev.search.domain.RecordInfo;
import com.fast.dev.search.model.FileModel;
import com.fast.dev.search.model.PushData;
import com.fast.dev.search.model.SearchRecord;
import com.fast.dev.search.model.SearchResult;
import com.fast.dev.search.util.FileTypeUtil;
import com.fast.dev.search.util.FormatUtil;
import com.fast.dev.search.util.PinyinTool;
import com.fast.dev.search.util.StringSplit;
import com.fast.dev.search.util.UrlValidateUtil;

@Service
public class RecordService {
	private static final Logger LOG = Logger.getLogger(RecordService.class);

	@Autowired
	private RecordDao recordDao;

	@Autowired
	private RecordInfoDao recordInfoDao;

	@Autowired
	private PushDataCacheDao dataCacheDao;

	@Autowired
	private HotWordsDao hotWordsDao;

	// @Autowired
	// private YouDaoWordHelper wordHelper;
	// 线程池
	private ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(20);

	@PreDestroy
	private void shutdown() {
		this.threadPool.shutdownNow();
	}

	/**
	 * 查询
	 * 
	 * @param id
	 * @return
	 */
	public RecordInfo page(String id) {
		return this.recordInfoDao.get(id);
	}

	/**
	 * 搜索
	 * 
	 * @param wd
	 * @param page
	 * @param size
	 */
	@CacheMethod(collectionName = "Record_search", overflowToDisk = true, maxMemoryCount = 100, timeToIdleSeconds = 300, timeToLiveSeconds = 300)
	public SearchResult search(@CacheParameter String wd, @CacheParameter Integer page, @CacheParameter Integer size,
			@CacheParameter String preTag, @CacheParameter String postTag) {
		// 开始记录数
		int from = (page - 1) * size;
		QueryResult queryResult = this.recordDao.search(wd, from, size, preTag, postTag);
		// 数据转换到视图层
		return toSearchResult(queryResult, wd, preTag, postTag);
	}

	/**
	 * 取热词
	 * 
	 * @param maxSize
	 * @param day
	 * @return
	 */
	@CacheMethod(collectionName = "Record_getHotWords", overflowToDisk = true, maxMemoryCount = 100, timeToIdleSeconds = 600, timeToLiveSeconds = 600)
	public List<HotWord> getHotWords(Integer maxSize, Integer day) {
		return this.hotWordsDao.list(maxSize, day);
	}

	/**
	 * 保存数据
	 * 
	 * @param datas
	 */
	public Collection<String> save(Map<String, PushData> datas) {
		LOG.info(String.format("save : [%s]", datas.size()));
		Map<String, Record> records = new LinkedHashMap<>();
		for (Entry<String, PushData> entry : datas.entrySet()) {
			// 去除重复
			if (this.recordInfoDao.urlExits(entry.getValue().getUrl())) {
				// 跳过重复，并设置该任务为完成状态
				LOG.info(String.format("Skip Exits : [%s]", entry.getValue().getUrl()));
				this.dataCacheDao.finishCache(entry.getKey());
			} else {
				PushData pushData = entry.getValue();
				if (!UrlValidateUtil.validate(pushData.getUrl())) {
					LOG.info(String.format("Url Error : [%s]", pushData.getUrl()));
					this.dataCacheDao.finishCache(entry.getKey());
					continue;
				}

				// 需要先保存到本地mongodb里
				Record record = toRecord(pushData);
				if (record != null) {
					records.put(entry.getKey(), record);
				}
			}
		}
		List<Record> saveDatas = new ArrayList<>(records.values());
		try {
			LinkedHashMap<String, String> result = this.recordDao.save(saveDatas);
			if (result != null && result.size() > 0) {
				this.dataCacheDao.finishCache(records.keySet().toArray(new String[0]));
			}
			return result == null ? null : result.keySet();
		} catch (Exception e) {
			// 异常则删除记录信息
			List<String> ids = new ArrayList<>();
			for (Record record : records.values()) {
				ids.add(record.getRef());
			}
			this.recordInfoDao.remove(ids.toArray(new String[0]));
			throw e;
		}
	}

	/**
	 * 记录热词
	 * 
	 * @param wd
	 */
	public void hitHotWord(final String words) {
		if (StringUtils.isEmpty(words)) {
			return;
		}
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				hotWordsDao.hit(1, StringSplit.split(words));
			}
		});
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
		RecordInfo recordInfo = new RecordInfo();
		// 设置URL
		recordInfo.setUrl(url);
		// 处理标题
		setRecordTitle(recordInfo, data);
		// 设置发布时间
		setRecordTime(recordInfo, data);
		// 设置文件列表
		setRecordFiles(recordInfo, data);
		// 设置索引关键词
		setRecordIndex(recordInfo, data);
		// 先入本地库
		this.recordInfoDao.save(recordInfo);
		// 拷贝对象
		Record record = new Record();
		BeanUtils.copyProperties(recordInfo, record);
		// 保留需要关联的id
		record.setRef(recordInfo.getId());
		return record;
	}

	/**
	 * 设置列表
	 * 
	 * @param record
	 * @param data
	 */
	private void setRecordFiles(final RecordInfo record, final PushData data) {
		// 文件列表
		if (data.getFiles() != null) {
			Collection<FileModel> files = new ArrayList<>();
			for (Entry<String, Long> entry : data.getFiles().entrySet()) {
				files.add(new FileModel(entry.getKey(), entry.getValue()));
			}
			record.setFiles(files);
		}
		// 设置文件总长度
		if (data.getFiles() != null) {
			long totalSize = 0;
			Set<String> fileTypes = new HashSet<>();
			for (Entry<String, Long> file : data.getFiles().entrySet()) {
				totalSize += file.getValue();
				fileTypes.add(FilenameUtils.getExtension(file.getKey()));
			}
			// 文件类型
			record.setFileType(fileTypes);
			/// 文件长度
			record.setTotalSize(totalSize);
			// 文件总量
			record.setFileCount(data.getFiles().size());
		} else {
			record.setFileCount(1);
		}
	}

	/**
	 * 设置索引
	 * 
	 * @param record
	 * @param data
	 */
	private void setRecordIndex(final Record record, final PushData data) {
		Set<String> indexNames = new HashSet<>();
		// 标题
		toIndexNames(indexNames, record.getTitle());
		// URL
		toIndexNames(indexNames, FilenameUtils.getBaseName(data.getUrl()));
		// 文件列表
		if (data.getFiles() != null) {
			for (String filePath : data.getFiles().keySet()) {
				toIndexNames(indexNames, FilenameUtils.getBaseName(filePath));
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
	private void toIndexNames(final Set<String> indexNames, final String name) {
		for (String str : StringSplit.split(name)) {
			try {
				// 处理拼音
				String[] pinArray = PinyinTool.toPinYinArray(str);
				// 首字母拼音
				indexNames.add(PinyinTool.getFirstStr(pinArray));
				// 全拼
				indexNames.add(PinyinTool.toText(pinArray));
				// 原始文字
				indexNames.add(name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
	private static SearchResult toSearchResult(final QueryResult queryResult, final String wd, String preTag,
			String postTag) {
		SearchResult searchResult = new SearchResult();
		searchResult.setTotal(queryResult.getTotal());
		List<SearchRecord> records = new ArrayList<>();
		for (QueryRecord queryRecord : queryResult.getRecords()) {
			records.add(toSearchRecord(queryRecord, wd, preTag, postTag));
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
	private static SearchRecord toSearchRecord(final QueryRecord queryRecord, String wd, String preTag,
			String postTag) {
		Map<String, Object> source = queryRecord.getSource();
		Map<String, Collection<String>> highLight = queryRecord.getHighLight();

		SearchRecord searchRecord = new SearchRecord();

		// 设置esID
		if (queryRecord.getId() != null) {
			searchRecord.setId(queryRecord.getId());
		}

		// 设置mongodbId
		if (source.get("ref") != null) {
			searchRecord.setRef(String.valueOf(source.get("ref")));
		}
		// 标题 ，手动高亮
		if (source.get("title") != null) {
			String title = String.valueOf(source.get("title"));
			try {
				title = title.replaceAll(wd, preTag + wd + postTag);
			} catch (Exception e) {
			}
			searchRecord.setTitle(title);
		}

		// 文件总长度
		if (source.get("totalSize") != null) {
			long totalSize = Long.valueOf(String.valueOf(source.get("totalSize")));
			searchRecord.setSize(FormatUtil.formatSize(totalSize));
		}

		// 文件创建时间,优先发布时间，发布时间为空则用创建时间
		if (source.get("publishTime") != null) {
			long time = Long.parseLong(String.valueOf(source.get("publishTime")));
			time = time > 0 ? time : Long.parseLong(String.valueOf(source.get("createTime")));
			searchRecord.setTime(FormatUtil.formatTime(time));
		}

		// 文件列表
		if (source.get("fileCount") != null) {
			searchRecord.setFileCount(Integer.parseInt(String.valueOf(source.get("fileCount"))));
		}

		// 文件类型
		if (source.get("fileType") != null) {
			Set<String> fileTypeName = new HashSet<>();
			for (String type : (Collection<String>) source.get("fileType")) {
				String typeMark = FileTypeUtil.query(type);
				if (typeMark != null) {
					fileTypeName.add(typeMark);
				}
			}
			searchRecord.setType(fileTypeName.toArray(new String[0]));
		}

		// 相关相关性
		if (highLight.get("index") != null) {
			String index = String.valueOf(highLight.get("index").toArray()[0]);
			searchRecord.setLike(index);
		}

		return searchRecord;
	}

}
