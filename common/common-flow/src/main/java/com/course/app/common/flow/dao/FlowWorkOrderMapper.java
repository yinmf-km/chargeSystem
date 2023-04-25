package com.course.app.common.flow.dao;

import com.course.app.common.core.annotation.EnableDataPerm;
import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.flow.model.FlowWorkOrder;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 工作流工单表数据操作访问接口。
 * 如果当前系统支持数据权限过滤，当前用户必须要能看自己的工单数据，所以需要把EnableDataPerm
 * 的mustIncludeUserRule参数设置为true，即便当前用户的数据权限中并不包含DataPermRuleType.TYPE_USER_ONLY，
 * 数据过滤拦截组件也会自动补偿该类型的数据权限，以便当前用户可以看到自己发起的工单。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@EnableDataPerm(mustIncludeUserRule = true)
public interface FlowWorkOrderMapper extends BaseDaoMapper<FlowWorkOrder> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param flowWorkOrderFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<FlowWorkOrder> getFlowWorkOrderList(
            @Param("flowWorkOrderFilter") FlowWorkOrder flowWorkOrderFilter, @Param("orderBy") String orderBy);
}
