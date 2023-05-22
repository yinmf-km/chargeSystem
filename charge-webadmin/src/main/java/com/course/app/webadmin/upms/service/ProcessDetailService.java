package com.course.app.webadmin.upms.service;

import com.course.app.common.core.base.service.IBaseService;
import com.course.app.webadmin.upms.model.ProcessDetail;

public interface ProcessDetailService extends IBaseService<ProcessDetail, Long> {

	void insertRecord(Long outBusiId, String processNm);
}
