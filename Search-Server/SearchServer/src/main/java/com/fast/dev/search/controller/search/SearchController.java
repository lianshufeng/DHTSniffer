package com.fast.dev.search.controller.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fast.dev.core.model.InvokerResult;
import com.fast.dev.search.domain.HotWord;
import com.fast.dev.search.model.SearchRecord;
import com.fast.dev.search.model.SearchResult;
import com.fast.dev.search.service.RecordService;
import com.fast.dev.search.service.RecordTagService;

@Controller
@RequestMapping("store")
public class SearchController {

	final static String preTag = "<font color='red'>";
	final static String postTag = "</font>";

	@Autowired
	private RecordService recordService;

	@Autowired
	private RecordTagService recordTagService;

	@RequestMapping("search.json")
	public InvokerResult<SearchResult> search(String wd, Integer page, Integer size) {
		page = page == null ? 1 : page;
		size = size == null || size > 30 ? 10 : size;
		// 记录热词
		this.recordService.hitHotWord(wd);
		int protocol = wd.indexOf(":");
		if (protocol > -1) {
			if (wd.substring(0, protocol).equalsIgnoreCase("tag")) {
				return new InvokerResult<SearchResult>(searchTag(wd.substring(protocol + 1, wd.length()), page, size));
			}
		}
		return new InvokerResult<SearchResult>(searchWord(wd, page, size));
	}

	/**
	 * 搜索标签
	 * 
	 * @param wd
	 * @param page
	 * @param size
	 * @return
	 */
	private SearchResult searchTag(String wd, Integer page, Integer size) {
		return this.recordService.searchTag(wd, page, size);
	}

	/**
	 * 搜索关键词
	 * 
	 * @param wd
	 * @param page
	 * @param size
	 * @return
	 */
	private SearchResult searchWord(String wd, Integer page, Integer size) {
		// 进行查询
		return this.recordService.searchWord(wd, page, size, preTag, postTag);
	}

	@Deprecated
	@RequestMapping("news.json")
	public InvokerResult<Object> news(Integer count) {
		// 进行查询
		SearchResult content = this.recordService.searchWord(null, 1, count == null || count > 30 ? 10 : count, "", "");
		List<Map<String, Object>> result = new ArrayList<>();
		for (final SearchRecord searchRecord : content.getDatas()) {
			String time = searchRecord.getTime();
			Map<String, Object> m = new HashMap<>();
			m.put("name", searchRecord.getTitle());
			if (time != null) {
				String[] strs = time.split(" ");
				m.put("time", strs.length > 0 ? strs[0] : null);
			}
			result.add(m);
		}
		return new InvokerResult<Object>(result);
	}

	@RequestMapping("hotWords.json")
	public InvokerResult<Object> hotWords(Integer count) {
		List<HotWord> result = this.recordService.getHotWords(count == null ? 10 : count, 7);
		return new InvokerResult<Object>(result);
	}

	@RequestMapping("tags.json")
	public InvokerResult<Object> tags() {
		return new InvokerResult<Object>(this.recordTagService.tags());
	}

}
