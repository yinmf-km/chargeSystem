package com.course.app.common.flow.model.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 工作流消息类型。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public final class FlowMessageType {

    /**
     * 催办消息。
     */
    public static final int REMIND_TYPE = 0;

    /**
     * 抄送消息。
     */
    public static final int COPY_TYPE = 1;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(2);
    static {
        DICT_MAP.put(REMIND_TYPE, "催办消息");
        DICT_MAP.put(COPY_TYPE, "抄送消息");
    }

    /**
     * 判断参数是否为当前常量字典的合法值。
     *
     * @param value 待验证的参数值。
     * @return 合法返回true，否则false。
     */
    public static boolean isValid(Integer value) {
        return value != null && DICT_MAP.containsKey(value);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private FlowMessageType() {
    }
}
