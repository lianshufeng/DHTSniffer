package com.fast.dev.push;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fast.dev.core.util.code.JsonUtil;
import com.fast.dev.push.conf.HostServerConfig;
import com.fast.dev.push.conf.PushDataServiceConfig;
import com.fast.dev.push.service.DataPushService;
import com.fast.dev.push.timer.TimerManager;

public class PushDataMain {

	private static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

	static ScheduledExecutorService threadPool = null;

	public static void main(String[] args) throws Exception {
		// 注册配置到spring中
		ConfigurableListableBeanFactory configurableListableBeanFactory = applicationContext.getBeanFactory();
		configurableListableBeanFactory.registerSingleton("hostServerConfig", hostServerConfig());
		applicationContext.scan("com.fast.dev");
		applicationContext.refresh();
		Collection<PushDataServiceConfig> configs = pushDataServiceConfigs();

		// 调度器
		final TimerManager timerManager = applicationContext.getBean(TimerManager.class);

		for (PushDataServiceConfig config : configs) {
			// 动态创建
			DataPushService dataPushService = applicationContext.getBean(readClass(config));
			dataPushService.build(config);
			timerManager.add(UUID.randomUUID().toString(), config.getCorn(), dataPushService);
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<DataPushService> readClass(PushDataServiceConfig config) throws ClassNotFoundException {
		return (Class<DataPushService>) Class.forName(DataPushService.class.getPackage().getName() + ".impl." + config.getClassName());
	}

	/**
	 * 读取主机配置
	 * 
	 * @return
	 * @throws Exception
	 */
	private static HostServerConfig hostServerConfig() throws Exception {
		return JsonUtil.loadToObject("HostServerConfig.json", HostServerConfig.class);
	}

	/**
	 * 读取配置
	 * 
	 * @return
	 * @throws Exception
	 */
	private static Collection<PushDataServiceConfig> pushDataServiceConfigs() throws Exception {
		List<PushDataServiceConfig> config = new ArrayList<>();
		File[] files = new File(PushDataMain.class.getResource("/").getFile() + "/task/").listFiles();
		for (File file : files) {
			if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("json")) {
				byte[] buff = FileUtils.readFileToByteArray(file);
				PushDataServiceConfig pushDataServiceConfig = JsonUtil.toObject(new String(buff),
						PushDataServiceConfig.class);
				config.add(pushDataServiceConfig);
			}
		}
		return config;
	}

}
