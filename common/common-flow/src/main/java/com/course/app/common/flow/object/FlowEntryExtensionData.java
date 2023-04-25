package com.course.app.common.flow.object;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 流程扩展数据对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
public class FlowEntryExtensionData {

    /**
     * 通知类型。
     */
    private List<String> notifyTypes;

    /**
     * 流程审批状态字典数据列表。Map的key是id和name。
     */
    private List<Map<String, String>> approvalStatusDict;

    /**
     * 级联删除业务数据。
     */
    private Boolean cascadeDeleteBusinessData = false;
}
