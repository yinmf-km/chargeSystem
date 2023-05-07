package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.SysStudentMapper;
import com.course.app.webadmin.upms.model.SysStudent;
import com.course.app.webadmin.upms.service.SysStudentService;

@Service
public class SysStudentServiceImpl extends BaseService<SysStudent, Long> implements SysStudentService {

	@Autowired
	private SysStudentMapper sysStudentMapper;

	@Override
	protected BaseDaoMapper<SysStudent> mapper() {
		return sysStudentMapper;
	}
}
