package com.course.app.common.flow.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.flow.model.FlowEntryVariable;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 流程变量数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface FlowEntryVariableMapper extends BaseDaoMapper<FlowEntryVariable> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param flowEntryVariableFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<FlowEntryVariable> getFlowEntryVariableList(
            @Param("flowEntryVariableFilter") FlowEntryVariable flowEntryVariableFilter,
            @Param("orderBy") String orderBy);
}
