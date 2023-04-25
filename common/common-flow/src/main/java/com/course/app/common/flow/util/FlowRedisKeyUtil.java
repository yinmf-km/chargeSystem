package com.course.app.common.flow.util;

/**
 * 工作流 Redis 键生成工具类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public class FlowRedisKeyUtil {

    /**
     * 计算流程对象缓存在Redis中的键值。
     *
     * @param processDefinitionKey 流程标识。
     * @return 流程对象缓存在Redis中的键值。
     */
    public static String makeFlowEntryKey(String processDefinitionKey) {
        return "FLOW_ENTRY:" + processDefinitionKey;
    }

    /**
     * 流程发布对象缓存在Redis中的键值。
     *
     * @param flowEntryPublishId 流程发布主键Id。
     * @return 流程发布对象缓存在Redis中的键值。
     */
    public static String makeFlowEntryPublishKey(Long flowEntryPublishId) {
        return "FLOW_ENTRY_PUBLISH:" + flowEntryPublishId;
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private FlowRedisKeyUtil() {
    }
}
