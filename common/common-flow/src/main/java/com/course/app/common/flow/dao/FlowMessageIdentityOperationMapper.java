package com.course.app.common.flow.dao;

import com.course.app.common.core.base.dao.BaseDaoMapper;
import com.course.app.common.flow.model.FlowMessageIdentityOperation;
import org.apache.ibatis.annotations.Param;

/**
 * 流程任务消息所属用户的操作数据操作访问接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface FlowMessageIdentityOperationMapper extends BaseDaoMapper<FlowMessageIdentityOperation> {

    /**
     * 删除指定流程实例的消息关联数据。
     *
     * @param processInstanceId 流程实例Id。
     */
    void deleteByProcessInstanceId(@Param("processInstanceId") String processInstanceId);
}
