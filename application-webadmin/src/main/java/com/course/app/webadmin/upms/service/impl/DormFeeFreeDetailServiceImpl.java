package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.SchoolFeeFreeDetailMapper;
import com.course.app.webadmin.upms.model.SchoolFeeFreeDetail;
import com.course.app.webadmin.upms.service.SchoolFeeFreeDetailService;

@Service
public class DormFeeFreeDetailServiceImpl extends BaseService<SchoolFeeFreeDetail, Long> implements SchoolFeeFreeDetailService {

	@Autowired
	private SchoolFeeFreeDetailMapper schoolFeeFreeDetailMapper;

	@Override
	protected BaseDaoMapper<SchoolFeeFreeDetail> mapper() {
		return schoolFeeFreeDetailMapper;
	}
}
