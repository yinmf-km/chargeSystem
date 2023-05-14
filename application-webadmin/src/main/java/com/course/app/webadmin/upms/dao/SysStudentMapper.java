package com.course.app.webadmin.upms.dao;

import java.util.List;
import java.util.Map;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.SysStudent;

public interface SysStudentMapper extends BaseDaoMapper<SysStudent> {

    public List<Map<String, Object>> listStudentInfo(Map<String, Object> condition);
}
