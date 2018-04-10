package com.fast.dev.search.tags.adapters.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fast.dev.search.tags.adapters.TagAdapter;
import com.fast.dev.search.util.ChineseUtil;

@Component
public class ChineseTagAdapter extends TagAdapter {

	private Map<String, Integer> matchMap = new HashMap<>();

	@Autowired
	private void init() {
		matchMap.put("中文", 0);
	}

	@Override
	public Collection<String> match(String... values) {
		for (String val : values) {
			if (ChineseUtil.isContain(val)) {
				return tagOrders().keySet();
			}
		}
		return null;
	}

	@Override
	public Map<String, Integer> tagOrders() {
		return matchMap;
	}

}
