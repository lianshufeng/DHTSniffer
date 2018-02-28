package com.fast.dht;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fast.dht.conf.DHTServerConfig;
import com.fast.dht.configuration.DHTSnifferConfiguration;
import com.fast.dht.util.JsonUtil;

public class CommandLineMain {

	private static final Logger LOG = Logger.getLogger(CommandLineMain.class);

	// spring容器
	private static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

	/**
	 * 程序入口
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String configFileName = null;
		if (args == null || args.length == 0) {
			String path = CommandLineMain.class.getResource("/").getPath();
			configFileName = new File(path + "/DHTServer.json").getAbsolutePath();
		} else {
			configFileName = new File(args[0]).getAbsolutePath();
		}

		DHTServerConfig dhtServerConfig = dhtServerConfig(configFileName);
		LOG.info("config : " + JsonUtil.toJson(dhtServerConfig));
		// 注入配置对象到容器里,并扫描
		ConfigurableListableBeanFactory configurableListableBeanFactory = applicationContext.getBeanFactory();
		configurableListableBeanFactory.registerSingleton("dhtServerConfig", dhtServerConfig);
		applicationContext.register(DHTSnifferConfiguration.class);
		applicationContext.refresh();

		// 通过容器拿出DHT嗅探服务
		DHTSnifferServer dhtSnifferServer = applicationContext.getBean(DHTSnifferServer.class);
		// 启动服务
		dhtSnifferServer.start();
	}

	/**
	 * 读取配置对象
	 * 
	 * @param fileName
	 * @return
	 */
	private static DHTServerConfig dhtServerConfig(String fileName) {
		try {
			String content = readFileContent(fileName, "UTF-8");
			return JsonUtil.toObject(content, DHTServerConfig.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @param encode
	 * @return
	 * @throws IOException
	 */
	private static String readFileContent(String fileName, String encode) throws IOException {
		File file = new File(fileName);
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] buff = StreamUtils.getBytes(fileInputStream);
		fileInputStream.close();
		return new String(buff, encode);
	}

}
