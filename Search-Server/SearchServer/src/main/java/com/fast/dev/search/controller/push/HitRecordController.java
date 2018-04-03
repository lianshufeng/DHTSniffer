package com.fast.dev.search.controller.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fast.dev.core.model.InvokerResult;
import com.fast.dev.search.dao.RecordHitDao;

@Controller
@RequestMapping("store")
public class HitRecordController {

	@Autowired
	private RecordHitDao recordHitDao;

	@RequestMapping("hitRecord.json")
	public InvokerResult<Object> hitRecord(final String id) {
		boolean result = recordHitDao.hitInc(id, 1);
		return new InvokerResult<Object>(result);
	}

}
