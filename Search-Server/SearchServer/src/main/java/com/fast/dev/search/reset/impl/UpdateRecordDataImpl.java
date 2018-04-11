package com.fast.dev.search.reset.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fast.dev.search.dao.RecordDao;
import com.fast.dev.search.domain.RecordInfo;
import com.fast.dev.search.reset.UpdateRecordData;
import com.fast.dev.search.service.RecordService;

@Component
public class UpdateRecordDataImpl implements UpdateRecordData {

	private static final Logger LOG = Logger.getLogger(UpdateRecordDataImpl.class);

	@Autowired
	private RecordService recordService;

	@Autowired
	private RecordDao recordDao;

	@Override
	public void execute(List<RecordInfo> list) {
		LOG.info("update data : " + list.size());
		Map<String, Object> tags = new HashMap<>();
		for (RecordInfo recordInfo : list) {
			executeTag(recordInfo);
			tags.put(recordInfo.getId(), recordInfo.getTags());
		}
		// 同步记录标签到ES
		updateRecordTagsToES(tags);
	}

	/**
	 * 更新标签
	 * 
	 * @param recordInfo
	 */
	private void executeTag(RecordInfo recordInfo) {
		this.recordService.setRecordTags(recordInfo);
	}

	/**
	 * 更新记录到标签
	 * 
	 * @param tags
	 */
	private void updateRecordTagsToES(Map<String, Object> tags) {
		this.recordDao.setTag(tags);
	}

}
