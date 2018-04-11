package com.fast.dev.search.tags.adapters.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.search.tags.adapters.TagAdapter;
import com.fast.dev.search.tags.adapters.bean.LikeTag;
import com.fast.dev.search.tags.adapters.conf.LikeTagConfig;
import com.fast.dev.search.util.StringSplit;

/**
 * 模糊匹配
 * 
 * @作者 练书锋
 * @时间 2018年4月9日
 *
 *
 */
@Component
public class LikeTagAdapter extends TagAdapter {
	// 标签排序
	private Map<String, Integer> tagOrders = null;
	// 当前配置过的标签
	private Set<LikeTag> likeTags = null;

	@Autowired
	@SuppressWarnings("unchecked")
	private void init() throws Exception {
		Map<String, Map<String, Object>> config = JsonUtil.loadToObject("LikeTagConfig.json", Map.class);
		initLikeTag(config);
		initTagOrders();

	}

	/**
	 * 初始化匹配的标签
	 * 
	 * @param config
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private void initLikeTag(Map<String, Map<String, Object>> config) throws Exception {
		this.likeTags = new HashSet<>();
		for (Entry<String, Map<String, Object>> entry : config.entrySet()) {
			Map<String, Object> properties = entry.getValue();
			LikeTagConfig likeTagConf = new LikeTagConfig();
			BeanUtils.populate(likeTagConf, properties);
			String[] names = StringSplit.split(entry.getKey());
			String[] likes = StringSplit.split(likeTagConf.getLikes());
			int sort = likeTagConf.getSort();
			String[] excludes = StringSplit.split(likeTagConf.getExcludes());
			LikeTag likeTag = new LikeTag(names, likes, sort, excludes);
			this.likeTags.add(likeTag);
		}
	}

	/**
	 * 缓存标签命令
	 */
	private void initTagOrders() {
		this.tagOrders = new HashMap<>();
		for (LikeTag likeTag : this.likeTags) {
			for (String name : likeTag.getNames()) {
				tagOrders.put(name, likeTag.getSort());
			}
		}
	}

	@Override
	public Collection<String> match(String... values) {
		Set<String> result = new HashSet<>();
		// 遍历所有需要校验的值
		for (String val : values) {
			// 遍历所有的标签
			for (LikeTag likeTag : this.likeTags) {
				// 遍历标签所有的匹配字符串
				for (String like : likeTag.getLikes()) {
					boolean contain = val.toLowerCase().indexOf(like.toLowerCase()) > -1;
					// 过滤排除
					if (likeTag.getExcludes() != null) {
						for (String exclude : likeTag.getExcludes()) {
							if (val.toLowerCase().indexOf(exclude.toLowerCase()) > -1) {
								contain = false;
								break;
							}
						}
					}
					if (contain) {
						result.addAll(Arrays.asList(likeTag.getNames()));
					}

				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Integer> tagOrders() {
		return this.tagOrders;
	}

}
