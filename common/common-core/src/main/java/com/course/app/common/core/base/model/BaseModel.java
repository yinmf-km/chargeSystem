package com.course.app.common.core.base.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;

/**
 * 实体对象的公共基类，所有子类均必须包含基类定义的数据表字段和实体对象字段。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
public class BaseModel {

    /**
     * 创建者Id。
     */
    @TableField("create_user_id")
    private Long createUserId;

    /**
     * 创建时间。
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新者Id。
     */
    @TableField("update_user_id")
    private Long updateUserId;

    /**
     * 更新时间。
     */
    @TableField("update_time")
    private Date updateTime;
}
