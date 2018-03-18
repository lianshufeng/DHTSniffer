package com.fast.dev.push.timer;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fast.dev.push.service.DataPushService;

public class TimerTaskExecute implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		JobDataMap jobDataMap = arg0.getMergedJobDataMap();
		DataPushService dataPushService = (DataPushService) jobDataMap.get("object");
		dataPushService.execute();
	}
}
