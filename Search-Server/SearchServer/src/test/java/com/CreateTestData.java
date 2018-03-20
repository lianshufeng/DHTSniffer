//package com;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Random;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//import com.fast.dev.core.util.code.JsonUtil;
//import com.fast.dev.es.dao.ESDao;
//import com.fast.dev.es.helper.ESHelper;
//import com.fast.dev.search.config.ESConfiguration;
//import com.fast.dev.search.domain.Record;
//
//public class CreateTestData {
//
//	static ESDao esDao = null;
//
//	@SuppressWarnings("resource")
//	private static void init() {
//		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
//		applicationContext.register(ESConfiguration.class);
//		applicationContext.refresh();
//		ESHelper esHelper = applicationContext.getBean(ESHelper.class);
//		esDao = esHelper.dao("resstore", "Record");
//	}
//
//	public static void main(String[] args) throws Exception {
//		init();
//
//		for (int i = 0; i < 30; i++) {
//			info("美国留给伊拉克的是个烂摊子吗");
//			info("公安部：各地校车将享最高路权");
//			info("中韩渔警冲突调查：韩警平均每天扣1艘中国渔船");
//			info("中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首");
//		}
//	}
//
//	private static void info(String info) throws Exception {
//
//		Record record = new Record();
//		record.setCreateTime(System.currentTimeMillis());
//		record.setIndex(info);
//		record.setTitle(info);
//		record.setUrl("http://url.com/dl.html");
//		record.setFiles(new HashMap<String, Long>() {
//			private static final long serialVersionUID = 1L;
//			{
//				put("/path/demo.rmvb", new BigDecimal(new Random().nextInt(1000000)).longValue());
//				put("/path/aa.mp4", new BigDecimal(new Random().nextInt(10000000)).longValue());
//				put("/path/ss.mp3", new BigDecimal(new Random().nextInt(10000000)).longValue());
//				put("/path/cc.exe", new BigDecimal(new Random().nextInt(10000000)).longValue());
//			}
//		});
//		long totalSize = 0;
//		for (long fileSize : record.getFiles().values()) {
//			totalSize += fileSize;
//		}
//		record.setTotalSize(totalSize);
//
//		System.out.println(JsonUtil.toJson(esDao.save(record)));
//	}
//
//}
