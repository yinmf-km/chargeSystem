package com.course.app.common.core.util;

/**
 * Redis 键生成工具类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public class RedisKeyUtil {

    private static final String SESSIONID_PREFIX = "SESSIONID:";
    private static final String QYCG_PREFIX = "QYCG:";

    /**
     * 获取通用的session缓存的键前缀。
     *
     * @return session缓存的键前缀。
     */
    public static String getSessionIdPrefix() {
        return SESSIONID_PREFIX;
    }

    /**
     * 获取指定用户Id的session缓存的键前缀。
     *
     * @param loginName 指定的用户登录名。
     * @return session缓存的键前缀。
     */
    public static String getSessionIdPrefix(String loginName) {
        return SESSIONID_PREFIX + loginName + "_";
    }

    /**
     * 获取指定用户Id和登录设备类型的session缓存的键前缀。
     *
     * @param loginName  指定的用户登录名。
     * @param deviceType 设备类型。
     * @return session缓存的键前缀。
     */
    public static String getSessionIdPrefix(String loginName, int deviceType) {
        return SESSIONID_PREFIX + loginName + "_" + deviceType + "_";
    }

    /**
     * 计算SessionId返回存储于Redis中的键。
     *
     * @param sessionId 会话Id。
     * @return 会话存储于Redis中的键值。
     */
    public static String makeSessionIdKey(String sessionId) {
        return SESSIONID_PREFIX + sessionId;
    }

    /**
     * 计算SessionId关联的权限数据存储于Redis中的键。
     *
     * @param sessionId 会话Id。
     * @return 会话关联的权限数据存储于Redis中的键值。
     */
    public static String makeSessionPermIdKey(String sessionId) {
        return "PERM:" + sessionId;
    }

    /**
     * 计算SessionId关联的数据权限数据存储于Redis中的键。
     *
     * @param sessionId 会话Id。
     * @return 会话关联的数据权限数据存储于Redis中的键值。
     */
    public static String makeSessionDataPermIdKey(String sessionId) {
        return "DATA_PERM:" + sessionId;
    }

    /**
     * 计算包含全局字典及其数据项的缓存键。
     *
     * @param dictCode 全局字典编码。
     * @return 全局字典指定编码的缓存键。
     */
    public static String makeGlobalDictKey(String dictCode) {
        return "GLOBAL_DICT:" + dictCode;
    }

    /**
     * 计算仅仅包含全局字典对象数据的缓存键。
     *
     * @param dictCode 全局字典编码。
     * @return 全局字典指定编码的缓存键。
     */
    public static String makeGlobalDictOnlyKey(String dictCode) {
        return "GLOBAL_DICT_ONLY:" + dictCode;
    }

    /**
     * 计算会话的菜单Id关联权限资源URL的缓存键。
     *
     * @param sessionId 会话Id。
     * @param menuId    菜单Id。
     * @return 计算后的缓存键。
     */
    public static String makeSessionMenuPermKey(String sessionId, Object menuId) {
        return "SESSION_MENU_ID:" + sessionId + "-" + menuId.toString();
    }

    /**
     * 计算会话的菜单Id关联权限资源URL的缓存键的前缀。
     *
     * @param sessionId 会话Id。
     * @return 计算后的缓存键前缀。
     */
    public static String getSessionMenuPermPrefix(String sessionId) {
        return "SESSION_MENU_ID:" + sessionId + "-";
    }

    /**
     * 计算会话关联的白名单URL的缓存键。
     *
     * @param sessionId 会话Id。
     * @return 计算后的缓存键。
     */
    public static String makeSessionWhiteListPermKey(String sessionId) {
        return "SESSION_WHITE_LIST:" + sessionId;
    }

    /**
     * 计算会话关联指定部门Ids的子部门Ids的缓存键。
     *
     * @param sessionId 会话Id。
     * @param deptIds   部门Id，多个部门Id之间逗号分割。
     * @return 计算后的缓存键。
     */
    public static String makeSessionChildrenDeptIdKey(String sessionId, String deptIds) {
        return "SESSION_CHILDREN_DEPT_ID:" + sessionId + "-" + deptIds;
    }

    public static String makeRandomCodeH5Key(String phone, String code) {
        return String.format("%s%s%s_%s", QYCG_PREFIX, "RANDOMCODE_H5:", phone, code);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private RedisKeyUtil() {}
}
