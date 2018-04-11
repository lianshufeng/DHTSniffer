package com.fast.dev.search.reset;

import java.util.List;

import com.fast.dev.search.domain.RecordInfo;

/**
 * 重置记录数据
 * 
 * @作者 练书锋
 * @时间 2018年4月11日
 *
 *
 */
public interface UpdateRecordData {

	/**
	 * 执行数据更新
	 */
	public void execute(List<RecordInfo> list);

}
