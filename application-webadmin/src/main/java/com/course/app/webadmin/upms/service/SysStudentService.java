package com.course.app.webadmin.upms.service;

import java.util.List;
import java.util.Map;

import com.course.app.common.core.base.service.IBaseService;
import com.course.app.webadmin.upms.model.SysStudent;

public interface SysStudentService extends IBaseService<SysStudent, Long> {

    public List<Map<String, Object>> listStudentInfo(Map<String, Object> condition);
}
