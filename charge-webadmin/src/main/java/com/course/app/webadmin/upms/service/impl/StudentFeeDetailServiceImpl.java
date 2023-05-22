package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.StudentFeeDetailMapper;
import com.course.app.webadmin.upms.model.StudentFeeDetail;
import com.course.app.webadmin.upms.service.StudentFeeDetailService;

@Service
public class StudentFeeDetailServiceImpl extends BaseService<StudentFeeDetail, Long>
		implements StudentFeeDetailService {

	@Autowired
	private StudentFeeDetailMapper studentFeeDetailMapper;

	@Override
	protected BaseDaoMapper<StudentFeeDetail> mapper() {
		return studentFeeDetailMapper;
	}
}
