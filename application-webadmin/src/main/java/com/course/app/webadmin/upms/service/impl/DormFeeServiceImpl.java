package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.DormFeeMapper;
import com.course.app.webadmin.upms.model.DormFee;
import com.course.app.webadmin.upms.service.DormFeeService;

@Service
public class DormFeeServiceImpl extends BaseService<DormFee, Long> implements DormFeeService {

	@Autowired
	private DormFeeMapper dormFeeMapper;

	@Override
	protected BaseDaoMapper<DormFee> mapper() {
		return dormFeeMapper;
	}
}
