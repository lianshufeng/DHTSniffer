package com.fast.dev.search.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import com.fast.dev.es.query.QueryHighlight;
import com.fast.dev.es.query.QueryLimit;
import com.fast.dev.es.query.QueryResult;
import com.fast.dev.es.query.QuerySort;
import com.fast.dev.search.domain.Record;

@Repository
public class RecordDao extends SuperDao<Record> {

	private static final String TitleName = "title";
	private static final String IndexName = "index";
	private static final String TagsName = "tags";

	/**
	 * 取出对应数据
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> get(String id) {
		Map<String, Object> m = esDao.get(id);
		if (m != null && m.size() > 0) {
			Object[] objects = m.values().toArray();
			if (objects != null && objects.length > 0) {
				return (Map<String, Object>) objects[0];
			}
		}
		return null;
	}

	/**
	 * 保存数据
	 * 
	 * @param record
	 * @return
	 */
	public LinkedHashMap<String, String> save(Collection<Record> records) {
		return esDao.save(records.toArray());
	}

	/**
	 * 增量更新inc
	 * 
	 * @param ref
	 * @param incCount
	 */
	public void setHit(Map<String, Long> updateMap) {
		Map<String, Object> sources = new HashMap<>();
		for (Entry<String, Long> entry : updateMap.entrySet()) {
			Map<String, Object> update = new HashMap<>();
			update.put("hit", entry.getValue());
			sources.put(entry.getKey(), update);
		}
		this.esDao.update(sources);
	}

	/**
	 * 分页查询
	 * 
	 * @param wd
	 * @param from
	 * @param size
	 * @param preTag
	 * @param postTag
	 * @return
	 */
	public QueryResult searchWord(final String wd, int from, int size, String preTag, String postTag) {

		// 自动分割，多次匹配
		// final String[] wds = StringSplit.split(wd);

		// 分页
		QueryLimit queryLimit = new QueryLimit(from, size, 5000l);

		// 排序
		List<QuerySort> querySorts = new ArrayList<>();
		if (!StringUtils.isEmpty(wd)) {
			querySorts.add(new QuerySort("hit", SortOrder.DESC));
		}
		querySorts.add(new QuerySort("createTime", SortOrder.DESC));

		// 高亮规则
		List<QueryHighlight> queryHighlights = new ArrayList<>();
		queryHighlights.add(new QueryHighlight(TitleName, preTag, postTag));
		queryHighlights.add(new QueryHighlight(IndexName, preTag, postTag));

		// 如果没有传递搜索参数则全局搜索，按照收录时间降序
		QueryBuilder queryBuilder = StringUtils.isEmpty(wd) ? QueryBuilders.matchAllQuery() : getQueryBuilder(wd);

		return this.esDao.list(queryBuilder, 20, queryHighlights, querySorts, queryLimit);
	}
	
	
	
	
	
	
	
	/**
	 * 搜索标记
	 * @param wd
	 * @param from
	 * @param size
	 * @return
	 */
	public QueryResult searchTag(final String tagName, int from, int size) {
		// 分页
		QueryLimit queryLimit = new QueryLimit(from, size, 5000l);

		// 排序
		List<QuerySort> querySorts = new ArrayList<>();
		querySorts.add(new QuerySort("hit", SortOrder.DESC));
		querySorts.add(new QuerySort("createTime", SortOrder.DESC));

		//查询
		return this.esDao.list(QueryBuilders.matchPhraseQuery(TagsName, tagName), 20, null, querySorts, queryLimit);
	}
	
	

	/**
	 * 构建查询对象
	 * 
	 * @param wds
	 * @return
	 */
	private static QueryBuilder getQueryBuilder(final String wd) {
		// 组合查询
		BoolQueryBuilder rootQueryBuilder = QueryBuilders.boolQuery();

		// 查询 title and
		BoolQueryBuilder titleQueryBuilder = QueryBuilders.boolQuery();
		titleQueryBuilder.must(QueryBuilders.matchPhraseQuery(TitleName, wd));
		// for (String kw : StringSplit.split(wd)) {
		// titleQueryBuilder.must(QueryBuilders.matchPhraseQuery(TitleName, kw));
		// }

		// 查询 index or
		BoolQueryBuilder indexQueryBuilder = QueryBuilders.boolQuery();
		indexQueryBuilder.should(QueryBuilders.matchPhraseQuery(IndexName, wd));

		// (title1 and title2) or (index1 or index2)
		return rootQueryBuilder.should(titleQueryBuilder).should(indexQueryBuilder);
	}

}
