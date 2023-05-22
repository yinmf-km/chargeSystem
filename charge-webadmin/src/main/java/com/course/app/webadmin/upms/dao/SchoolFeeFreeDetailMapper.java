package com.course.app.webadmin.upms.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.SchoolFeeFreeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 学费减免明细数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface SchoolFeeFreeDetailMapper extends BaseDaoMapper<SchoolFeeFreeDetail> {

    /**
     * 批量插入对象列表。
     *
     * @param schoolFeeFreeDetailList 新增对象列表。
     */
    void insertList(List<SchoolFeeFreeDetail> schoolFeeFreeDetailList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param schoolFeeFreeDetailFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<SchoolFeeFreeDetail> getSchoolFeeFreeDetailList(
            @Param("schoolFeeFreeDetailFilter") SchoolFeeFreeDetail schoolFeeFreeDetailFilter, @Param("orderBy") String orderBy);
}
