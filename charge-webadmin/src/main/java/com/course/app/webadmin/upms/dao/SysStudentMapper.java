package com.course.app.webadmin.upms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.SysStudent;

/**
 * 学生信息数据操作访问接口。
 * @author 云翼
 * @date 2023-02-21
 */
public interface SysStudentMapper extends BaseDaoMapper<SysStudent> {

	/**
	 * 批量插入对象列表。
	 * @param sysStudentList 新增对象列表。
	 */
	void insertList(List<SysStudent> sysStudentList);

	/**
	 * 获取过滤后的对象列表。
	 * @param sysStudentFilter 主表过滤对象。
	 * @param orderBy 排序字符串，order by从句的参数。
	 * @return 对象列表。
	 */
	List<SysStudent> getSysStudentList(@Param("sysStudentFilter") SysStudent sysStudentFilter,
			@Param("orderBy") String orderBy);

	List<Map<String, Object>> listStudentInfo(Map<String, Object> condition);
}
