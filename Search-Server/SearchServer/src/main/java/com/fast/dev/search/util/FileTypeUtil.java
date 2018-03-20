package com.fast.dev.search.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

public class FileTypeUtil {
	private final static Map<String, String> fileMap = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("zip", "压缩");
			put("rar", "压缩");
			put("7z", "压缩");
			

			put("wmv", "媒体");
			put("rmvb", "媒体");
			put("wmv", "媒体");
			put("3gp", "媒体");
			put("mp4", "媒体");
			put("avi", "媒体");
			put("flv", "媒体");
			put("mp3", "媒体");
			put("mkv", "媒体");
			

			put("exe", "应用");
			put("dll", "应用");
			put("ipa", "应用");
			put("apk", "应用");

			put("txt", "文档");
			put("doc", "文档");
			put("ppt", "文档");
			put("docx", "文档");
			put("xlsx", "文档");
			put("pptx", "文档");
			put("pdf", "文档");

			put("jpg", "图像");
			put("gif", "图像");
			put("png", "图像");
			put("bmp", "图像");

		}
	};

	/**
	 * 查询文件类型，只收录常见的
	 * 
	 * @param fileName
	 * @return
	 */
	public static String query(final String extName) {
		if (StringUtils.isEmpty(extName)) {
			return null;
		}
		String text = fileMap.get(extName.toLowerCase());
		return text == null ? "其他" : text;
	}

}
