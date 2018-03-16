package com.fast.dev.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.es.config.ElasticSearchConfiguration;
import com.fast.dev.search.conf.SearchConfig;

/**
 * 配置es
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月16日
 *
 */
@Configuration
public class ESConfiguration extends ElasticSearchConfiguration {

	@Bean
	public SearchConfig searchConfig() throws Exception {
		return JsonUtil.loadToObject("ESConfig.json", SearchConfig.class);
	}

}
