package com.fast.dev.search.reset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fast.dev.search.dao.RecordInfoDao;
import com.fast.dev.search.domain.RecordInfo;

@Component
@Scope("prototype")
public class FindRecordTask implements Runnable {

	// 开始时间
	private long time;
	// 限制数量
	private int limit;
	// 延迟
	private long sleep;

	@Autowired
	private RecordInfoDao recordInfoDao;

	@Autowired
	private ResetTaskManager resetTaskManager;

	@Autowired
	private UpdateRecordData resetRecordData;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public long getSleep() {
		return sleep;
	}

	public void setSleep(long sleep) {
		this.sleep = sleep;
	}

	@Override
	public void run() {
		List<RecordInfo> list = recordInfoDao.findFromTime(time, limit);
		// 继续执行找任务
		if (list != null && list.size() > 0) {
			// 执行数据
			resetRecordData.execute(list);
			// 延迟
			try {
				Thread.sleep(sleep);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.resetTaskManager.execute(list.get(list.size() - 1).getCreateTime(), limit, sleep);
		}
	}

}
