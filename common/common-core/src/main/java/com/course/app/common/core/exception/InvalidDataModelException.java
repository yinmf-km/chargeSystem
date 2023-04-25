package com.course.app.common.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 无效的实体对象的自定义异常。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvalidDataModelException extends RuntimeException {

    private final String modelName;

    /**
     * 构造函数。
     *
     * @param modelName 实体对象名。
     */
    public InvalidDataModelException(String modelName) {
        super("Invalid Model Class [" + modelName + "].");
        this.modelName = modelName;
    }
}
