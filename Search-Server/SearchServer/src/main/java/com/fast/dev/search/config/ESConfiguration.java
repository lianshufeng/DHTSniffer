package com.fast.dev.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.es.conf.ESConfig;
import com.fast.dev.es.config.ElasticSearchConfiguration;

@Configuration
public class ESConfiguration extends ElasticSearchConfiguration {

	@Bean
	public ESConfig esConfig() throws Exception {
		return JsonUtil.loadToObject("ESConfig.json", ESConfig.class);
	}

}
