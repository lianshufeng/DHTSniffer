package com.fast.dev.search.helper;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.search.util.YouDaoWordTranslation;

@Component
public class YouDaoWordHelper {

	private String ak;
	private String pk;

	@Autowired
	private void init() throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> youdao = JsonUtil.loadToObject("youdao.json", Map.class);
		ak = youdao.get("ak");
		pk = youdao.get("pk");
	}

	/**
	 * 翻译
	 * 
	 * @param word
	 * @return
	 */
	public String translation(String word) {
		try {
			return YouDaoWordTranslation.translation(this.ak, this.pk, word);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
