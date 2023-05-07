package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.DormFeeFreeDetailMapper;
import com.course.app.webadmin.upms.model.DormFeeFreeDetail;
import com.course.app.webadmin.upms.service.DormFeeFreeDetailService;

@Service
public class SchoolFeeFreeDetailServiceImpl extends BaseService<DormFeeFreeDetail, Long> implements DormFeeFreeDetailService {

	@Autowired
	private DormFeeFreeDetailMapper dormFeeFreeDetailMapper;

	@Override
	protected BaseDaoMapper<DormFeeFreeDetail> mapper() {
		return dormFeeFreeDetailMapper;
	}
}
