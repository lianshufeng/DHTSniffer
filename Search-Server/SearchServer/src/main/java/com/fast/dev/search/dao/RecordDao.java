package com.fast.dev.search.dao;

import java.util.ArrayList;
import java.util.List;

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
