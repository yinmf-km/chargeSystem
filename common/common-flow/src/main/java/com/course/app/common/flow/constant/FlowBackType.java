package com.course.app.common.flow.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 待办任务回退类型。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public final class FlowBackType {

    /**
     * 驳回。
     */
    public static final int REJECT = 0;
    /**
     * 撤回。
     */
    public static final int REVOKE = 1;
    /**
     * 干预。
     */
    public static final int INTERVENE = 2;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(2);
    static {
        DICT_MAP.put(REJECT, "驳回");
        DICT_MAP.put(REVOKE, "撤回");
        DICT_MAP.put(INTERVENE, "干预");
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
    private FlowBackType() {
    }
}
