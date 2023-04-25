package com.course.app.common.flow.object;

import lombok.Data;

import java.util.List;

/**
 * 流程用户任务扩展数据对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
public class FlowUserTaskExtData {

    public static final String NOTIFY_TYPE_MSG = "message";
    public static final String NOTIFY_TYPE_EMAIL = "email";

    /**
     * 任务通知类型列表。
     */
    private List<String> flowNotifyTypeList;
}
