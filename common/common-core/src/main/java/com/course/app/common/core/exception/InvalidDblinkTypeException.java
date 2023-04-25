package com.course.app.common.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 无效的数据库链接类型自定义异常。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidDblinkTypeException extends RuntimeException {

    /**
     * 构造函数。
     *
     * @param dblinkType 数据库链接类型。
     */
    public InvalidDblinkTypeException(Long dblinkType) {
        super("Invalid Dblink Type [" + dblinkType + "].");
    }
}
