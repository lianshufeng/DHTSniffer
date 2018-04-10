package com.fast.dev.search.tags.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fast.dev.search.tags.TagManager;
import com.fast.dev.search.tags.adapters.TagAdapter;

@Component
public class TagManagerImpl implements TagManager {

	private Collection<TagAdapter> tagAdapterCaches = null;

	// 标签顺序
	private String[] tagSorts = null;

	@Autowired
	private void init(ApplicationContext applicationContext) {
		tagAdapterCaches = applicationContext.getBeansOfType(TagAdapter.class).values();
		// 标签排序
		tagSorts();
	}

	/**
	 * 标签排序
	 */
	private void tagSorts() {
		List<TagOrder> orders = new ArrayList<>();
		for (TagAdapter tagAdapter : tagAdapterCaches) {
			for (Entry<String, Integer> e : tagAdapter.tagOrders().entrySet()) {
				orders.add(new TagOrder(e.getKey(), e.getValue()));
			}
		}

		// 排序
		Collections.sort(orders, new Comparator<TagOrder>() {

			@Override
			public int compare(TagOrder t1, TagOrder t2) {
				return t2.sort - t1.sort;
			}
		});

		// 转换为字符串数组
		List<String> result = new ArrayList<>();
		for (TagOrder tagOrder : orders) {
			result.add(tagOrder.getName());
		}
		tagSorts = result.toArray(new String[0]);
	}

	/**
	 * 依次调用适配器进行匹配
	 */
	@Override
	public Set<String> match(String... values) {
		Set<String> sets = new HashSet<>();
		for (TagAdapter tagAdapter : tagAdapterCaches) {
			Collection<String> result = tagAdapter.match(values);
			if (result != null && result.size() > 0) {
				sets.addAll(result);
			}
		}
		return sets;
	}

	@Override
	public String[] tags() {
		return this.tagSorts;
	}

	/**
	 * 标签
	 * 
	 * @作者 练书锋
	 * @时间 2018年4月9日
	 *
	 *
	 */
	class TagOrder {
		private String name;
		private int sort;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getSort() {
			return sort;
		}

		public void setSort(int sort) {
			this.sort = sort;
		}

		public TagOrder() {
			// TODO Auto-generated constructor stub
		}

		public TagOrder(String name, int sort) {
			super();
			this.name = name;
			this.sort = sort;
		}

	}

}
