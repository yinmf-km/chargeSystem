package com.course.app.webadmin.upms.model.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核状态常量字典对象。
 * @author 云翼
 * @date 2023-02-21
 */
public final class SysProcessFlag {

	/**
	 * 审核中
	 */
	public static final int IN_REVIEW = 0;

	/**
	 * 同意
	 */
	public static final int AGREE = 1;

	/**
	 * 不同意
	 */
	public static final int DISAGREE = 2;

	private static final Map<Object, String> DICT_MAP = new HashMap<>(2);
	static {
		DICT_MAP.put(IN_REVIEW, "审核中");
		DICT_MAP.put(AGREE, "同意");
		DICT_MAP.put(DISAGREE, "不同意");
	}

	/**
	 * 判断参数是否为当前常量字典的合法值。
	 * @param value 待验证的参数值。
	 * @return 合法返回true，否则false。
	 */
	public static boolean isValid(Integer value) {
		return value != null && DICT_MAP.containsKey(value);
	}

	/**
	 * 私有构造函数，明确标识该常量类的作用。
	 */
	private SysProcessFlag() {
	}
}
