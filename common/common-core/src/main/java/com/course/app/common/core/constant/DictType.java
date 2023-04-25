package com.course.app.common.core.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典类型常量字典对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public final class DictType {

    /**
     * 数据表字典。
     */
    public static final int TABLE = 1;
    /**
     * URL字典。
     */
    public static final int URL = 5;
    /**
     * 常量字典。
     */
    public static final int CONST = 10;
    /**
     * 自定义字典。
     */
    public static final int CUSTOM = 15;
    /**
     * 全局编码字典。
     */
    public static final int GLOBAL_DICT = 20;

    private static final Map<Object, String> DICT_MAP = new HashMap<>(2);
    static {
        DICT_MAP.put(TABLE, "数据表字典");
        DICT_MAP.put(URL, "URL字典");
        DICT_MAP.put(CONST, "静态字典");
        DICT_MAP.put(CUSTOM, "自定义字典");
        DICT_MAP.put(GLOBAL_DICT, "全局编码字典");
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
    private DictType() {
    }
}
