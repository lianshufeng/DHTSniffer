package com.fast.dev.search.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import com.fast.dev.es.query.QueryHighlight;
import com.fast.dev.es.query.QueryLimit;
import com.fast.dev.es.query.QueryMatch;
import com.fast.dev.es.query.QueryPhrase;
import com.fast.dev.es.query.QueryResult;
import com.fast.dev.es.query.QuerySort;
import com.fast.dev.search.domain.Record;

@Repository
public class RecordDao extends SuperDao<Record> {

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
	public Collection<String> save(Collection<Record> records) {
		Map<String, String> result = esDao.save(records.toArray());
		if (result != null) {
			return new ArrayList<>(result.keySet());
		}
		return null;
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
	public QueryResult search(final String wd, int from, int size, String preTag, String postTag) {

		// 分页
		QueryLimit queryLimit = new QueryLimit(from, size, 5000l);

		// 排序
		List<QuerySort> querySorts = new ArrayList<>();
		querySorts.add(new QuerySort("hitCount", SortOrder.DESC));
		querySorts.add(new QuerySort("createTime", SortOrder.DESC));

		// 高亮规则
		List<QueryHighlight> queryHighlights = new ArrayList<>();
		queryHighlights.add(new QueryHighlight("index", preTag, postTag));
		queryHighlights.add(new QueryHighlight("title", preTag, postTag));

		// 查询条件
		QueryPhrase queryPhrase = new QueryPhrase();
		queryPhrase.setAnd(false);
		queryPhrase.setQueryMatch(new ArrayList<QueryMatch>() {
			private static final long serialVersionUID = 1L;
			{
				add(new QueryMatch("index", wd));
				add(new QueryMatch("title", wd));
			}
		});

		return this.esDao.list(queryPhrase, queryHighlights, querySorts, queryLimit);
	}

}
