package com.course.app.webadmin.upms.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.SchoolFee;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 学费信息数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface SchoolFeeMapper extends BaseDaoMapper<SchoolFee> {

    /**
     * 批量插入对象列表。
     *
     * @param schoolFeeList 新增对象列表。
     */
    void insertList(List<SchoolFee> schoolFeeList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param schoolFeeFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<SchoolFee> getSchoolFeeList(
            @Param("schoolFeeFilter") SchoolFee schoolFeeFilter, @Param("orderBy") String orderBy);
}
