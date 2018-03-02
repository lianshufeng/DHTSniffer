package com.fast.dht.torrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.lf5.util.StreamUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.fast.dht.torrent.conf.TorrentConfig;
import com.fast.dht.torrent.task.Magnet2Torrent;
import com.fast.dht.torrent.util.LibraryUtil;
import com.fast.dht.util.JsonUtil;

public class TorrentMain {
	private static final Logger LOG = Logger.getLogger(TorrentMain.class);
	// spring容器
	private static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

	public static ExecutorService fixedThreadPool = null;

	public static void main(String[] args) throws Exception {
		LibraryUtil.addLibraryDir();
		ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
		TorrentConfig torrentConfig = torrentConfig();
		beanFactory.registerSingleton("torrentConfig", torrentConfig);
		applicationContext.scan(TorrentMain.class.getPackage().getName());
		applicationContext.refresh();

		// 开始工作
		startWork(torrentConfig);

	}

	/**
	 * 开始工作
	 * 
	 * @param torrentConfig
	 */
	private static void startWork(TorrentConfig torrentConfig) {
		fixedThreadPool = Executors.newFixedThreadPool(torrentConfig.getMaxRunTaskCount());
		for (int i = 0; i < torrentConfig.getMaxRunTaskCount(); i++) {
			Magnet2Torrent magnet2Torrent = applicationContext.getBean(Magnet2Torrent.class);
			fixedThreadPool.execute(magnet2Torrent);
		}
		LOG.info("start work , thread count : " + torrentConfig.getMaxRunTaskCount());
	}

	/**
	 * 读取配置文件
	 * 
	 * @return
	 */
	private static TorrentConfig torrentConfig() {
		try {
			String path = TorrentMain.class.getResource("/").getPath();
			String content = readFileContent(path + "/Torrent.json", "UTF-8");
			return JsonUtil.toObject(content, TorrentConfig.class);
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
