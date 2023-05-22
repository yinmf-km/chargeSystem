package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.ProcessDetailMapper;
import com.course.app.webadmin.upms.model.ProcessDetail;
import com.course.app.webadmin.upms.service.ProcessDetailService;

@Service
public class ProcessDetailServiceImpl extends BaseService<ProcessDetail, Long> implements ProcessDetailService {

	@Autowired
	private ProcessDetailMapper processDetailMapper;

	@Override
	protected BaseDaoMapper<ProcessDetail> mapper() {
		return processDetailMapper;
	}

	@Override
	public void insertRecord(Long outBusiId, String processNm) {
	}
}
