package com.fast.dev.search.controller.page;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fast.dev.search.domain.RecordInfo;
import com.fast.dev.search.model.FileModel;
import com.fast.dev.search.service.RecordService;
import com.fast.dev.search.util.DownLoadUrlEncode;
import com.fast.dev.search.util.FormatUtil;

@Controller
@RequestMapping("page")
public class PageController {

	@Autowired
	private RecordService recordService;

	@RequestMapping("{id}.html")
	public ModelAndView view(@PathVariable String id) {
		ModelAndView modelAndView = new ModelAndView("page.htl");
		RecordInfo recordInfo = this.recordService.page(id);
		modelAndView.addObject("record", record(recordInfo));
		// if (recordInfo != null) {
		// try {
		// modelAndView.addObject("record", PropertyUtils.describe(recordInfo));
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		return modelAndView;
	}

	/**
	 * 转换为需要显示的模型
	 * 
	 * @param recordInfo
	 * @return
	 */
	private Map<String, Object> record(RecordInfo recordInfo) {
		if (recordInfo == null) {
			return null;
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("title", recordInfo.getTitle());
		String sizeText = FormatUtil.formatSize(recordInfo.getTotalSize());
		m.put("totalSize", sizeText == null ? "" : "总共： " + sizeText);
		if (recordInfo.getFiles() == null || recordInfo.getFiles().size() == 0) {
			m.put("files", new FileModel[] { new FileModel(FilenameUtils.getName(recordInfo.getUrl()), 0) });
		} else {
			m.put("files", recordInfo.getFiles());
		}

		// 编码各种下载工具的URL
		Map<String, String> urls = new HashMap<>();
		urls.put("source", recordInfo.getUrl());
		urls.put("xunlei", DownLoadUrlEncode.xunlei(recordInfo.getUrl()));
		urls.put("kuaiche", DownLoadUrlEncode.kuaiche(recordInfo.getUrl()));
		urls.put("xuanfeng", DownLoadUrlEncode.xuanfeng(recordInfo.getUrl()));
		m.put("url", urls);

		return m;
	}

}
