package com.course.app.common.flow.object;

import lombok.Data;

/**
 * 流程任务的扩展属性。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
public class FlowElementExtProperty {

    /**
     * 最近的审批状态，该值目前仅仅用于流程线元素，即SequenceElement。
     */
    private Integer latestApprovalStatus;
}
