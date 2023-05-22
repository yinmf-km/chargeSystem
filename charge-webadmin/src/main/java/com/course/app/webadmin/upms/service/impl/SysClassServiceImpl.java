package com.course.app.webadmin.upms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.core.base.service.BaseService;
import com.course.app.webadmin.upms.dao.SysClassMapper;
import com.course.app.webadmin.upms.model.SysClass;
import com.course.app.webadmin.upms.service.SysClassService;

@Service
public class SysClassServiceImpl extends BaseService<SysClass, Long> implements SysClassService {

    @Autowired
    private SysClassMapper sysClassMapper;

    @Override
    protected BaseDaoMapper<SysClass> mapper() {
        return sysClassMapper;
    }

}
