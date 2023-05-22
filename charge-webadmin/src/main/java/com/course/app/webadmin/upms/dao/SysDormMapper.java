package com.course.app.webadmin.upms.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.webadmin.upms.model.SysDorm;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 宿舍信息数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface SysDormMapper extends BaseDaoMapper<SysDorm> {

    /**
     * 批量插入对象列表。
     *
     * @param sysDormList 新增对象列表。
     */
    void insertList(List<SysDorm> sysDormList);

    /**
     * 获取过滤后的对象列表。
     *
     * @param sysDormFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<SysDorm> getSysDormList(
            @Param("sysDormFilter") SysDorm sysDormFilter, @Param("orderBy") String orderBy);
}
