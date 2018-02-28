package com.fast.dht.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fast.dht.DHTSnifferServer;
import com.fast.dht.conf.DHTServerConfig;
import com.fast.dht.impl.DHTSnifferServerImpl;

@Configuration
public class DHTSnifferConfiguration {

	@Bean
	public DHTSnifferServer build(DHTServerConfig dhtServerConfig) {
		DHTSnifferServerImpl dhtSnifferServer = new DHTSnifferServerImpl();
		dhtSnifferServer.config(dhtServerConfig);
		return dhtSnifferServer;
	}

}
