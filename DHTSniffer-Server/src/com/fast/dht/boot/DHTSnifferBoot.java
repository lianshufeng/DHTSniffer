package com.fast.dht.boot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fast.dht.DHTSnifferServer;
import com.fast.dht.conf.DHTServerConfig;
import com.fast.dht.configuration.DHTSnifferConfiguration;
import com.fast.dht.util.JsonUtil;

/**
 * Application Lifecycle Listener implementation class boot
 *
 */


@WebListener
public class DHTSnifferBoot implements ServletContextListener {
	private static final Logger LOG = Logger.getLogger(DHTSnifferBoot.class);

	// spring容器
	public static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

	/**
	 * Default constructor.
	 */
	public DHTSnifferBoot() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		DHTSnifferServer dhtSnifferServer = 	applicationContext.getBean(DHTSnifferServer.class);
		dhtSnifferServer.stop();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		String path = DHTSnifferBoot.class.getResource("/").getPath();
		String configFileName = new File(path + "/DHTServer.json").getAbsolutePath();
		DHTServerConfig dhtServerConfig = dhtServerConfig(configFileName);
		try {
			LOG.info("config : " + JsonUtil.toJson(dhtServerConfig));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
