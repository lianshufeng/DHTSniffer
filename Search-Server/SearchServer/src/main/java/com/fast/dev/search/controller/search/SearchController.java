package com.fast.dev.search.controller.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fast.dev.core.model.InvokerResult;
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

}
