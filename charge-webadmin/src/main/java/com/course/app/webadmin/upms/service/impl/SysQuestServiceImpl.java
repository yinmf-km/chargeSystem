package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.SysQuestMapper;
import com.course.app.webadmin.upms.model.SysQuest;
import com.course.app.webadmin.upms.service.SysQuestService;

@Service
public class SysQuestServiceImpl extends BaseService<SysQuest, Long> implements SysQuestService {

	@Autowired
	private SysQuestMapper sysQuestMapper;

	@Override
	protected BaseDaoMapper<SysQuest> mapper() {
		return sysQuestMapper;
	}
}
