package com.course.app.common.flow.base.service;

import com.course.app.common.flow.model.FlowWorkOrder;

/**
 * 工作流在线表单的服务接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface BaseFlowOnlineService {

    /**
     * 更新在线表单主表数据的流程状态字段值。
     *
     * @param workOrder 工单对象。
     */
    void updateFlowStatus(FlowWorkOrder workOrder);

    /**
     * 根据工单对象级联删除业务数据。
     *
     * @param workOrder 工单对象。
     */
    void deleteBusinessData(FlowWorkOrder workOrder);
}
