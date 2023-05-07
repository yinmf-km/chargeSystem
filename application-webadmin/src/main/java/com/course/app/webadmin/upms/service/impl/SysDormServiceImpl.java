package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.SysDormMapper;
import com.course.app.webadmin.upms.model.SysDorm;
import com.course.app.webadmin.upms.service.SysDormService;

@Service
public class SysDormServiceImpl extends BaseService<SysDorm, Long> implements SysDormService {

	@Autowired
	private SysDormMapper sysDormMapper;

	@Override
	protected BaseDaoMapper<SysDorm> mapper() {
		return sysDormMapper;
	}
}
