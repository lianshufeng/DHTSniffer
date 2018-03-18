package com.fast.dev.push.timer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fast.dev.push.service.DataPushService;

/**
 * 定时器
 * 
 * @作者 练书锋
 * @联系 251708339@qq.com
 * @时间 2018年3月7日
 *
 */
@Component
public class TimerManager {
	private static final Logger Log = Logger.getLogger(TimerManager.class);
	private Scheduler scheduler;
	private Map<String, JobKey> jobCache = new ConcurrentHashMap<>();

	@PreDestroy
	private void shutdown() throws Exception {
		this.scheduler.shutdown(true);
	}

	@Autowired
	private void init() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		this.scheduler = sf.getScheduler();
		scheduler.start();
	}

	/**
	 * 添加调度器任务
	 * 
	 * @param taskName
	 * @param cron
	 * @param listCrawler
	 * @return
	 */
	public boolean add(final String schedulerName, final String cron, DataPushService dataPushService) {
		Log.info("更新调度：" + schedulerName + "  [ " + cron + " ] [" + dataPushService + "]");
		if (this.jobCache.containsKey(schedulerName)) {
			remove(schedulerName);
		}
		JobDataMap jobData = new JobDataMap();
		jobData.put("object", dataPushService);
		// 任务
		JobDetail jb = JobBuilder.newJob(TimerTaskExecute.class).setJobData(jobData).build();
		JobKey jobId = jb.getKey();
		// 触发器
		Trigger t = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
		try {
			this.scheduler.scheduleJob(jb, t);
			this.jobCache.put(schedulerName, jobId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除任务
	 * 
	 * @param id
	 */
	public void remove(final String schedulerName) {
		Log.debug("删除调度：" + schedulerName + "  [" + schedulerName + "]");
		JobKey jobKey = this.jobCache.remove(schedulerName);
		if (jobKey != null) {
			try {
				this.scheduler.deleteJob(jobKey);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}

	}

}
