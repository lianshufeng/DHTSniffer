package com.fast.dev.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fast.dev.component.mongodb.model.MongodbConfig;
import com.fast.dev.core.util.code.JsonUtil;

@Configuration
public class MongoConfiguration {

	@Bean
	public MongodbConfig mongodbConfig() {
		try {
			return JsonUtil.loadToObject("Mongodb.json", MongodbConfig.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
