package com.fast.dev.search.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fast.dev.es.dao.ESDao;
import com.fast.dev.es.helper.ESHelper;
import com.fast.dev.search.conf.SearchConfig;

public abstract class SuperDao<T> {

	// ESdao
	protected ESDao esDao;

	// 自动注入
	@Autowired
	private void init(ApplicationContext applicationContext, SearchConfig searchConfig) {
		this.esDao = applicationContext.getBean(ESHelper.class).dao(searchConfig.getDb(), getCls().getSimpleName());
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getCls() {
		Type t = this.getClass().getGenericSuperclass();
		if (t instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) t).getActualTypeArguments();
			return (Class<T>) p[0];
		}
		return null;
	}

}
