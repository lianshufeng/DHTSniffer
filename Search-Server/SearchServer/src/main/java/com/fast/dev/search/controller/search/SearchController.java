package com.fast.dev.search.controller.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fast.dev.core.model.InvokerResult;
import com.fast.dev.search.model.SearchRecord;
import com.fast.dev.search.model.SearchResult;
import com.fast.dev.search.service.RecordService;

@Controller
@RequestMapping("store")
public class SearchController {

	final static String preTag = "<font color='red'>";
	final static String postTag = "</font>";

	@Autowired
	private RecordService recordService;

	@RequestMapping("search.json")
	public InvokerResult<SearchResult> search(String wd, Integer page, Integer size) {
		// 进行查询
		SearchResult content = this.recordService.search(wd, page == null ? 1 : page,
				size == null || size > 30 ? 10 : size, preTag, postTag);
		return new InvokerResult<SearchResult>(content);
	}

	@RequestMapping("news.json")
	public InvokerResult<Object> news(Integer size) {
		// 进行查询
		SearchResult content = this.recordService.search(null, 1, size == null || size > 30 ? 10 : size, "", "");
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

}
