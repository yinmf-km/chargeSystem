package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.SchoolFeeMapper;
import com.course.app.webadmin.upms.model.SchoolFee;
import com.course.app.webadmin.upms.service.SchoolFeeService;

@Service
public class SchoolFeeServiceImpl extends BaseService<SchoolFee, Long> implements SchoolFeeService {

	@Autowired
	private SchoolFeeMapper schoolFeeMapper;

	@Override
	protected BaseDaoMapper<SchoolFee> mapper() {
		return schoolFeeMapper;
	}
}
