package com.fast.dev.search.reset;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ResetTaskManager {

	// 线程池
	private ExecutorService pool = Executors.newFixedThreadPool(10);

	@PreDestroy
	private void shutdown() {
		this.pool.shutdownNow();
	}

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * 执行任务
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public void execute(long time, int limit, long sleep) {
		FindRecordTask findRecordTask = this.applicationContext.getBean(FindRecordTask.class);
		findRecordTask.setLimit(limit);
		findRecordTask.setTime(time);
		findRecordTask.setSleep(sleep);
		this.pool.execute(findRecordTask);
	}

}
